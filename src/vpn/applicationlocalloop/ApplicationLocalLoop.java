package vpn.applicationlocalloop;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@TableName("vpn_application_local_loop")
public class ApplicationLocalLoop {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationLocalLoop.class);

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("office_id")
    long officeId;

    @ColumnName("ofc_type")
    int ofcType;

    @ColumnName("pop_id")
    long popId;

    @ColumnName("vlan_id")
    long vlanId;

    @ColumnName("router_switch_id")
    long routerSwitchId;

    @ColumnName("port_id")
    long portId;

    @ColumnName("port_type")
    int portType;

    @ColumnName("client_distance")
    long clientDistance;

    @ColumnName("btcl_distance")
    long btclDistance;

    @ColumnName("ocd_distance")
    long ocdDistance;

    @ColumnName("vendor_id")
    long vendor;
    @ColumnName("application")
    long application;

    @ColumnName("loop_provider")
    int loopProvider;

    @ColumnName("terminal_device_provider")
    int terminalDeviceProvider;

    String popName;

    public List<ApplicationLocalLoop> deserialize_custom(JsonElement jsonElements) {
        List<ApplicationLocalLoop> lists = new ArrayList<>();
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        JsonArray jsonArray=jsonObject.getAsJsonArray("localloops");
        if(jsonArray!=null)for (JsonElement jsonElement:jsonArray)
        {
            ApplicationLocalLoop localLoop=new ApplicationLocalLoop();
            JsonObject object=jsonElement.getAsJsonObject();
            JsonElement ID= object.get("id");
            localLoop.setId(ID != null ? ID.getAsLong() : 0);
            JsonElement popID= object.get("popID")!=null?object.get("popID"):object.get("popId");
            localLoop.setPopId(popID != null ? popID.getAsLong() : 0);
            localLoop.setOfficeId(object.get("officeId").getAsLong());
            JsonElement router_switchID=object.get("router_switchID");
            localLoop.setRouterSwitchId(router_switchID !=null?router_switchID.getAsLong():0);
            JsonElement clientDistances=object.get("clientDistances");
            localLoop.setClientDistance(clientDistances != null ? clientDistances.getAsLong() : 0);
            JsonElement BTCLDistances= object.get("BTCLDistances");
            localLoop.setBtclDistance(BTCLDistances != null ? BTCLDistances.getAsLong() : 0);
            JsonElement OCDistances=object.get("OCDistances");
            localLoop.setOcdDistance(OCDistances != null ? OCDistances.getAsLong() : 0);
            JsonElement portID=object.get("portID");
            localLoop.setPortId(portID != null ? portID.getAsInt() : 0);
            JsonElement VLANID=object.get("VLANID");
            localLoop.setVlanId(VLANID != null ? VLANID.getAsInt() : 0);
            lists.add(localLoop);
        }
        return lists;
    }
}