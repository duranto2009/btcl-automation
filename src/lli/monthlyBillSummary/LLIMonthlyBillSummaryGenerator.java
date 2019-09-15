package lli.monthlyBillSummary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lli.demandNote.adjustment.DNAdjustStatus;
import org.apache.log4j.Logger;

import accounting.AccountType;
import accounting.AccountingEntryService;
import accounting.GenerateBill;
import annotation.AccountingLogic;
import annotation.Transactional;
import lli.client.td.LLIProbableTDClientService;
import lli.connection.LLIConnectionConstants;
import lli.demandNote.adjustment.LLIDemandNoteAdjustment;
import lli.demandNote.adjustment.LLIDemandNoteAdjustmentService;
import lli.monthlyBill.LLIMonthlyBillByClient;
import lli.monthlyBill.LLIMonthlyBillByConnection;
import lli.monthlyUsage.LLIMonthlyUsageByClient;
import lli.monthlyUsage.LLIMonthlyUsageByConnection;
//import lombok.var;
import requestMapping.Service;
import util.DateUtils;
import util.NumberUtils;
import util.ServiceDAOFactory;
import util.TransactionType;

public class LLIMonthlyBillSummaryGenerator {

	static Logger logger = Logger.getLogger( LLIMonthlyBillSummaryGenerator.class );
	
	@Service
	LLIMonthlyBillSummaryByClientService lliMonthlyBillSummaryByClientService;
	@Service
	LLIMonthlyBillSummaryByItemService lliMonthlyBillSummaryByItemService;
	@Service
	LLIDemandNoteAdjustmentService lliDemandNoteAdjustmentService;
	@Service
	AccountingEntryService accountingEntryService;
	@Service
	LLIProbableTDClientService lliProbableTDClientService;
	
	List<LLIDemandNoteAdjustment> demandNoteAdjustments;
	
	
	class DayWiseUsage
	{
		double value;
		Map<Double, Double> discountUsage = new HashMap<Double, Double>();
	}
	
	public List<LLIMonthlyBillSummaryByClient> generateSummary(
			List<LLIMonthlyBillByClient> lliMonthlyBillByClients,
			List<LLIMonthlyUsageByClient> lliMonthlyUsageByClients,
			List<LLIMonthlyBillSummaryByClient> lastMonthSummaries) 
	{
		demandNoteAdjustments = lliDemandNoteAdjustmentService.getAllByStatus(DNAdjustStatus.ACTIVE);
		
		List<LLIMonthlyBillSummaryByClient> list = new ArrayList<>();
		
		for(LLIMonthlyBillByClient client : lliMonthlyBillByClients)
		{
			
			LLIMonthlyUsageByClient clientUsage = lliMonthlyUsageByClients
					.stream()
					.filter(x->x.getClientId() == client.getClientId())
					.findFirst()
					.orElse(new LLIMonthlyUsageByClient());
			
			LLIMonthlyBillSummaryByClient lastMonthClientSummary = lastMonthSummaries
					.stream()
					.filter(x->x.getClientId() == client.getClientId())
					.findFirst()
					.orElse(new LLIMonthlyBillSummaryByClient());
			
			try {
				LLIMonthlyBillSummaryByClient clientsummery = generateSummary(client, clientUsage, lastMonthClientSummary);
				
				save(clientsummery);
				list.add(clientsummery);
				
			}catch(Exception ex)
			{
				logger.error("Exception while generating LLIMonthlyBillSummaryByClient for client id = " + client.getClientId(), ex);
			}
		}
		
		return list;
	}

	
	@Transactional(transactionType=TransactionType.INDIVIDUAL_TRANSACTION)
	private void save(LLIMonthlyBillSummaryByClient clientsummery) throws Exception {
		
		lliMonthlyBillSummaryByClientService.save(clientsummery);
		if(clientsummery.getId() == null)
			return;
		
		for	(LLIMonthlyBillSummaryByItem item : clientsummery.getLliMonthlyBillSummaryByItems())
		{
			item.setMonthlyBillSummaryByClientId(clientsummery.getId());
			lliMonthlyBillSummaryByItemService.save(item);
		}
		
		calculateTDDate(clientsummery);
		updateDemandNoteAdjustmentAsCalculated(clientsummery.getClientID());
		insertIntoAccounting(clientsummery);
		
	}

	private LLIMonthlyBillSummaryByClient generateSummary(
			LLIMonthlyBillByClient clientBill, 
			LLIMonthlyUsageByClient clientUsage,
			LLIMonthlyBillSummaryByClient lastMonthClientSummary) throws Exception 
	{
		
		
		List<LLIMonthlyBillSummaryByItem> itemSummaries = new ArrayList<>();
		
		
		calculateCurrentMonthBill(itemSummaries, clientBill);
		calculateAdjustmentOfLastMonth(itemSummaries, clientUsage, lastMonthClientSummary);
		
		
		LLIMonthlyBillSummaryByClient clientSummary = new LLIMonthlyBillSummaryByClient();
		clientSummary.setLliMonthlyBillSummaryByItems(itemSummaries);
		
		clientSummary.setClientId(clientBill.getClientId());
		clientSummary.setTotalMbpsBreakDown(clientBill.getTotalMbpsBreakDown());
		clientSummary.setTotalMbpsBreakDownForCache(clientBill.getTotalMbpsBreakDownForCache());
		
		clientSummary.setBillingRangeBreakDown(clientBill.getBillingRangeBreakDown());
		clientSummary.setBillingRangeBreakDownForCache(clientBill.getBillingRangeBreakDownForCache());
		clientSummary.setLongTermContractBreakDown(clientBill.getLongTermContractBreakDown());
		clientSummary.setVatPercentage(clientBill.getVatPercentage());
		
		
		calculateBill(clientSummary);
		
		return clientSummary;
	}
	
	private void calculateCurrentMonthBill(
			List<LLIMonthlyBillSummaryByItem> itemSummaries,
			LLIMonthlyBillByClient clientBill)
	{
		LLIMonthlyBillSummaryByItem regularSummary = new LLIMonthlyBillSummaryByItem();
		LLIMonthlyBillSummaryByItem cacheSummary = new LLIMonthlyBillSummaryByItem();
		LLIMonthlyBillSummaryByItem loopSummary = new LLIMonthlyBillSummaryByItem();
		
		for (LLIMonthlyBillByConnection connection : clientBill.getMonthlyBillByConnections()) 
		{
			if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
			{
				regularSummary.setGrandCost(regularSummary.getGrandCost() + connection.getMbpsCost());// - connection.getDiscount());
				regularSummary.setDiscount(regularSummary.getDiscount() + connection.getDiscount());
			}
			else if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
			{
				cacheSummary.setGrandCost(cacheSummary.getGrandCost() + connection.getMbpsCost());// - connection.getDiscount());
				cacheSummary.setDiscount(cacheSummary.getDiscount() + connection.getDiscount());
			}
			
			loopSummary.setGrandCost(loopSummary.getGrandCost() + connection.getLoopCost());
			//no discount for local loop
		}
		
		double longTermAdjustmentBeforeDiscount = clientBill.getLongTermContructAdjustment() + clientBill.getLongTermContructDiscountAdjustment();
		regularSummary.setType(LLIMonthlyBillSummaryType.REGULAR);
		regularSummary.setGrandCost(NumberUtils.formattedValue(regularSummary.getGrandCost() - longTermAdjustmentBeforeDiscount));
		regularSummary.setDiscount(regularSummary.getDiscount() - clientBill.getLongTermContructDiscountAdjustment());
		regularSummary.setVatRate(clientBill.getVatPercentage());
		
		cacheSummary.setType(LLIMonthlyBillSummaryType.CACHE);
		cacheSummary.setVatRate(clientBill.getVatPercentage());
		
		loopSummary.setType(LLIMonthlyBillSummaryType.LOCAL_LOOP);
		loopSummary.setVatRate(clientBill.getVatPercentage());
		
		
		
		itemSummaries.add(regularSummary);
		itemSummaries.add(cacheSummary);
		itemSummaries.add(loopSummary);
		
	}
	
	private void calculateAdjustmentOfLastMonth(
			List<LLIMonthlyBillSummaryByItem> itemSummaries,
			LLIMonthlyUsageByClient clientUsage,
			LLIMonthlyBillSummaryByClient lastMonthClientSummary)
	{
		LLIMonthlyBillSummaryByItem regularAdjustmentSummary = new LLIMonthlyBillSummaryByItem();
		LLIMonthlyBillSummaryByItem cacheAdjustmentSummary = new LLIMonthlyBillSummaryByItem();
		LLIMonthlyBillSummaryByItem loopAdjustmentSummary = new LLIMonthlyBillSummaryByItem();
		
		for (LLIMonthlyUsageByConnection connection : clientUsage.getMonthlyUsageByConnections()) 
		{
			if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_REGULAR)
			{
				regularAdjustmentSummary.setGrandCost(regularAdjustmentSummary.getGrandCost() + connection.getMbpsCost());// - connection.getDiscount());
				regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() + connection.getDiscount());
			}
			else if(connection.getType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
			{
				cacheAdjustmentSummary.setGrandCost(cacheAdjustmentSummary.getGrandCost() + connection.getMbpsCost());// - connection.getDiscount());
				cacheAdjustmentSummary.setDiscount(cacheAdjustmentSummary.getDiscount() + connection.getDiscount());
			}
			
			loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() + connection.getLoopCost());
			//no discount for loop cost
		}
		
		double longTermAdjustmentBeforeDiscount = clientUsage.getLongTermContructAdjustment() + clientUsage.getLongTermContructDiscountAdjustment();
		regularAdjustmentSummary.setType(LLIMonthlyBillSummaryType.REGULAR_ADJUSTMENT);
		regularAdjustmentSummary.setGrandCost(NumberUtils.formattedValue(regularAdjustmentSummary.getGrandCost() - longTermAdjustmentBeforeDiscount));
		regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() - clientUsage.getLongTermContructDiscountAdjustment());
		regularAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());
		
		cacheAdjustmentSummary.setType(LLIMonthlyBillSummaryType.CACHE_ADJUSTMENT);
		cacheAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());
		
		loopAdjustmentSummary.setType(LLIMonthlyBillSummaryType.LOCAL_LOOP_ADJUSTMENT);
		loopAdjustmentSummary.setVatRate(clientUsage.getVatPercentage());
		
		for (LLIMonthlyBillSummaryByItem lastMonthItem : lastMonthClientSummary.getLliMonthlyBillSummaryByItems())
		{
			if(lastMonthItem.getType() == LLIMonthlyBillSummaryType.REGULAR)
			{
				regularAdjustmentSummary.setGrandCost(regularAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
				regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() - lastMonthItem.getDiscount());
			}
			else if(lastMonthItem.getType() == LLIMonthlyBillSummaryType.CACHE)
			{
				cacheAdjustmentSummary.setGrandCost(cacheAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
				cacheAdjustmentSummary.setDiscount(cacheAdjustmentSummary.getDiscount() - lastMonthItem.getDiscount());
			}
			else if(lastMonthItem.getType() == LLIMonthlyBillSummaryType.LOCAL_LOOP)
			{
				loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() - lastMonthItem.getGrandCost());
			}
			
		}
		
		//adjust from demand note if exists
		List<LLIDemandNoteAdjustment> listOfDemandNoteAdjustment = demandNoteAdjustments.stream().filter(d->d.getClientId() == clientUsage.getClientId()).collect(Collectors.toList());
		if(demandNoteAdjustments.size() > 0)
		{
			LLIMonthlyBillSummaryByItem demandNoteAdjustmentSummary = new LLIMonthlyBillSummaryByItem();
			demandNoteAdjustmentSummary.setType(LLIMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT);
			
			for(LLIDemandNoteAdjustment demandNoteAdjustment : listOfDemandNoteAdjustment)
			{
				if(demandNoteAdjustment.getConnectionType() == LLIConnectionConstants.CONNECTION_TYPE_CACHE)
				{
					cacheAdjustmentSummary.setGrandCost(cacheAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getBandWidthCharge());
					cacheAdjustmentSummary.setDiscount(cacheAdjustmentSummary.getDiscount() - demandNoteAdjustment.getBandWidthDiscount());
				}
				else
				{
					regularAdjustmentSummary.setGrandCost(regularAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getBandWidthCharge());
					regularAdjustmentSummary.setDiscount(regularAdjustmentSummary.getDiscount() - demandNoteAdjustment.getBandWidthDiscount());
				}
			
				loopAdjustmentSummary.setGrandCost(loopAdjustmentSummary.getGrandCost() - demandNoteAdjustment.getLoopCharge());
			
				if(demandNoteAdjustment.getTotalDue() > 0)
				{
					demandNoteAdjustmentSummary.setGrandCost(demandNoteAdjustmentSummary.getGrandCost() + demandNoteAdjustment.getTotalDue());
					// to handle change in vat rate we are summing up demand note vat here
					demandNoteAdjustmentSummary.setVat(demandNoteAdjustmentSummary.getVat() + demandNoteAdjustment.getVat());	
					
					//we will not add discount into adjustment 
					//cause TotalDue of demandNoteAdjustment is after discounted amount. 
					//Besides discount already included as BandwidthDiscount which has already been calculated above
					//Though we are adding in grandCost of demandNoteAdjustmentSummary
				}
			}
			
			if(demandNoteAdjustmentSummary.getGrandCost() > 0)
				itemSummaries.add(demandNoteAdjustmentSummary);
			
		}
		
		itemSummaries.add(regularAdjustmentSummary);
		itemSummaries.add(cacheAdjustmentSummary);
		itemSummaries.add(loopAdjustmentSummary);
		
	}
	
	
	private void calculateBill(LLIMonthlyBillSummaryByClient clientBillSummary) throws Exception
	{
		double totalDiscount = 0, subTotal = 0, totalVat = 0;
		LLIMonthlyBillSummaryByItem demandNoteAdjustmentSummary = null;
		
		for (LLIMonthlyBillSummaryByItem sc : clientBillSummary.getLliMonthlyBillSummaryByItems()) 
		{
			sc.setType();
			sc.setGrandCost(NumberUtils.formattedValue(sc.getGrandCost()));
			sc.setTotalCost(NumberUtils.formattedValue(sc.getGrandCost() - sc.getDiscount()));

			if(sc.getType() == LLIMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT)
			{
				demandNoteAdjustmentSummary = sc;
				sc.setVat(NumberUtils.formattedValue(sc.getVat()));
			}
			else
			{
				sc.setVat(NumberUtils.formattedValue(sc.getTotalCost() * sc.getVatRate()/100));
			}
			
			sc.setNetCost(NumberUtils.formattedValue(sc.getTotalCost() + sc.getVat()));
			
			subTotal += sc.getTotalCost();
			totalVat += sc.getVat();
			totalDiscount += sc.getDiscount();
			
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
	private void calculateTDDate(LLIMonthlyBillSummaryByClient clientBillSummary) throws Exception
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
		
		double totalBWCost = clientBillSummary.getLliMonthlyBillSummaryByItems()
				.stream()
				.filter(x->x.type == LLIMonthlyBillSummaryType.REGULAR
						|| x.type == LLIMonthlyBillSummaryType.CACHE)
				.mapToDouble(x->x.getGrandCost())
				.sum();
		
		double perDayBWCost = totalBWCost/30;
		int remainingDays = perDayBWCost <= 0 ? 0 : (int) (availableAmount / perDayBWCost);
		
		
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, remainingDays);
		long tdDate = cal.getTime().getTime();
		
		lliProbableTDClientService.updateTDDate(clientBillSummary.getClientID(), tdDate);
		
	}
	
	@Transactional
	private void insertIntoAccounting(LLIMonthlyBillSummaryByClient lliMonthlyBillByClient) throws Exception {
		AccountingLogic accountingLogic = lliMonthlyBillByClient.getClass().getAnnotation(AccountingLogic.class);
		
		if(accountingLogic != null &&  accountingLogic.value().newInstance() instanceof GenerateBill  )
		{
			GenerateBill generateBill = (GenerateBill) ServiceDAOFactory.getService(accountingLogic.value());
			
			double tempNetBill = lliMonthlyBillByClient.getNetPayable();
			double tempVat = lliMonthlyBillByClient.getVAT();
			
			
			LLIMonthlyBillSummaryByItem itemForDemandNote = lliMonthlyBillByClient.getLliMonthlyBillSummaryByItems()
					.stream()
					.filter(x-> x.getType() == LLIMonthlyBillSummaryType.DEMANDNOTE_ADJUSTMENT)
					.findFirst()
					.orElse(null);
			
			if(itemForDemandNote != null)
			{
				//for generating monthly bill incedient without demand note
				lliMonthlyBillByClient.setNetPayable(NumberUtils.formattedValue(lliMonthlyBillByClient.getNetPayable() - itemForDemandNote.getNetCost()));		
				lliMonthlyBillByClient.setVAT(NumberUtils.formattedValue(lliMonthlyBillByClient.getVAT() - itemForDemandNote.getVat()));
				
				if(lliMonthlyBillByClient.getNetPayable() < 0)
					lliMonthlyBillByClient.setNetPayable(0);
				if(lliMonthlyBillByClient.getVAT() < 0)
					lliMonthlyBillByClient.setVAT(0);
					
			}
			
			generateBill.generate(lliMonthlyBillByClient);
			
			lliMonthlyBillByClient.setNetPayable(tempNetBill);
			lliMonthlyBillByClient.setVAT(tempVat);
		}
	}
	
	@Transactional
	private void updateDemandNoteAdjustmentAsCalculated(long clientId) throws Exception 
	{
		List<LLIDemandNoteAdjustment> listOfDemandNoteAdjustment = demandNoteAdjustments.stream().filter(d->d.getClientId() == clientId).collect(Collectors.toList());
		if(listOfDemandNoteAdjustment.size() > 0)
		{
			for(LLIDemandNoteAdjustment demandNoteAdjustment: listOfDemandNoteAdjustment)
			{
				demandNoteAdjustment.setStatus(DNAdjustStatus.COMPLETED);
				lliDemandNoteAdjustmentService.save(demandNoteAdjustment);
			}
		}
	}
	
}
