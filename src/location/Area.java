package location;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("geo_area")
@Getter
@Setter
@NoArgsConstructor
public class Area {


    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("zone_id")
    long zoneId;
    @ColumnName("name")
    String nameEng;
}
