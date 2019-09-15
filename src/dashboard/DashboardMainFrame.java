package dashboard;

import com.google.gson.JsonObject;
import lombok.Builder;

@Builder
class DashboardMainFrame {
    String title;
    String div;
    String frameData;
    ChartType chartType;

    static DashboardMainFrame getMainFrame(JsonObject jsonObject, String title, String div, ChartType chartType) {
        return DashboardMainFrame.builder()
                .chartType(chartType)
                .div(div)
                .title(title)
                .frameData(jsonObject.get("children").getAsString())
                .build();
    }
}
