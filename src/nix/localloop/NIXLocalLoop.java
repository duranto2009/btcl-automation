package nix.localloop;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.application.localloop.NIXApplicationLocalLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
@TableName("nix_local_loop")
public class NIXLocalLoop {
    private static final Logger logger = LoggerFactory.getLogger(NIXLocalLoop.class);

    @PrimaryKey
    @ColumnName("id")
    long id;

    @ColumnName("application_local_loop")
    long applicationLocalLoop;

    @ColumnName("connection")
    long connection;

    @ColumnName("office")
    long office;

    NIXApplicationLocalLoop nixApplicationLocalLoop;
}