package lli.Application.BandwidthConfiguration;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@TableName("at_lli_bandwidth_configuration")
@Getter
@Setter
@Builder
public class BandwidthConfiguration {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("bandwidth")
    long bandwidth;
    @ColumnName("duration")
    long duration;
}
