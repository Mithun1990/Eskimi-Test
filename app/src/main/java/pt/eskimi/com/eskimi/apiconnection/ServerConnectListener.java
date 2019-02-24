package pt.eskimi.com.eskimi.apiconnection;

import org.json.JSONObject;

/**
 * Created by Naim on 7/19/2018.
 */

public interface ServerConnectListener {

    void onSuccessfulServerConnection(JSONObject jsonObject, int apiType);

    void onFailureServerConnection(JSONObject jsonObject, int apiType, int errorCode, String message);
}
