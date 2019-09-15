package location;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("geo_zone")
@Getter
@Setter
@NoArgsConstructor
public class Zone {

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("name")
    String nameEng;

    @ColumnName("is_central")
    boolean isCentral;
}
