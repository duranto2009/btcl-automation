package common.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthWiseDue {
    String serial;
    String month;
    String year;
    String due;
}
