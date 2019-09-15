package nix.application.downgrade;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import com.google.gson.JsonObject;
import inventory.InventoryConstants;
import lombok.Data;

@Data
@TableName("nix_downgrade_application")
public class NIXDowngradeApplication {

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("parent_application")
    long parent;
    @ColumnName("old_port_type")
    int oldPortType;
    @ColumnName("new_port_type")
    int newPortType;
    @ColumnName("old_port_id")
    long oldPortId;
    @ColumnName("new_port_id")
    long newPortId;
    @ColumnName("office_id")
    long office;



    public NIXDowngradeApplication desirializer(JsonObject jsonObject) {
        NIXDowngradeApplication nixDowngradeApplication = new NIXDowngradeApplication();
        long old_port =(jsonObject.get("oldPort")==null)?-1:jsonObject.get("oldPort").getAsJsonObject().get("ID").getAsLong();
        int newPortType = (jsonObject.get("newPortType")==null)?-1:jsonObject.get("newPortType").getAsJsonObject().get("ID").getAsInt();
        long officeId = jsonObject.get("office").getAsJsonObject().get("ID").getAsLong();

        String existingPortType =jsonObject.get("existingPortType").getAsString();
        int oldPortType = InventoryConstants.mapOfStringToPortType.get(existingPortType);
        nixDowngradeApplication.setOldPortType(oldPortType);
        nixDowngradeApplication.setOldPortId(old_port);
        nixDowngradeApplication.setNewPortType(newPortType);
        nixDowngradeApplication.setOffice(officeId);
        return nixDowngradeApplication;
    }
}
