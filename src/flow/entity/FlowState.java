package flow.entity;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import lombok.*;

/**
 * @author maruf
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@TableName("flow_state")
public class FlowState {

    @PrimaryKey
    @ColumnName("id")
    int id;
    @ColumnName("name")
    String name;
    @ColumnName("description")
    String description;
    @ColumnName("flow")
    int flow;
    @ColumnName("color")
    String color;
    @ColumnName("view_description")
    String viewDescription;

    @ColumnName("phase")
    String phase;

    String url;
    String modal;
    String view;
    String redirect;
    String param;
    public FlowState(int id,String name, String description,int flow,String color, String viewDescription){
        this.id = id;
        this.name = name;
        this.description = description;
        this.flow = flow;
        this.color = color;
        this.viewDescription = viewDescription;
    }

}
