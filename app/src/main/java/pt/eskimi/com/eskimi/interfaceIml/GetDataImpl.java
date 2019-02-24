package pt.eskimi.com.eskimi.interfaceIml;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pt.eskimi.com.eskimi.apiconnection.RequestType;
import pt.eskimi.com.eskimi.apiconnection.ServerConnectListener;
import pt.eskimi.com.eskimi.apiconnection.ServerConnection;
import pt.eskimi.com.eskimi.interfacepackage.DataView;
import pt.eskimi.com.eskimi.presenter.GetDataPresenter;
import pt.eskimi.com.eskimi.utils.URLs;

/**
 * Created by Naim on 2/24/2019.
 */

public class GetDataImpl implements GetDataPresenter, RequestType,
        ServerConnectListener {

    private DataView dataView = null;
    private Context context = null;
    private final int API_GET_DATA = 1;

    public GetDataImpl(DataView dataView, Context context) {

        this.dataView = dataView;
        this.context = context;
    }

    @Override
    public void getDataFromSever() {
        String url = URLs.BASE_URL;
        Map<String, String> params = new HashMap<>();


        Map<String, String> header = new HashMap<>();


        ServerConnection serverConnection = new ServerConnection();
        serverConnection.setServerConnectListener(this);
        serverConnection.connectToApi(context, params, url, GET, API_GET_DATA, header);
    }

    @Override
    public void onSuccessfulServerConnection(JSONObject jsonObject, int apiType) {
        switch (apiType) {
            case API_GET_DATA:
                dataView.onSuccessGetData(context, jsonObject);
                break;

        }
    }

    @Override
    public void onFailureServerConnection(JSONObject jsonObject, int apiType, int errorCode, String message) {
        switch (apiType) {
            case API_GET_DATA:
                dataView.onFailureGetData(context, errorCode, message, jsonObject);
                break;

        }
    }
}
