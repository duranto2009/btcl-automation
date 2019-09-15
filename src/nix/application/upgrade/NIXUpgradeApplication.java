package nix.application.upgrade;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import com.google.gson.JsonObject;
import inventory.InventoryConstants;
import lombok.Data;

@Data
@TableName("nix_upgrade_application")
public class NIXUpgradeApplication {
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

    public NIXUpgradeApplication desirializer(JsonObject jsonObject) {

        NIXUpgradeApplication nixUpgradeApplication = new NIXUpgradeApplication();
        long old_port =(jsonObject.get("oldPort")==null)?-1:jsonObject.get("oldPort")
                .getAsJsonObject().get("ID").getAsLong();
        int newPortType = (jsonObject.get("newPortType")==null)?-1:jsonObject.get("newPortType")
                .getAsJsonObject().get("ID").getAsInt();
        long officeId = jsonObject.get("office").getAsJsonObject().get("ID").getAsLong();
        String existingPortType =jsonObject.get("existingPortType").getAsString();
        int oldPortType = InventoryConstants.mapOfStringToPortType.get(existingPortType);
        nixUpgradeApplication.setOldPortType(oldPortType);
        nixUpgradeApplication.setOldPortId(old_port);
        nixUpgradeApplication.setNewPortType(newPortType);
        nixUpgradeApplication.setOffice(officeId);
        return nixUpgradeApplication;
    }
}
