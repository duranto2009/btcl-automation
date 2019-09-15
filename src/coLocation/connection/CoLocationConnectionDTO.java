package coLocation.connection;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import coLocation.inventory.CoLocationInventoryDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("colocation_connection")
public class CoLocationConnectionDTO {


    @PrimaryKey
    @ColumnName("id")
    long ID;
    @ColumnName("history_id")
    long historyID;
    @ColumnName("client_id")
    long clientID;
    @ColumnName("name")
    String name;
    @ColumnName("connection_type")
    int connectionType;
    @ColumnName("rack_needed")
    boolean rackNeeded;
    @ColumnName("rack_size_id")
    Integer rackSize;
    @ColumnName("rack_space_id")
    Integer rackSpace;
    @ColumnName("power_needed")
    boolean powerNeeded;
    @ColumnName("power_amount")
    Double powerAmount;
    @ColumnName("power_type_id")
    Integer powerType;
    @ColumnName("fiber_needed")
    boolean fiberNeeded;
    @ColumnName("fiber_core")
    Integer fiberCore;
    @ColumnName("fiber_type_id")
    Integer fiberType;
    @ColumnName("active_from")
    long activeFrom;
    @ColumnName("active_to")
    long activeTo;
    @ColumnName("valid_from")
    long validFrom;
    @ColumnName("valid_to")
    long validTo;
    @ColumnName("status")
    int status;
    @ColumnName("start_date")
    long startDate;
    @ColumnName("incident")
    int incident;
    @ColumnName("discount_rate")@Builder.Default
    double discountRate = 0.0;	//new


    @ColumnName("floor_space_needed")
    boolean floorSpaceNeeded;
    @ColumnName("floor_space_amount")
    Double floorSpaceAmount;
    @ColumnName("floor_space_type")
    Integer floorSpaceType;



    String rackSizeDescription;
    String rackSpaceDescription;
    String powerTypeDescription;
    String ofcTypeDescription;
    String floorSpaceTypeDescription;
    JsonElement conTypeDescription;

    String incidentDescription;

    JsonElement client;

    List<CoLocationInventoryDTO> coLocationInventoryDTOList;


}
