package reportnew;

import lli.LLIDropdownPair;
import lombok.Data;

import java.util.List;

@Data
public class ReportSearchCriteria  {
    String name;
    String type;
    int dataType;
    Object input;
    Object input1;
    Object input2;
    List<LLIDropdownPair> list;
}
