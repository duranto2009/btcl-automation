package reportnew;

import lombok.Data;

import java.util.List;

@Data
public class PostEntity {
    List<ReportSearchCriteria> selectedSearchCriteria;
    List<ReportSearchCriteria>selectedDisplayItems;
    List<ReportSearchCriteria> selectedOrderItems;

}
