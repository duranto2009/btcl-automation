package entity.utility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontEndHelperBean {
    @Builder.Default String url="";
    @Builder.Default String modal="";
    @Builder.Default String view = "";
    @Builder.Default String redirect="";
    @Builder.Default String param="";
}
