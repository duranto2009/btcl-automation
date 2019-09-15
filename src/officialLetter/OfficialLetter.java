package officialLetter;

import annotation.*;
import common.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("official_letter")
public  class OfficialLetter  {
    @ColumnName("id")
    @PrimaryKey
    public long id;


    @ColumnName("module_id")
    public int moduleId;

    @SearchFieldFromReferenceTable(entityClass=ClientDTO.class,entityColumnName="loginName", operator = "like")
    @ColumnName("client_id")
    public long clientId;

    @ColumnName("class_name")
    public String className;

    @ColumnName("app_id")
    public long applicationId;

    @ColumnName("official_letter_type")
    public OfficialLetterType officialLetterType;

    @ColumnName("creation_time")
    @Builder.Default
    public long creationTime=System.currentTimeMillis();

    @ColumnName("last_modification_time")
    @CurrentTime
    public long lastModificationTime;

    @ColumnName("is_deleted")
    @Builder.Default
    public boolean isDeleted = false;

}

