package nix.monthlybillsummary;

import accounting.AccountType;
import accounting.AccountingEntryService;
import accounting.GenerateBill;
import annotation.AccountingLogic;
import annotation.Transactional;
import nix.monthlybill.NIXMonthlyBillByClient;
import nix.monthlybill.NIXMonthlyBillByConnection;
import nix.monthlyusage.NIXMonthlyUsageByClient;
import nix.monthlyusage.NIXMonthlyUsageByConnection;
import nix.revise.NIXProbableTDClientService;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.DateUtils;
import util.NumberUtils;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.*;


public class NIXMonthlyBillSummaryGenerator {

	static Logger logger = Logger.getLogger( NIXMonthlyBillSummaryGenerator.class );
	
	@Service
	NIXMonthlyBillSummaryByClientService nixMonthlyBillSummaryByClientService;
	@Service
	NIXMonthlyBillSummaryByItemService nixMonthlyBillSummaryByItemService;
//	@Service
//	NIXDemandNoteAdjustmentService nixDemandNoteAdjustmentService;
	
	@Service
	AccountingEntryService accountingEntryService;
	@Service
	NIXProbableTDClientService nixProbableTDClientService;
	
//	List<NIXDemandNoteAdjustment> demandNoteAdjustments;
	
	
	class DayWiseUsage
	{
		double value;
		Map<Double, Double> discountUsage = new HashMap<Double, Double>();
	}
	
	public List<NIXMonthlyBillSummaryByClient> generateSummary(
			List<NIXMonthlyBillByClient> nixMonthlyBillByClients,
			List<NIXMonthlyUsageByClient> nixMonthlyUsageByClientsBillByClients,
			List<NIXMonthlyBillSummaryByClient> lastMonthSummaries) 
	{
//		demandNoteAdjustments = nixDemandNoteAdjustmentService.getAllByStatus(DNAdjustStatus.ACTIVE);
		
		List<NIXMonthlyBillSummaryByClient> nixMonthlyBillSummaryByClientList = new ArrayList<>();
		
		for(NIXMonthlyBillByClient nixMonthlyBillByClient : nixMonthlyBillByClients)
		{
			
			NIXMonthlyUsageByClient clientUsage = nixMonthlyUsageByClientsBillByClients
					.stream()
					.filter(x->x.getClientId() == nixMonthlyBillByClient.getClientId())
					.findFirst()
					.orElse(new NIXMonthlyUsageByClient());
			
			NIXMonthlyBillSummaryByClient lastMonthClientSummary = lastMonthSummaries
					.stream()
					.filter(x->x.getClientId() == nixMonthlyBillByClient.getClientId())
					.findFirst()
					.orElse(new NIXMonthlyBillSummaryByClient());
			
			try {
				NIXMonthlyBillSummaryByClient clientsummery = generateSummary(nixMonthlyBillByClient, clientUsage, lastMonthClientSummary);
				
				save(clientsummery);
				nixMonthlyBillSummaryByClientList.add(clientsummery);
				
			}catch(Exception ex)
			{
				logger.error("Exception while generating NIXMonthlyBillSummaryByClient for client id = " + nixMonthlyBillByClient.getClientId(), ex);
			}
		}
		
		return nixMonthlyBillSummaryByClientList;
	}

	
	@Transactional(transactionType=TransactionType.INDIVIDUAL_TRANSACTION)
	private void save(NIXMonthlyBillSummaryByClient clientsummery) throws Exception {
		
		nixMonthlyBillSummaryByClientService.save(clientsummery);
		if(clientsummery.getId() == null)
			return;
		
		for	(NIXMonthlyBillSummaryByItem nixMonthlyBillSummaryByItem : clientsummery.getNixMonthlyBillSummaryByItems())
		{
			nixMonthlyBillSummaryByItem.setMonthlyBillSummaryByClientId(clientsummery.getId());
			nixMonthlyBillSummaryByItemService.save(nixMonthlyBillSummaryByItem);
		}
		//todo : td date calculation and accounting calculation
		calculateTDDate(clientsummery);
//		updateDemandNoteAdjustmentAsCalculated(clientsummery.getClientID());
		insertIntoAccounting(clientsummery);
		
	}

	private NIXMonthlyBillSummaryByClient generateSummary(
			NIXMonthlyBillByClient clientBill, 
			NIXMonthlyUsageByClient clientUsage,
			NIXMonthlyBillSummaryByClient lastMonthClientSummary) throws Exception 
	{
		
		
		List<NIXMonthlyBillSummaryByItem> itemSummaries = new ArrayList<>();
		
		
		calculateCurrentMonthBill(itemSummaries, clientBill);
		calculateAdjustmentOfLastMonth(itemSummaries, clientUsage, lastMonthClientSummary);
		NIXMonthlyBillSummaryByClient clientSummary = new NIXMonthlyBillSummaryByClient();
		clientSummary.setNixMonthlyBillSummaryByItems(itemSummaries);
		clientSummary.setClientId(clientBill.getClientId());
		clientSummary.setVatPercentage(clientBill.getVatPercentage());
		calculateBill(clientSummary);
		
		return clientSummary;
	}
	
	private void calculateCurrentMonthBill(
			List<NIXMonthlyBillSummaryByItem> itemSummaries,
			NIXMonthlyBillByClient clientBill)
	{
//		NIXMonthlyBillSummaryByItem regularSummary = new NIXMonthlyBillSummaryByItem();
		NIXMonthlyBillSummaryByItem loopSummary = new NIXMonthlyBillSummaryByItem();
		NIXMonthlyBillSummaryByItem portSummary = new NIXMonthlyBillSummaryByItem();

		for (NIXMonthlyBillByConnection nixMonthlyBillByConnection : clientBill.getMonthlyBillByConnections())
		{
//			if(nixMonthlyBillByConnection.getType() == NIXConstants.CONNECTION_TYPE_REGULAR)
//			{
////				regularSummary.setGrandCost(regularSummary.getGrandCost() + connection.getMbpsCost());// - connection.getDiscount());
//				regularSummary.setGrandCost(regularSummary.getGrandCost() - nixMonthlyBillByConnection.getDiscount());
//				regularSummary.setDiscount(regularSummary.getDiscount() + nixMonthlyBillByConnection.getDiscount());
//			}
			
			loopSummary.setGrandCost(loopSummary.getGrandCost() + nixMonthlyBillByConnection.getLoopCost());
			portSummary.setGrandCost(portSummary.getGrandCost()+nixMonthlyBillByConnection.getPortCost());
			//no discount for local loop
		}
		
//		regularSummary.setType(NIXMonthlyBillSummaryType.REGULAR);
//		regularSummary.setVatRate(clientBill.getVatPercentage());
		loopSummary.setType(NIXMonthlyBillSummaryType.LOCAL_LOOP);
		loopSummary.setVatRate(clientBill.getVatPercentage());
		portSummary.setType(NIXMonthlyBillSummaryType.PORT);
		portSummary.setVatRate(clientBill.getVatPercentage());
//		itemSummaries.add(regularSummary);
		itemSummaries.add(loopSummary);
		itemSummaries.add(portSummary);

	}
	
	private void calculateAdjustmentOfLastMonth(
			List<NIXMonthlyBillSummaryByItem> itemSummaries,
			NIXMonthlyUsageByClient clientUsage,
			NIXMonthlyBillSummaryByClient lastMonthClientSummary)
	{
//		NIXMonthlyBillSummaryByItem regularAdjustmentSummary = new NIXMonthlyBillSummaryByItem();
		NIXMonthlyBillSummaryByItem portAdjustmentSummary = new NIXMonthlyBillSummaryByItem();
		NIXMonthlyBillSummaryByItem loopAdjustmentSummary = new NIXMonthlyBillSummaryByItem();
		
		for (NIXMonthlyUsageByConnection nixMonthlyUsageByConnection : clientUsage.getMonthlyUsageByConnections())
		{
			
//				regularAdjustmentSummary.setGrandCost(regularAdjustmentSummary.getGrandCost() - nixMonthlyUsageByConnection.getDiscount());
//				regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() + nixMonthlyUsageByConnection.getDiscount());
//
			
			loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() + nixMonthlyUsageByConnection.getLoopCost());
			portAdjustmentSummary.setGrandCost(portAdjustmentSummary.getGrandCost() + nixMonthlyUsageByConnection.getPortCost());
			//no discount for loop cost
		}
//
//		regularAdjustmentSummary.setType(NIXMonthlyBillSummaryType.REGULAR_ADJUSTMENT);
//		regularAdjustmentSummary.setGrandCost(NumberUtils.formattedValue(regularAdjustmentSummary.getGrandCost()));
//		regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount());
//		regularAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());
		
		loopAdjustmentSummary.setType(NIXMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT);
		loopAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());


		portAdjustmentSummary.setType(NIXMonthlyBillSummaryType.PORT_ADJUSTMENT);
		portAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());
		
		for (NIXMonthlyBillSummaryByItem lastMonthItem : lastMonthClientSummary.getNixMonthlyBillSummaryByItems())
		{
//			if(lastMonthItem.getType() == NIXMonthlyBillSummaryType.REGULAR)
//			{
//				regularAdjustmentSummary.setGrandCost(regularAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
//				regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() - lastMonthItem.getDiscount());
//			}
//			else
				if(lastMonthItem.getType() == NIXMonthlyBillSummaryType.LOCAL_LOOP)
			{
				loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
			}
			else if(lastMonthItem.getType() == NIXMonthlyBillSummaryType.PORT)
			{
				portAdjustmentSummary.setGrandCost(portAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
			}
			
		}
		
		//todo :adjust from demand note if exists
//		List<NIXDemandNoteAdjustment> listOfDemandNoteAdjustment = demandNoteAdjustments.stream().filter(d->d.getClientId() == clientUsage.getClientId()).collect(Collectors.toList());
//		if(demandNoteAdjustments.size() > 0)
//		{
//			NIXMonthlyBillSummaryByItem demandNoteAdjustmentSummary = new NIXMonthlyBillSummaryByItem();
//			demandNoteAdjustmentSummary.setType(NIXMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT);
//			
//			for(NIXDemandNoteAdjustment demandNoteAdjustment : listOfDemandNoteAdjustment)
//			{
//				if(demandNoteAdjustment.getConnectionType() == NIXConnectionConstants.CONNECTION_TYPE_CACHE)
//				{
//					cacheAdjustmentSummary.setGrandCost(cacheAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getBandWidthCharge());
//					cacheAdjustmentSummary.setDiscount(cacheAdjustmentSummary.getDiscount() - demandNoteAdjustment.getBandWidthDiscount());
//				}
//				else
//				{
//					regularAdjustmentSummary.setGrandCost(regularAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getBandWidthCharge());
//					regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() - demandNoteAdjustment.getBandWidthDiscount());
//				}
//			
//				loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getLoopCharge());
//			
//				if(demandNoteAdjustment.getTotalDue() > 0)
//				{
//					demandNoteAdjustmentSummary.setGrandCost(demandNoteAdjustmentSummary.getGrandCost() + demandNoteAdjustment.getTotalDue());
//					// to handle change in vat rate we are summing up demand note vat here
//					demandNoteAdjustmentSummary.setVat(demandNoteAdjustmentSummary.getVat() + demandNoteAdjustment.getVat());	
//					
//					//we will not add discount into adjustment 
//					//cause TotalDue of demandNoteAdjustment is after discounted amount. 
//					//Besides discount already included as BandwidthDiscount which has already been calculated above
//					//Though we are adding in grandCost of demandNoteAdjustmentSummary
//				}
//			}
//			
//			if(demandNoteAdjustmentSummary.getGrandCost() > 0)
//				itemSummaries.add(demandNoteAdjustmentSummary);
//			
//		}
		
//		itemSummaries.add(regularAdjustmentSummary);
		itemSummaries.add(portAdjustmentSummary);
		itemSummaries.add(loopAdjustmentSummary);
		
	}
	
	
	private void calculateBill(NIXMonthlyBillSummaryByClient clientBillSummary) throws Exception
	{
		double totalDiscount = 0, subTotal = 0, totalVat = 0;
		NIXMonthlyBillSummaryByItem demandNoteAdjustmentSummary = null;
		
		for (NIXMonthlyBillSummaryByItem nixMonthlyBillSummaryByItem : clientBillSummary.getNixMonthlyBillSummaryByItems())
		{
			nixMonthlyBillSummaryByItem.setType();
			nixMonthlyBillSummaryByItem.setGrandCost(NumberUtils.formattedValue(nixMonthlyBillSummaryByItem.getGrandCost()));
			nixMonthlyBillSummaryByItem.setTotalCost(NumberUtils.formattedValue(nixMonthlyBillSummaryByItem.getGrandCost() - nixMonthlyBillSummaryByItem.getDiscount()));

			if(nixMonthlyBillSummaryByItem.getType() == NIXMonthlyBillSummaryType.DEMAND_NOTE_ADJUSTMENT)
			{
				demandNoteAdjustmentSummary = nixMonthlyBillSummaryByItem;
				nixMonthlyBillSummaryByItem.setVat(NumberUtils.formattedValue(nixMonthlyBillSummaryByItem.getVat()));
			}
			else
			{
				nixMonthlyBillSummaryByItem.setVat(NumberUtils.formattedValue(nixMonthlyBillSummaryByItem.getTotalCost() * nixMonthlyBillSummaryByItem.getVatRate()/100));
			}

			nixMonthlyBillSummaryByItem.setNetCost(NumberUtils.formattedValue(nixMonthlyBillSummaryByItem.getTotalCost() + nixMonthlyBillSummaryByItem.getVat()));
			
			subTotal += nixMonthlyBillSummaryByItem.getTotalCost();
			totalVat += nixMonthlyBillSummaryByItem.getVat();
			totalDiscount += nixMonthlyBillSummaryByItem.getDiscount();
			
		}
		//TODO :need to get yearly fee for additional ip
		
		
		subTotal = NumberUtils.formattedValue(subTotal);
		//get adjustment balance info from table
		double balance = accountingEntryService.getBalanceByClientIDAndAccountID(clientBillSummary.getClientId(), AccountType.ADJUSTABLE.getID());
		double adjustedAmount = 0.0;
		if(demandNoteAdjustmentSummary != null)
		{
			double subTotalWithoutDN = NumberUtils.formattedValue(subTotal - demandNoteAdjustmentSummary.getTotalCost());
			//adjustedAmount = subTotalWithoutDN <= 0 ? 0 : Math.min(subTotalWithoutDN, balance);
			adjustedAmount = Math.min(subTotalWithoutDN, balance);
		}
		else
			//adjustedAmount = subTotal <= 0 ? 0 : Math.min(subTotal, balance);
			adjustedAmount = Math.min(subTotal, balance);
		
		
		  
		clientBillSummary.setAdjustmentAmount(NumberUtils.formattedValue(adjustedAmount));
		clientBillSummary.setGrandTotal(NumberUtils.formattedValue(subTotal));
		
		clientBillSummary.setTotalPayable( NumberUtils.formattedValue( clientBillSummary.getGrandTotal() - clientBillSummary.getAdjustmentAmount()));
		//if(clientBillSummary.getTotalPayable() < 0)
		//	clientBillSummary.setTotalPayable(0);
		
		/********/
		//not using calculated total vat cause there arises a issue while vat changed and totalpayable is zero
		clientBillSummary.setVAT(Math.ceil(clientBillSummary.getTotalPayable() * clientBillSummary.getVatPercentage()/100));
		clientBillSummary.setNetPayable(NumberUtils.formattedValue(clientBillSummary.getTotalPayable() + clientBillSummary.getVAT()));
		

		clientBillSummary.setCreatedDate(DateUtils.getCurrentTime());
		
		//bill dto property set
		clientBillSummary.setGenerationTime(DateUtils.getCurrentTime());
		clientBillSummary.setActivationTimeFrom(DateUtils.getFirstDayOfMonth(0).getTime());
		clientBillSummary.setActivationTimeTo(DateUtils.getLastDayOfMonth(0).getTime());
		clientBillSummary.setLastPaymentDate(DateUtils.getLastDayOfMonth(0).getTime());
		clientBillSummary.setDeleted(false);
		clientBillSummary.setMonth(DateUtils.getMonthFromDate(DateUtils.getCurrentTime()));
		clientBillSummary.setYear(DateUtils.getYearFromDate(DateUtils.getCurrentTime()));
		
		
		clientBillSummary.setDiscountPercentage(0);
		clientBillSummary.setDiscount(NumberUtils.formattedValue(totalDiscount));
		
		

	}
	
	
	@Transactional
	private void calculateTDDate(NIXMonthlyBillSummaryByClient clientBillSummary) throws Exception
	{
		//get security money balance info from table
		double securityAmount = accountingEntryService.getBalanceByClientIDAndAccountID(
				clientBillSummary.getClientId(),
				AccountType.SECURITY.getID());
		
		//get adjustment balance info from table
		double balance = accountingEntryService.getBalanceByClientIDAndAccountID(
				clientBillSummary.getClientId(), 
				AccountType.ADJUSTABLE.getID());
		
		//get total due info from table
		double receivableAmount = accountingEntryService.getBalanceByClientIDAndAccountID(
				clientBillSummary.getClientId(),
				AccountType.ACCOUNT_RECEIVABLE_TD.getID());
		
		
		double availableAmount = securityAmount + balance - receivableAmount;


		double totalCost = clientBillSummary.getNixMonthlyBillSummaryByItems()
				.stream()
				.filter(x->x.type == NIXMonthlyBillSummaryType.REGULAR)
				.mapToDouble(x->x.getGrandCost())
				.sum();
//		
		double perDayCost = totalCost/30;
		int remainingDays = perDayCost <= 0 ? 0 : (int) (availableAmount / perDayCost);
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, remainingDays);
		long tdDate = cal.getTime().getTime();
		
		nixProbableTDClientService.updateTDDate(clientBillSummary.getClientID(), tdDate);
		
	}
	
	@Transactional
	private void insertIntoAccounting(NIXMonthlyBillSummaryByClient nixMonthlyBillByClient) throws Exception {
		AccountingLogic accountingLogic = nixMonthlyBillByClient.getClass().getAnnotation(AccountingLogic.class);
		
		if(accountingLogic != null &&  accountingLogic.value().newInstance() instanceof GenerateBill  )
		{
			GenerateBill generateBill = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());
			
			double tempNetBill = nixMonthlyBillByClient.getNetPayable();
			double tempVat = nixMonthlyBillByClient.getVAT();
			
			
			NIXMonthlyBillSummaryByItem itemForDemandNote = nixMonthlyBillByClient.getNixMonthlyBillSummaryByItems()
					.stream()
					.filter(x-> x.getType() == NIXMonthlyBillSummaryType.DEMAND_NOTE_ADJUSTMENT)
					.findFirst()
					.orElse(null);
			
			if(itemForDemandNote != null)
			{
				//for generating monthly bill incedient without demand note
				nixMonthlyBillByClient.setNetPayable(NumberUtils.formattedValue(nixMonthlyBillByClient.getNetPayable() - itemForDemandNote.getNetCost()));
				nixMonthlyBillByClient.setVAT(NumberUtils.formattedValue(nixMonthlyBillByClient.getVAT() - itemForDemandNote.getVat()));
				
				if(nixMonthlyBillByClient.getNetPayable() < 0)
					nixMonthlyBillByClient.setNetPayable(0);
				if(nixMonthlyBillByClient.getVAT() < 0)
					nixMonthlyBillByClient.setVAT(0);
					
			}
			
			generateBill.generate(nixMonthlyBillByClient);
			
			nixMonthlyBillByClient.setNetPayable(tempNetBill);
			nixMonthlyBillByClient.setVAT(tempVat);
		}
	}
	
//	@Transactional
//	private void updateDemandNoteAdjustmentAsCalculated(long clientId) throws Exception 
//	{
//		List<NIXDemandNoteAdjustment> listOfDemandNoteAdjustment = demandNoteAdjustments.stream().filter(d->d.getClientId() == clientId).collect(Collectors.toList());
//		if(listOfDemandNoteAdjustment.size() > 0)
//		{
//			for(NIXDemandNoteAdjustment demandNoteAdjustment: listOfDemandNoteAdjustment)
//			{
//				demandNoteAdjustment.setStatus(DNAdjustStatus.COMPLETED);
//				nixDemandNoteAdjustmentService.save(demandNoteAdjustment);
//			}
//		}
//	}
	
}
