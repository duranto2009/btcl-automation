package nix.nixportconfig;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("nix_port_charge_conf")
public class NIXPortConfig{
    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("port_charge")
    int portCharge;
    @ColumnName("port_type")
    int portType;
    @ColumnName("registration_charge")
    int registrationCharge;
    @ColumnName("upgrade_charge")
    int upgradeCharge;
    @ColumnName("downgrade_charge")
    int downgradeCharge;
    @ColumnName("close_charge")
    int closeCharge;
}
