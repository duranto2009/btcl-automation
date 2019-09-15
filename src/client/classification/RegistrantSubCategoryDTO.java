package client.classification;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("at_client_reg_sub_category")
public class RegistrantSubCategoryDTO {

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("parentCatId")
    long parentCategoryId;

    @ColumnName("regSubCatId")
    long registrantSubCategoryId;

    @ColumnName("name")
    String name;

    @ColumnName("isDeleted")
    boolean isDeleted = false;

    @ColumnName("lastModificationTime")
    @CurrentTime
    long lastModificationTime;
}
