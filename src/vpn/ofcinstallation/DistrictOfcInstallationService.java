package vpn.ofcinstallation;

import java.util.ArrayList;
import java.util.List;

import common.RequestFailureException;
import inventory.InventoryConstants;
import inventory.InventoryService;
import org.apache.log4j.Logger;

import annotation.Transactional;
import common.CategoryConstants;
import inventory.InventoryDAO;
import inventory.InventoryItem;
import util.DatabaseConnectionFactory;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;

public class DistrictOfcInstallationService {
	
	static org.apache.log4j.Logger logger = Logger.getLogger(DistrictOfcInstallationService.class);
	
	DistrictOfcInstallationDAO districtOfcInstallationDAO = new DistrictOfcInstallationDAO();
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public List<DistrictOfcInstallationDTO> getAllDistrictOfcInstallationCosts() throws Exception{
		List<DistrictOfcInstallationDTO> allDistrictOfcInstallationCosts = new ArrayList<DistrictOfcInstallationDTO>();
		
		InventoryDAO inventoryDAO = new InventoryDAO();
		
		List<InventoryItem> allInventoryItemsOfDistrictCategoryForNames = inventoryDAO.getInventoryItemLisByCatagoryID(CategoryConstants.CATEGORY_ID_DISTRICT, DatabaseConnectionFactory.getCurrentDatabaseConnection());
		
//		logger.fatal(allInventoryItemsOfDistrictCategoryForNames);
		
		for(InventoryItem district : allInventoryItemsOfDistrictCategoryForNames){
//			logger.fatal(district);
			DistrictOfcInstallationDTO districtOfcInstallationDTO = new DistrictOfcInstallationDTO();
			if(isDistrictAlreadyInCostTable(district.getID())){
				districtOfcInstallationDTO = districtOfcInstallationDAO.getDistrictOfcInstallationDTOByDistrictID(district.getID()).stream().findFirst().orElse(null);
				if(districtOfcInstallationDTO != null){
					districtOfcInstallationDTO.setDistrictName(district.getName());
				}
			}
			else{
				districtOfcInstallationDTO.setDistrictID(district.getID());
				districtOfcInstallationDTO.setInstallationCost(0);
				districtOfcInstallationDAO.insertDistrictOfcInstallationDTO(districtOfcInstallationDTO);
				
				districtOfcInstallationDTO = districtOfcInstallationDAO.getDistrictOfcInstallationDTOByDistrictID(district.getID()).stream().findFirst().orElse(null);
				if(districtOfcInstallationDTO != null){
					districtOfcInstallationDTO.setDistrictName(district.getName());
				}
			}
			allDistrictOfcInstallationCosts.add(districtOfcInstallationDTO);
		}
		
		return allDistrictOfcInstallationCosts;
	}

	private boolean isDistrictAlreadyInCostTable(long districtID) throws Exception {
		if(null==districtOfcInstallationDAO.getDistrictOfcInstallationDTOByDistrictID(districtID)){
			return false;
		}
		return true;
	}

	@Transactional
	public void updateAllDistrictOfcInstallationCost(List<DistrictOfcInstallationDTO> districtOfcInstallationDTOListToBeUpdated) throws Exception {
		for(DistrictOfcInstallationDTO districtOfcInstallationDTO : districtOfcInstallationDTOListToBeUpdated){
			DistrictOfcInstallationDTO oldDistrictOfcInstallationDTO = districtOfcInstallationDAO
					.getDistrictOfcInstallationDTOByDistrictID(districtOfcInstallationDTO.getDistrictID())
					.stream()
					.findFirst()
					.orElse(null);
			
			if(oldDistrictOfcInstallationDTO != null)oldDistrictOfcInstallationDTO.setInstallationCost(districtOfcInstallationDTO.getInstallationCost());
			
			ModifiedSqlGenerator.updateEntity(oldDistrictOfcInstallationDTO, DistrictOfcInstallationDTO.class, false, false);
			
		}
	}
	
	@Transactional(transactionType=util.TransactionType.READONLY)
	public double getOfcInstallationCostByDistrictID(long districtID) {
		double cost = 0;
		try{
			cost = districtOfcInstallationDAO.getDistrictOfcInstallationDTOByDistrictID(districtID)
					.stream()
					.findFirst()
					.orElseThrow(()->new RequestFailureException("No District OFC installation cost found with district id: " + districtID))
					.getInstallationCost();
		}catch (Exception e){
			logger.fatal(e.getMessage());
			logger.fatal("Returning Factor: " + 0.0);
		}
		return cost;
	}

	@Transactional(transactionType = TransactionType.READONLY)
	public double getOfcInstallationCostByPortId(long portId) {
		InventoryItem invItem = ServiceDAOFactory.getService(InventoryService.class)
				.getInventoryParentItemPathMapUptoRootByItemID(portId)
				.get(InventoryConstants.CATEGORY_ID_DISTRICT);
		double factor = 1.5;
		try{
			if(isDistrictAlreadyInCostTable(invItem.getID())) {
				factor = getOfcInstallationCostByDistrictID(invItem.getID());
			}
		}catch (Exception e) {
			logger.fatal(e.getMessage());
			e.printStackTrace();
		}
		return factor;
	}
}
