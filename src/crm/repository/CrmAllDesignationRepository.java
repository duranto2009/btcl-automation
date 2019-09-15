package crm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import common.RequestFailureException;
import crm.CrmDesignationDTO;
import crm.CrmEmployeeDTO;
import crm.inventory.CRMInventoryCatagoryType;
import util.ModifiedSqlGenerator;

public class CrmAllDesignationRepository {

	Logger logger = Logger.getLogger(CrmAllDesignationRepository.class);
	private static CrmAllDesignationRepository instance = null;

	Map<Long, CrmDesignationDTO> mapOfDesignationDTOToDesignationID;
	Map<Integer, CrmDesignationDTO> mapOfDesignationDTOToInventoryCategoryID;
	Map<Integer,Integer> mapOfInventoryCategoryIDToRootInventoryCategoryID;

	Map<Integer, CRMInventoryCatagoryType> mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID;
	Map<Integer, CRMInventoryCatagoryType> mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID;
	Map<Integer, Map<Integer, CRMInventoryCatagoryType>> mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID;

	private CrmAllDesignationRepository() {
		mapOfDesignationDTOToDesignationID = CrmDesignationRepository
				.getInstance().mapOfPartialCrmDesignationDTOToDesignationID;
		mapOfDesignationDTOToInventoryCategoryID = CrmDesignationRepository
				.getInstance().mapOfPartialCrmDesignationDTOToInventoryCategoryID;
		mapOfInventoryCategoryIDToRootInventoryCategoryID = new ConcurrentHashMap<>();
		

		mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID = CrmInventoryCategoryRepository
				.getInstance().mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID;
		mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID = CrmInventoryCategoryRepository
				.getInstance().mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID;
		mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID = CrmInventoryCategoryRepository
				.getInstance().mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID;

	}
	
	public void reload(boolean isFirstReload){
		CrmInventoryCategoryRepository.getInstance().reload(isFirstReload);
		CrmDesignationRepository.getInstance().reload(isFirstReload);
	}
	
	public CRMInventoryCatagoryType getCrmInventoryCategoryTypeDTOByInventoryCategoryID(Integer inventoryCategoryID) {
		return mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.get(inventoryCategoryID);
	}
	
	public List<CRMInventoryCatagoryType> getRootInventoryCategoryTypeList() {
		List<CRMInventoryCatagoryType> inventoryCategoryTypes = new ArrayList<>();
		inventoryCategoryTypes.addAll(mapOfRootInventoryCategoryTypeDTOToInventoryCategoryTypeID.values());
		return inventoryCategoryTypes;
	}

	public CrmDesignationDTO getCrmDesignationDTOByDesignationID(long designationID) {
		if (!mapOfDesignationDTOToDesignationID.containsKey(designationID)) {
			return null;
		}
		CrmDesignationDTO crmDesignationDTO = mapOfDesignationDTOToDesignationID.get(designationID);
		
		if(!mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.containsKey(crmDesignationDTO.getID())){
			return null;
		}
		CRMInventoryCatagoryType inventoryCatagoryType = mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.get(crmDesignationDTO.getID());
		
		try {
			util.ModifiedSqlGenerator.populateObjectFromOtherObject(inventoryCatagoryType, crmDesignationDTO,
					CRMInventoryCatagoryType.class);
		} catch (Exception ex) {
			logger.fatal("CrmAllDesignationRepository populate action fail : ", ex);
		}
		return crmDesignationDTO;
	}

	public CrmDesignationDTO getCrmDesignationDTOByInventoryCategoryID(int inventoryCategoryID) {
		if (!mapOfDesignationDTOToInventoryCategoryID.containsKey(inventoryCategoryID)) {
			return null;
		}
		CrmDesignationDTO crmDesignationDTO = mapOfDesignationDTOToInventoryCategoryID.get(inventoryCategoryID);

		if(!mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.containsKey(crmDesignationDTO.getID())){
			return null;
		}
		CRMInventoryCatagoryType inventoryCatagoryType = mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.get(crmDesignationDTO.getID());

		try {
			util.ModifiedSqlGenerator.populateObjectFromOtherObject(inventoryCatagoryType, crmDesignationDTO,
					CRMInventoryCatagoryType.class);
		} catch (Exception ex) {
			logger.fatal("CrmAllDesignationRepository populate action fail : ", ex);
		}
		return crmDesignationDTO;
	}
	
	public List<CrmDesignationDTO> getCrmDesignationChildListByParentDesignationID(long crmDesignationID){
		
		
		CrmDesignationDTO parentDesignationDTO = getCrmDesignationDTOByDesignationID(crmDesignationID);
		if(parentDesignationDTO == null){
			throw new RequestFailureException("No crm designation found with ID "+crmDesignationID);
		}
		
		if(parentDesignationDTO.getID() == null){
			return Collections.emptyList();
		}
		
		Map<Integer,CRMInventoryCatagoryType> mapOfInventoryCategoryTypeToCategoryTypeID =  mapOfChildInventoryCategoryTypeDTOMapToInventoryCategoryTypeIDToParentInventoryCategoryTypeID.get(parentDesignationDTO.getID());
		if(mapOfInventoryCategoryTypeToCategoryTypeID == null){
			return Collections.emptyList();
		}
		List<CrmDesignationDTO> childDesignationDTOs = new ArrayList<>();
		
		for(CRMInventoryCatagoryType crmInventoryCatagoryType: mapOfInventoryCategoryTypeToCategoryTypeID.values()){
			CrmDesignationDTO crmDesignationDTO = mapOfDesignationDTOToInventoryCategoryID
					.get(crmInventoryCatagoryType.getID());
			if(crmDesignationDTO == null){
				continue;
			}
			ModifiedSqlGenerator.populateObjectFromOtherObject(crmInventoryCatagoryType, crmDesignationDTO, CRMInventoryCatagoryType.class);
			childDesignationDTOs.add(crmDesignationDTO);
		}
		
		return childDesignationDTOs;
	}

	public List<CrmDesignationDTO> getRootDesignationList() {
		List<CRMInventoryCatagoryType> inventoryCatagoryTypeList = this.getRootInventoryCategoryTypeList();
		List<CrmDesignationDTO> crmDesignationDTOList = new ArrayList<>();
		for (CRMInventoryCatagoryType inventoryCategory : inventoryCatagoryTypeList) {
			crmDesignationDTOList.add(getCrmDesignationDTOByInventoryCategoryID(inventoryCategory.getID()));
		}
		return crmDesignationDTOList;
	}
	
	public CrmDesignationDTO getRootDesignationDTOByDesignationID(long designationID){
		compressPathnventoryCategoryIDToRootnventoryCategoryID();
		CrmDesignationDTO childDesignationDTO = this.getCrmDesignationDTOByDesignationID(designationID);
		if(!mapOfInventoryCategoryIDToRootInventoryCategoryID.containsKey(childDesignationDTO.getID())){
			return null;
		}
		CrmDesignationDTO rootDesignationDTO = this.getCrmDesignationDTOByInventoryCategoryID(mapOfInventoryCategoryIDToRootInventoryCategoryID.get(childDesignationDTO.getID()));
		return rootDesignationDTO;
	}
	
	
	private void compressPathnventoryCategoryIDToRootnventoryCategoryID(){
		for ( Map.Entry<Long, CrmDesignationDTO> entry : mapOfDesignationDTOToDesignationID.entrySet()){
			Long key = entry.getKey();
			CrmDesignationDTO crmDesignationDTO = entry.getValue();
			CrmDesignationDTO fullcrmDesignationDTO = this.getCrmDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
			
			if(fullcrmDesignationDTO != null){
				union(fullcrmDesignationDTO.getID(), fullcrmDesignationDTO.getParentCatagoryTypeID());
			}
		}	
	}
	private void union(Integer inventoryCategoryID, Integer parentInventoryCategoryID) {
		if(parentInventoryCategoryID == null){
			mapOfInventoryCategoryIDToRootInventoryCategoryID.put(inventoryCategoryID, inventoryCategoryID);
			return;
		}
		Integer parentOfinventoryCategoryID = find(inventoryCategoryID);
		if(parentOfinventoryCategoryID == null){
			return;
		}
		Integer parentOfparentInventoryCategoryID = find(parentInventoryCategoryID);
		if(parentOfparentInventoryCategoryID == null){
			return;
		}
		if(parentOfinventoryCategoryID!=parentOfparentInventoryCategoryID){
		
			mapOfInventoryCategoryIDToRootInventoryCategoryID.put(parentOfinventoryCategoryID, parentOfparentInventoryCategoryID);
		}	
	}

	private Integer find(Integer inventoryCategoryID) {
		CRMInventoryCatagoryType inventoryCategoryType = mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.get(inventoryCategoryID);
		if(inventoryCategoryType == null){
			return null;
		}
		while(inventoryCategoryType.getParentCatagoryTypeID() != null){
			mapOfInventoryCategoryIDToRootInventoryCategoryID.put(inventoryCategoryID,inventoryCategoryType.getParentCatagoryTypeID());
			inventoryCategoryType = mapOfInventoryCategoryTypeDTOToInventoryCategoryTypeID.get(inventoryCategoryType.getParentCatagoryTypeID());
			if(inventoryCategoryType == null){
				return null;
			}
		}
		//
		return inventoryCategoryType.getID();
	}

	public static synchronized CrmAllDesignationRepository getInstance() {
		if (instance == null) {
			instance = new CrmAllDesignationRepository();
		}
		return instance;
	}
}
