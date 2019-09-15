package officialLetter;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@TableName("official_letter_concern")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfficialLetterConcern {


    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("recipient_id")
    long recipientId;

    @ColumnName("recipient_type")
    RecipientType recipientType;

    @ColumnName("sender_id")
    long senderId;

    @ColumnName("sender_type")
    SenderType senderType;

    @ColumnName("refer_type")
    ReferType referType;

    @ColumnName("official_letter_id")
    long officialLetterId;

    @ColumnName("creation_time")
    @Builder.Default
    long creationTime=System.currentTimeMillis();

    @ColumnName("last_modification_time")
    @CurrentTime
    long lastModificationTime;

    @ColumnName("is_deleted")
    @Builder.Default
    boolean isDeleted = false;
}
