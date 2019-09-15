package lli.Application.ownership;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.Data;

@Data
@TableName("lli_on_process_connection")
public class LLIOnProcessConnection {
    @PrimaryKey
    @ColumnName("id")
    long id;
    @ColumnName("application")
    long application;
    @ColumnName("connection")
    long connection;
}
