package dashboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import util.KeyValuePair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Color{
    private final static String[] colors = {
            "#845EC2",
            "#D65DB1",
            "#FF6F91",
            "#FF9671",
            "#FFC75F",
            "#F9F871",
            "#6794dc",
            "#FFC771",
            "#F9F871",
            "#FF9671",
    };
    static String getColor(int index) {
        return colors[index];
    }
}
//        List<JsonObject> jsonObjects = countMapForAppPhaseClient.entrySet()
//                .stream()
//                .map(t-> {
//
//                    ApplicationPhaseClient applicationPhaseClient = t.getKey();
//                    JsonObject jsonObject = new JsonObject();
//
//                    if(applicationPhaseClient == ApplicationPhaseClient.PROCESSING) {
//                        jsonObject.addProperty("from", "Submitted");
//                        jsonObject.addProperty("to", "Processing");
//                    }
//                    if(applicationPhaseClient == ApplicationPhaseClient.DEMAND_NOTE_GENERATED) {
//                        jsonObject.addProperty("from", "Processing");
//                        jsonObject.addProperty("to", "Demand Note Generated");
//                    }
//                    if(applicationPhaseClient == ApplicationPhaseClient.PAYMENT_DONE) {
//                        jsonObject.addProperty("from", "Demand Note Generated");
//                        jsonObject.addProperty("to", "Payment Done");
//                    }
//                    if(applicationPhaseClient == ApplicationPhaseClient.COMPLETED){
//                        jsonObject.addProperty("from", "Processing");
//                        jsonObject.addProperty("to", "Completed");
//                    }
//                    else if(applicationPhaseClient == ApplicationPhaseClient.REJECTED) {
//                        jsonObject.addProperty("from", "Processing");
//                        jsonObject.addProperty("to", "Rejected");
//                    }
//                    jsonObject.addProperty("value", t.getValue());
//
//
//
//                    return  jsonObject;
//
//                }).collect(Collectors.toList());
 class DashboardDataBuilder {


    List<JsonObject> createTimeline(List<String> texts) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        return texts.stream()
                .map(t-> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("category", atomicInteger.incrementAndGet());
                    jsonObject.addProperty("value", 1);
                    jsonObject.addProperty("text", t);
                    jsonObject.addProperty("center", atomicInteger.get() %2 == 0 ? "bottom" : "top");
                    return jsonObject;
                })
            .collect(Collectors.toList());

    }

    <T extends Number> List<JsonObject> createSimpleColumn (List<KeyValuePair<String,T>> pairs) {
        return pairs.stream()
                .map(pair-> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("category", pair.getKey());
                    jsonObject.addProperty("value", pair.getValue());
                    return jsonObject;
                })
                .collect(Collectors.toList());
    }

    <T extends Number> List<JsonObject> createBrokenPie(Map<KeyValuePair<String, T>, Map<String, T>> map) {
        AtomicInteger integer = new AtomicInteger(0);
        return map.entrySet()
            .stream()
            .map(outer -> {
                JsonObject jsonObject = new JsonObject();
                KeyValuePair<String, T> firstPair = outer.getKey();
                jsonObject.addProperty("category", firstPair.getKey());
                jsonObject.addProperty("value", firstPair.getValue());
                int index = integer.getAndIncrement();
                jsonObject.addProperty("color", Color.getColor(index));

                Map<String, T> secondMap = outer.getValue();
                List<JsonObject> children = secondMap.entrySet()
                        .stream()
                        .map(inner -> {
                            JsonObject innerJsonObject = new JsonObject();
                            innerJsonObject.addProperty("category", inner.getKey());
                            innerJsonObject.addProperty("value", inner.getValue());
                            return innerJsonObject;
                        })
                        .collect(Collectors.toList());
                JsonArray jsonArray = new JsonArray();
                children.forEach(jsonArray::add);
                jsonObject.add("children", jsonArray);
                return jsonObject;
            }).collect(Collectors.toList());
    }
}
