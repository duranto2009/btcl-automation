package crm.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import crm.CrmEmployeeDTO;
import crm.CrmEmployeeNode;

/**
 * @author Alam
 * 
 */
public class EmployeeDTOAdapter implements JsonSerializer<CrmEmployeeDTO> {
	@Override
	public JsonElement serialize(CrmEmployeeDTO crmEmployeeDTO, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("crmEmployeeID", crmEmployeeDTO.getCrmEmployeeID());
		jsonObject.addProperty("ID", crmEmployeeDTO.getID());
		jsonObject.addProperty("parentID", crmEmployeeDTO.getParentID());
		jsonObject.addProperty("inventoryCatagoryTypeID", crmEmployeeDTO.getInventoryCatagoryTypeID());
		jsonObject.addProperty("name", crmEmployeeDTO.getName());
		
		if(crmEmployeeDTO instanceof CrmEmployeeNode){
			CrmEmployeeNode crmEmployeeNode = (CrmEmployeeNode)crmEmployeeDTO;
			jsonObject.add("childEmployeeList", context.serialize(crmEmployeeNode.getChildCrmEmployeeNodeList()));
		}
		
		jsonObject.addProperty("userID", crmEmployeeDTO.getUserID() );
		jsonObject.addProperty("hasPermissionToChangeStatus", crmEmployeeDTO.isHasPermissionToChangeStatus());
		jsonObject.addProperty("hasPermissionToPassStatusChangingPermission",
				crmEmployeeDTO.isHasPermissionToPassStatusChangingPermission());
		jsonObject.addProperty("hasPermissionToForwardComplain", crmEmployeeDTO.isHasPermissionToForwardComplain());
		jsonObject.addProperty("hasPermissionToPassComplainForwardingPermission",
				crmEmployeeDTO.isHasPermissionToPassComplainForwardingPermission());
		jsonObject.addProperty("hasPermissionToPassComplain", crmEmployeeDTO.isHasPermissionToPassComplain());
		jsonObject.addProperty("hasPermissionToPassComplainPassingPermission",
				crmEmployeeDTO.isHasPermissionToPassComplainPassingPermission());
		jsonObject.addProperty("hasPermissionToAssignComplain", crmEmployeeDTO.isHasPermissionToAssignComplain());
		jsonObject.addProperty("hasPermissionToPassComplainAssigningPermission",
				crmEmployeeDTO.isHasPermissionToPassComplainAssigningPermission());
		
		jsonObject.addProperty("hasPermissionToAddComplain",crmEmployeeDTO.isHasPermissionToAddComplain());
		jsonObject.addProperty("hasPermissionToPassAddComplainPermission",crmEmployeeDTO.isHasPermissionToPassAddComplainPermission());
		jsonObject.addProperty("hasPermissionToChangePriority",crmEmployeeDTO.isHasPermissionToChangePriority());
		jsonObject.addProperty("hasPermissionToPassChangePriorityPermission",crmEmployeeDTO.isHasPermissionToPassChangePriorityPermission());
		jsonObject.addProperty("hasPermissionToPassComplainToOtherDepartment",crmEmployeeDTO.isHasPermissionToPassComplainToOtherDepartment());
		jsonObject.addProperty("hasPermissionToPassPassComplainPermissionToOtherDepartment",crmEmployeeDTO.isHasPermissionToPassPassComplainPermissionToOtherDepartment());
		jsonObject.addProperty("isNoc",crmEmployeeDTO.isNOC());
		jsonObject.addProperty("departmentName",crmEmployeeDTO.getDepartmentName());
		
		return jsonObject;
	}
}
