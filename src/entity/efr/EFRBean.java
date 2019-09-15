package entity.efr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EFRBean {
    private String source;
    private String sourceType;
    private String destination;
    private String destinationType;
    private long proposedLoopDistance;

}
