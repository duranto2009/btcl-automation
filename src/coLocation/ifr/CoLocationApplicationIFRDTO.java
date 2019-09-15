package coLocation.ifr;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;


@Getter
@Setter
@Data
@Builder @AllArgsConstructor @NoArgsConstructor
@TableName("colocation_application_ifr")
public class CoLocationApplicationIFRDTO {
    @PrimaryKey @ColumnName("id") long id;
    @ColumnName("parent_ifr_id") long parentIFRID;
    @ColumnName("application_id") long applicationID;
    @ColumnName("submission_date") long submissionDate;
    @ColumnName("state") int state;
    @ColumnName("type_name") String typeName;
    @ColumnName("category_label") String categoryLabel;
    @ColumnName("category_name") String categoryName;
    @ColumnName("amount_label") String amountLabel;
    @ColumnName("amount_name") String amountName;
    @ColumnName("is_replied") boolean isReplied;
    @ColumnName("is_selected") boolean isSelected;
    @ColumnName("is_completed") boolean isCompleted;
    @ColumnName("user_role_id") int userRoleId;
    @ColumnName("user_type") int userType;
    @ColumnName("deadline") long deadline;

}
