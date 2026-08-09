package org.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class JsonParser {
    @SuppressWarnings("unchecked")
    public static List<DataModel> parse(String json) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String script = "var obj = JSON.parse('" + json.replace("'", "\\'") + "'); obj;";
        Object result = engine.eval(script);
        List<DataModel> list = new ArrayList<>();
        if (result instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) result;
            Object addresses = map.get("addresses");
            if (addresses instanceof List) {
                List<Map<String, String>> items = (List<Map<String, String>>) addresses;
                for (Map<String, String> item : items) {
                    String domain = item.get("domain");
                    String ip = item.get("ip");
                    if (domain != null && ip != null) {
                        list.add(new DataModel(domain, ip));
                    }
                }
            }
        }
        return list;
    }
    public static String toJson(List<DataModel> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"addresses\":[");

        for (int i = 0; i < list.size(); i++) {
            DataModel item = list.get(i);
            sb.append("{\"domain\":\"")
                .append(item.getDomain())
                .append("\",")
                .append("\"ip\":\"")
                .append(item.getIp())
                .append("\"}");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]}");
        return sb.toString();
    }
}