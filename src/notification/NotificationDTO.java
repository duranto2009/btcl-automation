package notification;

import annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("notification")
public class NotificationDTO {
	@ColumnName("id")
	@PrimaryKey
	long id;
	@ColumnName("generation_time")
	long generationTime;
	@ColumnName("module_id")
	long moduleId;
	@ColumnName("entity_id")
	long entityId;
	@ColumnName("entity_Type")
	String entityType;
	@ColumnName("role_account_id")
	long roleOrAccountId;
	@ColumnName("user_id")
	long userId;
	@ColumnName("description")
	String description;
	@ColumnName("action_url")
	String actionURL;
	@ColumnName("is_seen")
    boolean isSeen;
	@ColumnName("is_action_taken")
	boolean isActionTaken;
	@ColumnName("has_child")
	boolean hasChild;
	@ColumnName("is_for_client")
	boolean isForClient;
	@ColumnName("child_id")
	long childId;


}
