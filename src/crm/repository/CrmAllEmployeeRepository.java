package crm.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import common.StringUtils;
import crm.CrmEmployeeDTO;
import crm.inventory.CRMInventoryItem;
import util.ModifiedSqlGenerator;

public class CrmAllEmployeeRepository {

	Logger logger = Logger.getLogger(CrmAllEmployeeRepository.class);
	private static CrmAllEmployeeRepository instance = null;

	Map<Long, CrmEmployeeDTO> mapOfEmployeeDTOToEmployeeID;
	Map<Long, CrmEmployeeDTO> mapOfEmployeeDTOToCRMInventoryItemID;
	Map<Long, Set<CrmEmployeeDTO>> mapOfEmployeeDTOToUserID;
	Map<Long,Long> mapOfCRMInventoryItemIDToRootCRMInventoryItemID;
	
	Map<Long, CRMInventoryItem> mapOfCRMInventoryItemDTOToCRMInventoryItemID;
	Map<Long,CRMInventoryItem> mapOfRootCRMInventoryItemDTOToCRMInventoryItemID;
	Map<Long,Map<Long,CRMInventoryItem>> mapOfChildCRMInventoryItemDTOMapToCRMInventoryItemIDToParentCRMInventoryItemID;
	Map<Integer,Map<Long,CRMInventoryItem>> mapOfCRMInventoryItemToCRMInventoryItemIdMapToInventoryCategoryID;
	

	private CrmAllEmployeeRepository() {
		mapOfEmployeeDTOToEmployeeID = CrmEmployeeRepository.getInstance().mapOfPartialEmployeeDTOToEmployeeID;
		mapOfEmployeeDTOToCRMInventoryItemID = CrmEmployeeRepository.getInstance().mapOfPartialEmployeeDTOToInventoryItemID;
		mapOfEmployeeDTOToUserID = CrmEmployeeRepository.getInstance().mapOfPartialEmployeeDTOToUserID;
		mapOfCRMInventoryItemIDToRootCRMInventoryItemID = new ConcurrentHashMap<Long, Long>();
		
		mapOfEmployeeDTOToUserID = CrmEmployeeRepository.getInstance().mapOfEmployeeIDSetToUserID;
		
		mapOfCRMInventoryItemDTOToCRMInventoryItemID = CrmInventoryItemRepository.getInstance().mapOfInventoryItemDTOToInventoryItemID;
		mapOfRootCRMInventoryItemDTOToCRMInventoryItemID = CrmInventoryItemRepository.getInstance().mapOfRootInventoryItemDTOToInventoryItemID;
		mapOfChildCRMInventoryItemDTOMapToCRMInventoryItemIDToParentCRMInventoryItemID = CrmInventoryItemRepository.getInstance().mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID;
		mapOfCRMInventoryItemToCRMInventoryItemIdMapToInventoryCategoryID = CrmInventoryItemRepository.getInstance().mapOfInventoryItemToInventoryItemIdMapToInventoryCategoryID;
	}
	
	public boolean hasAnyEmployeeHavingCategoyID(int crmInventoryCategoryID){
		if(! mapOfCRMInventoryItemToCRMInventoryItemIdMapToInventoryCategoryID.containsKey(crmInventoryCategoryID)){
			return false;
		}
		for(CRMInventoryItem crmInventoryItem: mapOfCRMInventoryItemToCRMInventoryItemIdMapToInventoryCategoryID.get(crmInventoryCategoryID).values()){
			if(mapOfEmployeeDTOToCRMInventoryItemID.containsKey(crmInventoryItem.getID())){
				return true;
			}
		}
		return false;
	}
	public void reload (boolean isFirstReload){
		CrmEmployeeRepository.getInstance().reload(isFirstReload);
		CrmInventoryItemRepository.getInstance().reload(isFirstReload);
	}
	public List<CRMInventoryItem> getCrmInventoryItemDTOListByPartialName(String partialName){
		List<CRMInventoryItem> crmInventoryItems = new ArrayList<>();
		if(StringUtils.isBlank(partialName)){
			return new ArrayList<>(mapOfCRMInventoryItemDTOToCRMInventoryItemID.values());
		}
		partialName = StringUtils.toUpperCase(partialName);
		for(CRMInventoryItem crmInventoryItem: mapOfCRMInventoryItemDTOToCRMInventoryItemID.values()){
			if(StringUtils.toUpperCase(crmInventoryItem.getName()).contains(partialName)){
				crmInventoryItems.add(crmInventoryItem);
			}
		}
		return crmInventoryItems;
	}
	
	
	public CRMInventoryItem getCrmCRMInventoryItemDTOByInventoryID(long inventoryID){
		return mapOfCRMInventoryItemDTOToCRMInventoryItemID.get(inventoryID);
	}
	
	public List<CRMInventoryItem> getChildCRMInventoryItemListByInventoryID(long inventoryItemID){
		Map<Long,CRMInventoryItem> childCRMInventoryItemMap = mapOfChildCRMInventoryItemDTOMapToCRMInventoryItemIDToParentCRMInventoryItemID.get(inventoryItemID);
		List<CRMInventoryItem> inventoryItems = new ArrayList<>();
		if(childCRMInventoryItemMap!=null){
			inventoryItems.addAll(childCRMInventoryItemMap.values());
		}
		return inventoryItems;
	}
	
	public List<CrmEmployeeDTO> getChildEmployeeDTOListByEmployeeID(long employeeID){
		long inventoryItemID = getCrmEmployeeDTOByEmployeeID(employeeID).getID();
		Map<Long,CRMInventoryItem> childCRMInventoryItemMap = mapOfChildCRMInventoryItemDTOMapToCRMInventoryItemIDToParentCRMInventoryItemID.get(inventoryItemID);
		List<CRMInventoryItem> inventoryItems = new ArrayList<>();
		List<CrmEmployeeDTO> crmEmployeeDTOs = new ArrayList<>();
		if(childCRMInventoryItemMap!=null){
			inventoryItems.addAll(childCRMInventoryItemMap.values());
		
			for (CRMInventoryItem crmInventoryItem : inventoryItems) {
				
				CrmEmployeeDTO crmPartialEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByCRMInventoryItemID(crmInventoryItem.getID());
				if(crmPartialEmployeeDTO==null){
					continue;
				}
				
				ModifiedSqlGenerator.populateObjectFromOtherObject(crmInventoryItem, crmPartialEmployeeDTO, CRMInventoryItem.class);
				crmEmployeeDTOs.add(crmPartialEmployeeDTO);
			}
		}
		
		return crmEmployeeDTOs;
	}
	
	
	public List<CRMInventoryItem> getRootCRMInventoryItemList(){
		List<CRMInventoryItem> inventoryItems = new ArrayList<>();
		inventoryItems.addAll(mapOfRootCRMInventoryItemDTOToCRMInventoryItemID.values());
		return inventoryItems;
	}
	
	
	public List<CRMInventoryItem> getCRMInventoryItemListByInventoryCategoryID(long inventoryCategoryID){
		Map<Long, CRMInventoryItem> mapOfInventoryDTOToCRMInventoryItemID = mapOfCRMInventoryItemToCRMInventoryItemIdMapToInventoryCategoryID.get(inventoryCategoryID);
		List<CRMInventoryItem> inventoryItemList = new ArrayList<>();
		inventoryItemList.addAll(mapOfInventoryDTOToCRMInventoryItemID.values());
		return inventoryItemList;
	}
	
	public List<CrmEmployeeDTO> getCrmEmployeeDTOListByInventoryCategoryID(int inventoryCategoryID){
		Map<Long, CRMInventoryItem> mapOfInventoryDTOToCRMInventoryItemID = mapOfCRMInventoryItemToCRMInventoryItemIdMapToInventoryCategoryID.get(inventoryCategoryID);
		if(mapOfInventoryDTOToCRMInventoryItemID==null){
			return Collections.EMPTY_LIST;
		}
		List<CrmEmployeeDTO> crmEmployeeDTOList = new ArrayList<>();
		for(CRMInventoryItem crmInventoryItem: mapOfInventoryDTOToCRMInventoryItemID.values()){
			CrmEmployeeDTO crmEmployeeDTO = getCrmEmployeeDTOByCRMInventoryItemID(crmInventoryItem.getID());
			if(crmEmployeeDTO != null){
				crmEmployeeDTOList.add(crmEmployeeDTO);
			}
		}
		return crmEmployeeDTOList;
	}

	public CrmEmployeeDTO getCrmEmployeeDTOByEmployeeID(long employeeID) {
		if (!mapOfEmployeeDTOToEmployeeID.containsKey(employeeID)) {
			return null;
		}
		CrmEmployeeDTO crmEmployeeDTO = mapOfEmployeeDTOToEmployeeID.get(employeeID);
		
		if(!mapOfCRMInventoryItemDTOToCRMInventoryItemID.containsKey(crmEmployeeDTO.getID())){
			return null;
		}
		CRMInventoryItem inventoryItem = mapOfCRMInventoryItemDTOToCRMInventoryItemID.get(crmEmployeeDTO.getID());
		try {
			util.ModifiedSqlGenerator.populateObjectFromOtherObject(inventoryItem, crmEmployeeDTO, CRMInventoryItem.class);
		} catch (Exception ex) {
			logger.fatal("CrmAllEmployeeRepository populate action fail : ", ex);
		}
		return crmEmployeeDTO;
	}

	public CrmEmployeeDTO getCrmEmployeeDTOByCRMInventoryItemID(long inventoryItemID) {
		if (!mapOfEmployeeDTOToCRMInventoryItemID.containsKey(inventoryItemID)) {
			return null;
		}
		CrmEmployeeDTO crmEmployeeDTO = mapOfEmployeeDTOToCRMInventoryItemID.get(inventoryItemID);
		if(!mapOfCRMInventoryItemDTOToCRMInventoryItemID.containsKey(crmEmployeeDTO.getID())){
			return null;
		}
		CRMInventoryItem inventoryItem = mapOfCRMInventoryItemDTOToCRMInventoryItemID.get(inventoryItemID);
		try {
			util.ModifiedSqlGenerator.populateObjectFromOtherObject(inventoryItem, crmEmployeeDTO,
			 CRMInventoryItem.class);
		} catch (Exception ex) {
			logger.fatal("CrmAllEmployeeRepository populate action fail : ", ex);
		}
		return crmEmployeeDTO;
	}
	
	public List<CrmEmployeeDTO> getCrmEmployeeDTOListByUserID(long userID) {
		
		List<CrmEmployeeDTO> crmEmployeeDTOs = new ArrayList<>();
		
		if (!mapOfEmployeeDTOToUserID.containsKey(userID)) {
			return crmEmployeeDTOs;
		}
		Set<CrmEmployeeDTO> crmPartialEmployeeDTOs = mapOfEmployeeDTOToUserID.get(userID);
		
		for(CrmEmployeeDTO crmEmployeeDTO: crmPartialEmployeeDTOs){
			CRMInventoryItem inventoryItem = mapOfCRMInventoryItemDTOToCRMInventoryItemID.get(crmEmployeeDTO.getID());
			if (inventoryItem == null) {
				continue;
			}
			try {
				util.ModifiedSqlGenerator.populateObjectFromOtherObject(inventoryItem, crmEmployeeDTO,
				 CRMInventoryItem.class);
				crmEmployeeDTOs.add(crmEmployeeDTO);
			} catch (Exception ex) {
				logger.fatal("CrmAllEmployeeRepository populate action fail : ", ex);
			}
		}
		
		return crmEmployeeDTOs;
	}

	public Map<Long, CrmEmployeeDTO> getMapOfChildEmployeeToEmployeeID(long parentEmployeeID) {
		CrmEmployeeDTO parentCrmEmployeeDTO = getCrmEmployeeDTOByEmployeeID(parentEmployeeID);
		Map<Long, CRMInventoryItem> mapOfChildCRMInventoryItemDTOToCRMInventoryItemID = CrmInventoryItemRepository
				.getInstance().mapOfChildInventoryItemDTOMapToInventoryItemIDToParentInventoryItemID.get(parentCrmEmployeeDTO.getID());
		Map<Long, CrmEmployeeDTO> mapOfChildEmployeeDTOToEmployeeID = new ConcurrentHashMap<>();

		for (Map.Entry<Long, CRMInventoryItem> inventoryItemDTOByCRMInventoryItemID : mapOfChildCRMInventoryItemDTOToCRMInventoryItemID
				.entrySet()) {
			CrmEmployeeDTO crmEmployeeDTO = getCrmEmployeeDTOByCRMInventoryItemID(
					inventoryItemDTOByCRMInventoryItemID.getKey());
			mapOfChildEmployeeDTOToEmployeeID.put(crmEmployeeDTO.getCrmEmployeeID(), crmEmployeeDTO);

		}
		return mapOfChildEmployeeDTOToEmployeeID;
	}
	
	public List<CrmEmployeeDTO> getRootEmployeeList(){
		List<CRMInventoryItem> inventoryItemList = this.getRootCRMInventoryItemList();
		List<CrmEmployeeDTO> crmEmployeeDTOList = new ArrayList<>();
		for (CRMInventoryItem inventoryItem : inventoryItemList) {
			CrmEmployeeDTO crmEmployeeDTO = getCrmEmployeeDTOByCRMInventoryItemID(inventoryItem.getID());
			if(crmEmployeeDTO!=null){
				crmEmployeeDTOList.add(crmEmployeeDTO);	
			}
		}
		return crmEmployeeDTOList;
	}
	
	public CrmEmployeeDTO getRootEmployeeDTOByEmployeeID(long employeeID){
		compressPathnventoryItemIDToRootnventoryItemID();
		CrmEmployeeDTO childEmployeeDTO = this.getCrmEmployeeDTOByEmployeeID(employeeID);
		if(!mapOfCRMInventoryItemIDToRootCRMInventoryItemID.containsKey(childEmployeeDTO.getID())){
			return null;
		}
		CrmEmployeeDTO rootCrmEmployeeDTO = this.getCrmEmployeeDTOByCRMInventoryItemID(mapOfCRMInventoryItemIDToRootCRMInventoryItemID.get(childEmployeeDTO.getID()));
		return rootCrmEmployeeDTO;
	}
	
	
	private void compressPathnventoryItemIDToRootnventoryItemID(){
		for ( Map.Entry<Long, CrmEmployeeDTO> entry : mapOfEmployeeDTOToEmployeeID.entrySet()){
			Long key = entry.getKey();
			CrmEmployeeDTO crmEmployeeDTO = entry.getValue();
			CrmEmployeeDTO fullCrmEmployeeDTO = this.getCrmEmployeeDTOByEmployeeID(crmEmployeeDTO.getCrmEmployeeID());
			
			if(fullCrmEmployeeDTO != null){
				union(fullCrmEmployeeDTO.getID(), fullCrmEmployeeDTO.getParentID());
			}
		}	
	}
	private void union(Long inventoryItemID, Long parentCRMInventoryItemID) {
		Long parentOfinventoryItemID = find(inventoryItemID);
		if(parentCRMInventoryItemID != null){
			Long parentOfparentInventoryID = find(parentCRMInventoryItemID);
			
			if(parentOfinventoryItemID==parentOfparentInventoryID){
				System.out.println("They are already friends");
			}else{
				mapOfCRMInventoryItemIDToRootCRMInventoryItemID.put(parentOfinventoryItemID, parentOfparentInventoryID);
			}
		}else{
			mapOfCRMInventoryItemIDToRootCRMInventoryItemID.put(parentOfinventoryItemID, parentOfinventoryItemID);
		}
		
	}

	private Long find(Long inventoryItemID) {
		CRMInventoryItem inventoryItem = mapOfCRMInventoryItemDTOToCRMInventoryItemID.get(inventoryItemID);
		if(inventoryItem.getParentID() == null){
			return inventoryItemID;
		}
		mapOfCRMInventoryItemIDToRootCRMInventoryItemID.put(inventoryItemID, find(inventoryItem.getParentID()));
		return inventoryItemID;
	}

	public static synchronized CrmAllEmployeeRepository getInstance() {
		if (instance == null) {
			instance = new CrmAllEmployeeRepository();
		}
		return instance;
	}
}
