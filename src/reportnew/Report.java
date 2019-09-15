package reportnew;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Report {
    public ArrayList<Map<String,Object>> mapArrayList = null;
    public List<ReportSearchCriteria> reportDisplayColumns = null;
}
