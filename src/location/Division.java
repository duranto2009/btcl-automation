package location;


import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.struts.action.ActionForm;

@TableName("geo_division")
@Getter
@Setter
@NoArgsConstructor
public class Division {

    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("name_eng")
    String nameEng;
    @ColumnName("name_bng")
    String nameBng;


}
