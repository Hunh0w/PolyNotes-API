package fr.hunh0w.polynotes.utils;

import org.jose4j.json.internal.json_simple.JSONObject;

public class JsonUtils {

    public static JSONObject getErrorMessage(String message){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject;
    }

}
