package dashboard;

import com.google.gson.JsonObject;
import lombok.Builder;

@Builder
class DashboardCounterCard{
    String textClass;
    @Builder.Default String additionalText="";
    String subText;
    String cardValue;
    String iconClass;
    ChartType chartType;
    String cardData;

    static DashboardCounterCard getDashboardCounterCard(JsonObject jsonObject, String textClass, String additionalText, String iconClass, ChartType chartType) {
        return DashboardCounterCard.builder()
                .textClass(textClass)
                .subText(jsonObject.get("name").getAsString())
                .additionalText(additionalText)
                .cardValue(jsonObject.get("value").getAsString())
                .iconClass(iconClass)
                .chartType(chartType)
                .cardData(jsonObject.get("children").getAsString())
                .build();
    }

}
