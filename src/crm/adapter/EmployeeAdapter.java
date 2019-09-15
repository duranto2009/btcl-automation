package crm.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import annotation.ColumnName;
import crm.CrmEmployeeNode;

public class EmployeeAdapter implements JsonSerializer<CrmEmployeeNode> {

	@Override
	public JsonElement serialize(CrmEmployeeNode crmEmployeeNode, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("crmEmployeeID", crmEmployeeNode.getCrmEmployeeID());
		jsonObject.addProperty("ID", crmEmployeeNode.getID());
		jsonObject.addProperty("parentID", crmEmployeeNode.getParentID());
		jsonObject.addProperty("inventoryCatagoryTypeID", crmEmployeeNode.getInventoryCatagoryTypeID());
		jsonObject.addProperty("name", crmEmployeeNode.getName());
		jsonObject.add("childEmployeeList", context.serialize(crmEmployeeNode.getChildCrmEmployeeNodeList()));
		jsonObject.addProperty("hasPermissionToChangeStatus", crmEmployeeNode.isHasPermissionToChangeStatus());
		jsonObject.addProperty("hasPermissionToPassStatusChangingPermission",
				crmEmployeeNode.isHasPermissionToPassStatusChangingPermission());
		jsonObject.addProperty("hasPermissionToForwardComplain", crmEmployeeNode.isHasPermissionToForwardComplain());
		jsonObject.addProperty("hasPermissionToPassComplainForwardingPermission",
				crmEmployeeNode.isHasPermissionToPassComplainForwardingPermission());
		jsonObject.addProperty("hasPermissionToPassComplain", crmEmployeeNode.isHasPermissionToPassComplain());
		jsonObject.addProperty("hasPermissionToPassComplainPassingPermission",
				crmEmployeeNode.isHasPermissionToPassComplainPassingPermission());
		jsonObject.addProperty("hasPermissionToAssignComplain", crmEmployeeNode.isHasPermissionToAssignComplain());
		jsonObject.addProperty("hasPermissionToPassComplainAssigningPermission",
				crmEmployeeNode.isHasPermissionToPassComplainAssigningPermission());
		
		jsonObject.addProperty("hasPermissionToAddComplain",crmEmployeeNode.isHasPermissionToAddComplain());
		jsonObject.addProperty("hasPermissionToPassAddComplainPermission",crmEmployeeNode.isHasPermissionToPassAddComplainPermission());
		jsonObject.addProperty("hasPermissionToChangePriority",crmEmployeeNode.isHasPermissionToChangePriority());
		jsonObject.addProperty("hasPermissionToPassChangePriorityPermission",crmEmployeeNode.isHasPermissionToPassChangePriorityPermission());
		jsonObject.addProperty("hasPermissionToPassComplainToOtherDepartment",crmEmployeeNode.isHasPermissionToPassComplainToOtherDepartment());
		jsonObject.addProperty("hasPermissionToPassPassComplainPermissionToOtherDepartment",crmEmployeeNode.isHasPermissionToPassPassComplainPermissionToOtherDepartment());
		
		jsonObject.addProperty("isNoc",crmEmployeeNode.isNOC());
		jsonObject.addProperty("departmentName",crmEmployeeNode.getDepartmentName());

		return jsonObject;
	}

}