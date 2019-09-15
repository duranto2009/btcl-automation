package lli.longTerm;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;
import util.DateUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("at_lli_longterm_benefit")
public class LLILongTermBenefit {

    @PrimaryKey
    @ColumnName("id")
    Long id;

    @ColumnName("contractId")
    Long contractId;

    @ColumnName("clientId")
    Long clientId;

    @ColumnName("amount")
    @Builder.Default
    double amount = 0;

    @ColumnName("isDeleted")
    @Builder.Default
    boolean isDeleted = false;

    @ColumnName("createdDate")
    @Builder.Default
    long createdDate = DateUtils.getCurrentTime();

    @CurrentTime
    @ColumnName("lastModifiedDate")
    @Builder.Default
    long lastModificationTime = DateUtils.getCurrentTime();
}
