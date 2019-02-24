package pt.eskimi.com.eskimi.utils;

import org.json.JSONObject;

/**
 * Created by Naim on 7/29/2018.
 */

public class JsonParse {

    public static boolean checkJson(String parseKey, JSONObject jsonObject) {
        boolean isValid = false;
        if (jsonObject.has(parseKey)) {
            if (!jsonObject.isNull(parseKey)) {
                isValid = true;
            }
        }
        return isValid;
    }



}
