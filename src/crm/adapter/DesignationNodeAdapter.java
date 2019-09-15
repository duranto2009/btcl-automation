package crm.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import crm.CrmDesignationNode;

public class DesignationNodeAdapter implements JsonSerializer<CrmDesignationNode> {

	@Override
	public JsonElement serialize(CrmDesignationNode crmDesignationNode, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("designationTypeID", crmDesignationNode.getDesignationID());
		jsonObject.addProperty("ID", crmDesignationNode.getID());
		jsonObject.addProperty("parentCatagoryTypeID", crmDesignationNode.getParentCatagoryTypeID());
		jsonObject.addProperty("name", crmDesignationNode.getName());
		jsonObject.add("childEmployeeList", context.serialize(crmDesignationNode.getChildCrmDesignationNodeList()));
		jsonObject.addProperty("hasPermissionToChangeStatus", crmDesignationNode.isHasPermissionToChangeStatus());
		jsonObject.addProperty("hasPermissionToPassStatusChangingPermission",
				crmDesignationNode.isHasPermissionToPassStatusChangingPermission());
		jsonObject.addProperty("hasPermissionToForwardComplain", crmDesignationNode.isHasPermissionToForwardComplain());
		jsonObject.addProperty("hasPermissionToPassComplainForwardingPermission",
				crmDesignationNode.isHasPermissionToPassComplainForwardingPermission());
		jsonObject.addProperty("hasPermissionToPassComplain", crmDesignationNode.isHasPermissionToPassComplain());
		jsonObject.addProperty("hasPermissionToPassComplainPassingPermission",
				crmDesignationNode.isHasPermissionToPassComplainPassingPermission());
		jsonObject.addProperty("hasPermissionToAssignComplain", crmDesignationNode.isHasPermissionToAssignComplain());
		jsonObject.addProperty("hasPermissionToPassComplainAssigningPermission",
				crmDesignationNode.isHasPermissionToPassComplainAssigningPermission());
		
		jsonObject.addProperty("hasPermissionToAddComplain",crmDesignationNode.isHasPermissionToAddComplain());
		jsonObject.addProperty("hasPermissionToPassAddComplainPermission",crmDesignationNode.isHasPermissionToPassAddComplainPermission());
		jsonObject.addProperty("hasPermissionToChangePriority",crmDesignationNode.isHasPermissionToChangePriority());
		jsonObject.addProperty("hasPermissionToPassChangePriorityPermission",crmDesignationNode.isHasPermissionToPassChangePriorityPermission());
		jsonObject.addProperty("hasPermissionToPassComplainToOtherDepartment",crmDesignationNode.isHasPermissionToPassComplainToOtherDepartment());
		jsonObject.addProperty("hasPermissionToPassPassComplainPermissionToOtherDepartment",crmDesignationNode.isHasPermissionToPassPassComplainPermissionToOtherDepartment());
		
		jsonObject.addProperty("isNoc",crmDesignationNode.isNOC());
		jsonObject.addProperty("departmentName",crmDesignationNode.getDepartmentName());

		return jsonObject;
	}

}
