package crm.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import annotation.ForwardedAction;
import common.RequestFailureException;
import common.StringUtils;
import crm.CrmDepartmentDTO;
import crm.CrmDesignationNode;
import crm.CrmDesignationDTO;
import crm.adapter.DesignationDTOAdapter;
import crm.adapter.DesignationNodeAdapter;
import crm.repository.CrmAllDesignationRepository;
import crm.repository.CrmDepartmentRepository;
import crm.service.CrmDepartmentService;
import crm.service.CrmDesignationService;
import inventory.InventoryItem;
import login.LoginDTO;
import repository.Repository;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.RecordNavigationManager;

@ActionRequestMapping("CrmDesignation")
public class CrmDesignationAction extends AnnotatedRequestMappingAction {
	@Service
	CrmDesignationService crmDesignationService;
	@Service 
	CrmDepartmentService crmDepartmentService;
	@Override
	public GsonBuilder getGsonBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CrmDesignationNode.class, new DesignationNodeAdapter());
		gsonBuilder.registerTypeAdapter(CrmDesignationDTO.class, new DesignationDTOAdapter());
		gsonBuilder.registerTypeHierarchyAdapter(CrmDesignationDTO.class, new DesignationDTOAdapter());
		return gsonBuilder;
	}
	@RequestMapping(mapping = "/deleteDepartment",requestMethod = RequestMethod.POST)
	public void deleteDepartment(@RequestParameter("departmentID") long departmentID) throws Exception{
		crmDepartmentService.deleteDepartmentByDepartmentID(departmentID);
	}

	@RequestMapping(mapping = "/create_designation", requestMethod = RequestMethod.POST)
	public CrmDesignationDTO insertDesignation(CrmDesignationDTO crmDesignationDTO,
			@RequestParameter("departmentID") long departmentID) throws Exception {
		
		crmDesignationService.insert(crmDesignationDTO,departmentID);
		CrmAllDesignationRepository.getInstance().reload(false);
		return crmDesignationDTO;
	}

	@RequestMapping(mapping = "/get_child_designation_list", requestMethod = RequestMethod.GET)
	public List<CrmDesignationDTO> getChildDesignationDTOListByParentDesignationID(
			@RequestParameter("parentDesignationCategoryTypeID") Integer parentDesignationCategoryTypeID
			, @RequestParameter("departmentID") long departmentID) throws Exception {
		if(parentDesignationCategoryTypeID != null){
			CrmDesignationDTO parentDesignationDTO = CrmAllDesignationRepository
															.getInstance()
															.getCrmDesignationDTOByInventoryCategoryID(parentDesignationCategoryTypeID);
			return crmDesignationService.getChildDesignationDTOListByParentDesignationID(parentDesignationDTO.getDesignationID());
		}else{
			
			CrmDesignationDTO rootDesignationDTO = crmDesignationService
					.getRootDesignationDTOByDepartmentID(departmentID); 
			List<CrmDesignationDTO> crmDesignationDTOs = new ArrayList<>();
			if(rootDesignationDTO!=null){
				crmDesignationDTOs.add(rootDesignationDTO);
			}
			return crmDesignationDTOs;
		}
	}

	@RequestMapping(mapping = "/update_designation", requestMethod = RequestMethod.POST)
	public CrmDesignationDTO updateDesignation(CrmDesignationDTO crmDesignationDTO) throws Exception {
		crmDesignationService.update(crmDesignationDTO);
		return crmDesignationDTO;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(mapping = "/getDesignations", requestMethod = RequestMethod.GET)
	public List<CrmDesignationNode> getDesignations(LoginDTO loginDTO,@RequestParameter("departmentID") Long departmentID) throws Exception {
		
		if(departmentID==null){
			return crmDesignationService.getDesignationTreeRoots();
		}else{
			CrmDepartmentDTO crmDepartmentDTO = CrmDepartmentRepository.getInstance().getCrmDepartmentDTOByDepartmentID(departmentID);
			if(crmDepartmentDTO == null){
				return Collections.EMPTY_LIST;
			}
			Long rootDesignationID = crmDepartmentDTO.getRootDesignationID();
			if(rootDesignationID == null){
				return Collections.EMPTY_LIST;
			}
			CrmDesignationNode crmRootDesignationNode = crmDesignationService.getDesignationTreeNodeByDesignationID(rootDesignationID);
			List<CrmDesignationNode> crmDesignationNodes = new ArrayList<>();
			if(crmRootDesignationNode!= null){
				crmDesignationNodes.add(crmRootDesignationNode);
			}
			return crmDesignationNodes;
		}
	}

	@RequestMapping(mapping = "/getRrootDesignations", requestMethod = RequestMethod.GET)
	public List<CrmDesignationDTO> getRootDesignationDTOList() throws Exception {
		return crmDesignationService.getRootDesignationDTOs();
	}
	
	@RequestMapping(mapping = "/deleteDesignation", requestMethod = RequestMethod.POST)
	public long deleteDesignationByDesignationID(@RequestParameter("designationID") long designationID) throws Exception {
		crmDesignationService.deleteByDesignationID(designationID);
		return designationID;
	}
	@RequestMapping(mapping = "/getDesignation", requestMethod = RequestMethod.GET)
	public CrmDesignationDTO getDesignationDTOByDesignationID(@RequestParameter("crmInventoryCategoryTypeID") int crmInventoryCategoryID) throws Exception {
		return crmDesignationService.getCrmDesignationDTOByCrmInventoryCategoryID(crmInventoryCategoryID);
	}
	
	@ForwardedAction
	@RequestMapping(mapping = "/GetView", requestMethod = RequestMethod.GET)
	public String getDesignationPage() throws Exception {
		return "DesignationConfigurationView";
	}
	
	@ForwardedAction
	@RequestMapping(mapping = "/department/getAddDepartment", requestMethod = RequestMethod.GET)
	public String getAddDepartment(){
		return "getAddDepartment";
		
	}
	
	@RequestMapping(mapping = "/department/autoComplete", requestMethod = RequestMethod.POST)
	public List<InventoryItem> autoComplete(@RequestParameter("name") String partialName, 
			@RequestParameter("categoryType") int categoryType, @RequestParameter("parentID") Long parentID) throws Exception{
		return crmDepartmentService.getAreaNameByPartialMatch(StringUtils.trim(partialName), parentID, categoryType);
	}
	@RequestMapping(mapping="/department/autocompleteDept", requestMethod=RequestMethod.POST)
	public List<CrmDepartmentDTO> getDepartmentDTOsByPartialName(@RequestParameter("deptName") String partialName,
								@RequestParameter("districtID") Long districtID, 
								@RequestParameter("upazilaID") Long upazilaID,
								@RequestParameter("unionID") Long unionID)
		throws Exception{
		return crmDepartmentService.getCrmDepartmentDTOsByPartialDeptName(StringUtils.trim(partialName), districtID, upazilaID, unionID);
	}
	@RequestMapping(mapping = "/department/addDepartment", requestMethod = RequestMethod.POST)
	public long addDepartment(CrmDepartmentDTO crmDepartmentDTO) throws Exception{
		crmDepartmentService.insertCrmDepartmentDTO(crmDepartmentDTO);
		return crmDepartmentDTO.getID();
	}
	
	@RequestMapping(mapping = "/department/editDepartment", requestMethod= RequestMethod.POST)
	public void editDepartment(CrmDepartmentDTO crmDepartmentDTO)throws Exception {
		crmDepartmentService.updateCrmDepartmentDTO(crmDepartmentDTO);
	}
	@ForwardedAction
	@RequestMapping (mapping="/department/getSearchDepartment", requestMethod = RequestMethod.All)
	public String getSearchSpacePage(HttpServletRequest request) throws Exception{
		LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        RecordNavigationManager rnManager = new RecordNavigationManager(
        			SessionConstants.NAV_CRM_DEPT, request, crmDepartmentService,
        			SessionConstants.VIEW_CRM_DEPT,
        			SessionConstants.SEARCH_CRM_DEPT
        );
        
        rnManager.doJob(loginDTO);
        return "getSearchDepartment";
	}
	@RequestMapping (mapping = "/department/deleteDepartment", requestMethod = RequestMethod.POST)
	public void deleteDepartment(@RequestParameter("deleteIDs") List<Long>departmentIDs)throws Exception{
		crmDepartmentService.deleteDepartmentByDepartmentIDList(departmentIDs);
	}
	@RequestMapping (mapping = "/department/copyOrganogram", requestMethod = RequestMethod.POST)
	public void copyOrganogramOfOtherDepartment( @RequestParameter("srcDeptID") long srcDepartmentID, @RequestParameter("desDeptID") long desDepartmentID) throws Exception{
		crmDesignationService.copyDesignationFromOtherDepartment(srcDepartmentID, desDepartmentID);
	}
	@RequestMapping (mapping = "/repository", requestMethod = RequestMethod.All)
	public void reloadRepository( @RequestParameter("repoName") String repositoryName) throws Exception{
		Class<?> classObject = Class.forName(repositoryName);
		Method method = classObject.getDeclaredMethod("getInstance");
		Repository  repository = (Repository)method.invoke(null);
		repository.reload(true);
	}
	
}
