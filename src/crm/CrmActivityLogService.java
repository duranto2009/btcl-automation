package crm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import annotation.Transactional;
import crm.inventory.CRMInventoryItem;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmAllEmployeeRepository;
import crm.repository.CrmDepartmentRepository;
import login.LoginDTO;
import util.NavigationService;


public class CrmActivityLogService implements NavigationService {
	CrmActivityLogDAO crmActivityLogDAO = new CrmActivityLogDAO();
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
		return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects)
			throws Exception {
		return crmActivityLogDAO.getAllCrmActivityLogIDsBySearchCriteria(searchCriteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(transactionType=util.TransactionType.READONLY)
	public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
		return crmActivityLogDAO.getAllCrmActivityLogByIDs(recordIDs);
	}

	public List<CrmEmployeeWithDesignationAndDepartment> getAllCrmEmployeeByPartialName(String partialName) {
		List <CrmEmployeeWithDesignationAndDepartment> crmEmployeeDTOs = new ArrayList<>();
		List<CRMInventoryItem> crmInventoryItems = CrmAllEmployeeRepository.getInstance().getCrmInventoryItemDTOListByPartialName(partialName);
		for(CRMInventoryItem crmInventoryItem: crmInventoryItems){
			CrmEmployeeWithDesignationAndDepartment crmRawEmployee = getCrmEmployeeWithDesignationAndDepartment(crmInventoryItem);
			crmEmployeeDTOs.add(crmRawEmployee);
		}
		return crmEmployeeDTOs;
		
	}

	private CrmEmployeeWithDesignationAndDepartment getCrmEmployeeWithDesignationAndDepartment(CRMInventoryItem crmInventoryItem) {
		CrmEmployeeWithDesignationAndDepartment crmRawEmployee = new CrmEmployeeWithDesignationAndDepartment();
		CrmEmployeeDTO crmEmployeeDTO = CrmAllEmployeeRepository.getInstance().getCrmEmployeeDTOByCRMInventoryItemID(crmInventoryItem.getID());
		
		CrmDesignationDTO crmDesignationDTO = CrmAllDesignationRepository.getInstance().getCrmDesignationDTOByInventoryCategoryID(crmInventoryItem.getInventoryCatagoryTypeID());
		CrmDesignationDTO rootCrmDesignationDTO = CrmAllDesignationRepository.getInstance().getRootDesignationDTOByDesignationID(crmDesignationDTO.getDesignationID());
		CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentByRootDesignationID(rootCrmDesignationDTO.getDesignationID());
		
	
		crmRawEmployee.setEmployeeID(crmEmployeeDTO.getCrmEmployeeID());
		crmRawEmployee.setEmployeeName(crmInventoryItem.getName());
		crmRawEmployee.setDepartmentName(crmDepartmentDTO.getDepartmentName());
		crmRawEmployee.setDesignationName(crmDesignationDTO.getName());
	
	
		return crmRawEmployee;
	}

}
