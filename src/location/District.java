package location;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("geo_district")
@Getter
@Setter
@NoArgsConstructor
public class District {

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("name_eng")
    String nameEng;
    @ColumnName("name_bng")
    String nameBng;
    @ColumnName("division")
    int division;

}
