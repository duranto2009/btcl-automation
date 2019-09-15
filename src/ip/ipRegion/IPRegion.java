package ip.ipRegion;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import location.District;
import lombok.Data;

import java.util.*;

@Data
@TableName("ip_region")
public class IPRegion {

    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("name_en")
    String name_en;

    @ColumnName("name_bn")
    String name_bn;

    @ColumnName("availability")
    Boolean availability;


    @ColumnName( "created_date")
    Long createdDate;

    @ColumnName( "last_modified_date")
    Long lastModifiedDate;

    @ColumnName("created_by")
    String createdBy;

    @ColumnName("last_modified_by")
    String lastModifiedBy;

    List<District> districts = new ArrayList<>();
}
