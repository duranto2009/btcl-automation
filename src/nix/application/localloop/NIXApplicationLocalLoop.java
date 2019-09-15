package nix.application.localloop;

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
@TableName("nix_application_local_loop")
public class NIXApplicationLocalLoop {
    private static final Logger logger = LoggerFactory.getLogger(NIXApplicationLocalLoop.class);

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
    @ColumnName("history_id")
    long historyId;

    String popName;
    String portName;
    String switchName;
    String vlanName;
    String portTypeString;
    public List<NIXApplicationLocalLoop> deserialize_custom(JsonElement jsonElements) {
        List<NIXApplicationLocalLoop> lists = new ArrayList<>();
        JsonObject jsonObject = jsonElements.getAsJsonObject();
        //long appID=Integer.parseInt(String.valueOf(jsonObject.get("applicationID")));
        JsonArray jsonArray = jsonObject.getAsJsonArray("localloops");
        if (jsonArray != null){
            for (JsonElement jsonElement : jsonArray) {
                NIXApplicationLocalLoop localLoop = new NIXApplicationLocalLoop();
                JsonObject object = jsonElement.getAsJsonObject();
                JsonElement ID = object.get("id");
                localLoop.setId(ID != null ? ID.getAsLong() : 0);
                JsonElement appID = object.get("application");
                localLoop.setApplication(appID != null ? appID.getAsLong() : 0);

                JsonElement ofcType = object.get("ofcType");
                localLoop.setOfcType(ofcType != null ? ofcType.getAsInt() : 0);

                JsonElement btclDistance = object.get("btclDistance");
                localLoop.setBtclDistance(btclDistance != null ? btclDistance.getAsLong() : 0);

                JsonElement clientDistance = object.get("clientDistance");
                localLoop.setClientDistance(clientDistance != null ? clientDistance.getAsLong() : 0);

                JsonElement ocdDistance = object.get("ocdDistance");
                localLoop.setOcdDistance(ocdDistance != null ? ocdDistance.getAsLong() : 0);
                JsonElement vendor = object.get("vendor");
                localLoop.setVendor(vendor != null ? vendor.getAsLong() : 0);

                JsonElement popID = object.get("popID") != null ? object.get("popID") : object.get("popId");
                localLoop.setPopId(popID != null ? popID.getAsLong() : 0);
                localLoop.setOfficeId(object.get("officeId").getAsLong());

                if(object.has("router_switchID")){

                    JsonElement router_switchID = object.get("router_switchID");
                    localLoop.setRouterSwitchId(router_switchID.getAsLong());
                }else if(object.has("routerSwitchId")){
                    JsonElement router_switchID = object.get("routerSwitchId");
                    localLoop.setRouterSwitchId( router_switchID.getAsLong());
                }else{
                    localLoop.setRouterSwitchId(0);
                }


                if(object.has("portID")){

                    JsonElement portID = object.get("portID");
                    localLoop.setPortId(portID != null ? portID.getAsInt() : 0);
                }else if(object.has("portId")){
                    JsonElement portID = object.get("portId");
                    localLoop.setPortId( portID.getAsInt() );
                }else{
                    localLoop.setPortId(0);
                }


                if(object.has("VLANID")){

                    JsonElement VLANID = object.get("VLANID");
                    localLoop.setVlanId(VLANID != null ? VLANID.getAsInt() : 0);
                }else if(object.has("vlanId")){

                    JsonElement VLANID = object.get("vlanId");
                    localLoop.setVlanId(VLANID != null ? VLANID.getAsInt() : 0);
                }else{
                    localLoop.setVlanId(0);
                }


                JsonElement historyId = object.get("historyId");
                localLoop.setHistoryId(btclDistance != null ? historyId.getAsLong() : 0);

                lists.add(localLoop);
            }
        }
        return lists;
    }
}