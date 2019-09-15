package crm.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.GsonBuilder;

import annotation.ForwardedAction;
import common.RequestFailureException;
import common.RequestFailureExceptionForwardToSamePage;
import common.StringUtils;
import crm.CrmDepartmentDTO;
import crm.CrmEmployeeDTO;
import crm.CrmEmployeeDesignationValuePair;
import crm.CrmEmployeeNode;
import crm.adapter.EmployeeAdapter;
import crm.adapter.EmployeeDTOAdapter;
import crm.inventory.CRMInventoryItem;
import crm.repository.CrmAllEmployeeRepository;
import crm.repository.CrmDepartmentRepository;
import crm.service.CrmDepartmentService;
import crm.service.CrmEmployeeService;
import login.LoginDTO;
import net.sf.jasperreports.olap.mapping.Mapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import user.UserDTO;
import user.UserRepository;
import util.RecordNavigationManager;

@ActionRequestMapping("CrmEmployee")
public class CrmEmployeeAction extends AnnotatedRequestMappingAction{
	
	@Service
	CrmEmployeeService crmEmployeeService ;
	@Service 
	CrmDepartmentService crmDepartmentService;
	@Override
	public GsonBuilder getGsonBuilder(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(CrmEmployeeDTO.class, new EmployeeDTOAdapter());
		gsonBuilder.registerTypeAdapter(CrmEmployeeDTO.class, new EmployeeDTOAdapter());
		return gsonBuilder;
	}
	@RequestMapping(mapping="/createEmployee",requestMethod= RequestMethod.POST)
	public CrmEmployeeDTO insertEmployee(CrmEmployeeDTO crmEmployeeDTO,@RequestParameter("departmentID") long departmentID) throws Exception {
		// check parrent
		// if not crm admin throw exception
		crmEmployeeService.insertEmployee(crmEmployeeDTO,departmentID);
		CrmAllEmployeeRepository.getInstance().reload(false);
		return crmEmployeeDTO;
	}
	
	@RequestMapping(mapping="/updateEmployee",requestMethod= RequestMethod.POST)
	public CrmEmployeeDTO updateEmployee(CrmEmployeeDTO crmEmployeeDTO, LoginDTO loginDTO) throws Exception {
		// if (hasCrmAdminMenuPermission ) throw new Exception;
		crmEmployeeService.updateEmployee(crmEmployeeDTO);
		return crmEmployeeDTO;
	}
	
	@RequestMapping(mapping="/deleteEmployee",requestMethod= RequestMethod.POST)
	public long deleteEmployee(@RequestParameter("employeeID") long employeeID) throws Exception {
		crmEmployeeService.deleteEmployeeByID(employeeID);
		return employeeID;
	}
	
	@RequestMapping(mapping="/GetView",requestMethod= RequestMethod.GET)
	public ActionForward getEmployeePage(ActionMapping mapping) throws Exception {
		
		return mapping.findForward("EmployeeView");
	}
	
	@ForwardedAction
	@RequestMapping(mapping="/getDepartmentView", requestMethod = RequestMethod.GET)
	public String getDepartmentPageByDepartmentID( @RequestParameter("departmentID") long deptID,
			HttpServletRequest request) throws Exception{
		List<CrmDepartmentDTO> crmDepartmentDTOs = (List<CrmDepartmentDTO>) crmDepartmentService.getDTOs(Arrays.asList(deptID));
		if(crmDepartmentDTOs.isEmpty()){
			throw new RequestFailureException("No department found with id " + deptID);
		}
		request.setAttribute("department", crmDepartmentDTOs.get(0));
		return "employeeView";
	}
	@RequestMapping(mapping="/getEmployees",requestMethod= RequestMethod.GET)
	public List<CrmEmployeeNode> getEmployees(LoginDTO loginDTO) throws Exception {
		/*ajax*/
		return crmEmployeeService.getEmployeeNodeByUserID(loginDTO.getUserID());
	}
	
	@RequestMapping(mapping="/getAllEmployeeRoots", requestMethod = RequestMethod.GET )
	public List<CrmEmployeeNode> getAllEmployeeRoots(LoginDTO loginDTO, @RequestParameter("departmentID") Long departmentID ) throws Exception {
		
		if(departmentID==null){
			return crmEmployeeService.getAllRootEmployeeNodeWithDescendantEmployeeList();
		}else{
			return crmEmployeeService.getCrmEmplyeeNodeListByDepartmentID(departmentID);
		}
	}
	
	@RequestMapping(mapping="/GetDescendantEmployeesByPartialName",requestMethod= RequestMethod.GET)
	public List<CrmEmployeeDesignationValuePair> getDescendantEmployeeListByPartialName(@RequestParameter("complainResolverName") String partialEmployeeName
								,@RequestParameter("departmentID") Long departmentID, LoginDTO loginDTO) throws Exception {
		/*ajax*/
		if(!loginDTO.getIsAdmin()){
			throw new RequestFailureException("Invalid Request");
		}
		return crmEmployeeService.getDescendantEmployeeListByPartialName(partialEmployeeName,departmentID, loginDTO);
	}
	
	@RequestMapping(mapping="/GetDescendantEmployeesByPartialNameAndEmployeeID",requestMethod= RequestMethod.GET)
	public List<CrmEmployeeDesignationValuePair> getDescendantEmployeeListByPartialNameAndEmployeeID(
			@RequestParameter("resolverPartialName") String resolverPartialName
			,@RequestParameter("complainID") long complainID
			,@RequestParameter("departmentID") Long departmentID
			,LoginDTO loginDTO) throws Exception {
		/*ajax*/
		return crmEmployeeService.getDesignationEmloyeeListByPartialNameAndCrmComplainID(resolverPartialName
				, complainID, departmentID,loginDTO);
	}
	
	@RequestMapping(mapping="/GetEmployeesByPartialName",requestMethod= RequestMethod.GET)
	public List<CrmEmployeeDesignationValuePair> getEmployeeListByPartialNameAndEmployeeID(@RequestParameter("asignee") String partialName,
			@RequestParameter("deptID") long deptID) throws Exception {
		
		return crmEmployeeService.getDesignationEmloyeeListByPartialName(partialName, deptID);
	}
	@RequestMapping(mapping="/getEmployeeByID", requestMethod = RequestMethod.GET )
	public CrmEmployeeDTO getEmployeeByID( @RequestParameter("crmEmployeeID") long crmEmployeeID ) throws Exception{
		
		CrmEmployeeDTO crmEmployeeDTO = crmEmployeeService.getEmployeeDTOByEmployeeID( crmEmployeeID );
		return crmEmployeeDTO;
	}
	@RequestMapping(mapping = "/GetUserByPartialName", requestMethod = RequestMethod.GET)
	public List<UserDTO> getUserDTOListByPartialNameMatch(@RequestParameter("username") String userName) throws Exception{
		List<UserDTO> userList = UserRepository.getInstance().getUserList();
		List<UserDTO> matchingUserList = new ArrayList<>();
		
		for(UserDTO userDTO: userList){
			if( StringUtils.isBlank( userName ) || userDTO.getUsername().contains( userName ) ){
				matchingUserList.add(userDTO);
			}
		}
		
		return matchingUserList;
	}
	@ForwardedAction
	@RequestMapping(mapping="/getEmployeeView", requestMethod=RequestMethod.GET)
	public String getEmployeeViewPage(HttpServletRequest request, @RequestParameter("employeeID")long employeeID) throws Exception{
		CrmEmployeeDTO crmEmployeeDTO = crmEmployeeService.getEmployeeDTOByEmployeeID(employeeID);
		request.setAttribute("employee", crmEmployeeDTO);
		return "getEmployeeView";
	}
	
	@RequestMapping( mapping="/GetUsernameFromID", requestMethod = RequestMethod.GET )
	public String getUsernameFromID( @RequestParameter("id") long id ){
		UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(id); 
		if(userDTO == null){
			throw new RequestFailureException("No user found with ID "+id);
		}
		return userDTO.getUsername();
	}
	@ForwardedAction
	@RequestMapping(mapping = "/searchEmployee", requestMethod = RequestMethod.All)
	public String getSearchEmployeePage(HttpServletRequest request,LoginDTO loginDTO) throws Exception{
        RecordNavigationManager rnManager = new RecordNavigationManager(
        			SessionConstants.NAV_CRM_EMPLOYEE, request, crmEmployeeService,
        			SessionConstants.VIEW_CRM_EMPLOYEE,
        			SessionConstants.SEARCH_CRM_EMPLOYEE
        );
        
        rnManager.doJob(loginDTO);
        return "getSearchCrmEmployee";
	}

}
