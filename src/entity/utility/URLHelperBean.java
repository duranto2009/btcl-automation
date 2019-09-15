package entity.utility;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class URLHelperBean {
    @Builder.Default String url="";
    @Builder.Default String subMenu1="";
    @Builder.Default String subMenu2="";
    @Builder.Default String subMenu3="";
}
