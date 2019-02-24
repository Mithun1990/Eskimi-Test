package pt.eskimi.com.eskimi.interfacepackage;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Naim on 2/24/2019.
 */

public interface DataView {


    void onSuccessGetData(Context context, JSONObject jsonObject);

    void onFailureGetData(Context context, int errorCode, String message, JSONObject jsonObject);


}
