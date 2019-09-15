
package crm.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import crm.CrmDesignationDTO;

public class DesignationDTOAdapter implements JsonSerializer<CrmDesignationDTO> {

	@Override
	public JsonElement serialize(CrmDesignationDTO crmDesignationDTO, Type arg1, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("designationTypeID", crmDesignationDTO.getDesignationID());
		jsonObject.addProperty("ID", crmDesignationDTO.getID());
		jsonObject.addProperty("parentCatagoryTypeID", crmDesignationDTO.getParentCatagoryTypeID());
		jsonObject.addProperty("name", crmDesignationDTO.getName());
		jsonObject.addProperty("hasPermissionToChangeStatus", crmDesignationDTO.isHasPermissionToChangeStatus());
		jsonObject.addProperty("hasPermissionToPassStatusChangingPermission",
				crmDesignationDTO.isHasPermissionToPassStatusChangingPermission());
		jsonObject.addProperty("hasPermissionToForwardComplain", crmDesignationDTO.isHasPermissionToForwardComplain());
		jsonObject.addProperty("hasPermissionToPassComplainForwardingPermission",
				crmDesignationDTO.isHasPermissionToPassComplainForwardingPermission());
		jsonObject.addProperty("hasPermissionToPassComplain", crmDesignationDTO.isHasPermissionToPassComplain());
		jsonObject.addProperty("hasPermissionToPassComplainPassingPermission",
				crmDesignationDTO.isHasPermissionToPassComplainPassingPermission());
		jsonObject.addProperty("hasPermissionToAssignComplain", crmDesignationDTO.isHasPermissionToAssignComplain());
		jsonObject.addProperty("hasPermissionToPassComplainAssigningPermission",
				crmDesignationDTO.isHasPermissionToPassComplainAssigningPermission());
		
		jsonObject.addProperty("hasPermissionToAddComplain",crmDesignationDTO.isHasPermissionToAddComplain());
		jsonObject.addProperty("hasPermissionToPassAddComplainPermission",crmDesignationDTO.isHasPermissionToPassAddComplainPermission());
		jsonObject.addProperty("hasPermissionToChangePriority",crmDesignationDTO.isHasPermissionToChangePriority());
		jsonObject.addProperty("hasPermissionToPassChangePriorityPermission",crmDesignationDTO.isHasPermissionToPassChangePriorityPermission());
		jsonObject.addProperty("hasPermissionToPassComplainToOtherDepartment",crmDesignationDTO.isHasPermissionToPassComplainToOtherDepartment());
		jsonObject.addProperty("hasPermissionToPassPassComplainPermissionToOtherDepartment",crmDesignationDTO.isHasPermissionToPassPassComplainPermissionToOtherDepartment());
		
		jsonObject.addProperty("isNoc",crmDesignationDTO.isNOC());
		jsonObject.addProperty("departmentName",crmDesignationDTO.getDepartmentName());

		return jsonObject;
	}

}