package pt.eskimi.com.eskimi.apiconnection;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import pt.eskimi.com.eskimi.utils.DetectConnection;
import pt.eskimi.com.eskimi.utils.URLs;


/**
 * Created by Naim on 7/19/2018.
 */

public class ServerConnection implements RequestType {

    private AsyncHttpClient client = new AsyncHttpClient();
    ServerConnectListener serverConnectListener;

    public void setServerConnectListener(ServerConnectListener serverConnectListener) {
        this.serverConnectListener = serverConnectListener;
    }

    public void connectToApi(Context context, Map<String, String> requestParams,
                             String url, int requestType, int apiType, Map<String, String> header) {


        try {

            if (client == null)
                return;

            RequestParams params = new RequestParams();
            Iterator parameter = requestParams.keySet().iterator();
            while (parameter.hasNext()) {
                String key = (String) parameter.next();
                String value = requestParams.get(key);
                params.put(key, value);
            }

            Iterator parameterHeader = header.keySet().iterator();
            while (parameterHeader.hasNext()) {
                String key = (String) parameterHeader.next();
                String value = header.get(key);
                client.addHeader(key, value);
            }

            client.setTimeout(URLs.TIME_OUT);


            if (DetectConnection.checkInternetConnection(context)) {
                if (POST == requestType) {
                    post(url, params, apiType);
                } else if (GET == requestType) {
                    get(url, params, apiType);
                }
            } else {
                if (serverConnectListener != null)
                    serverConnectListener.onFailureServerConnection(null, apiType, INTERNET_OR_SERVER_ERROR, "Connection error.");

            }


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void post(String url, RequestParams params, final int apiType) {
        try {
            System.out.println("Params " + params.toString());
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {


                        String response = new String(responseBody);
                        System.out.println("Response " + response);
                        JSONObject json = new JSONObject(response);
                        String success = json.getString("success") + "";
                        String message = json.getString("message");
                        if (success.contentEquals("1")) {
                            if (serverConnectListener != null)
                                serverConnectListener.onSuccessfulServerConnection(json, apiType);


                        } else {
                            try {
                                int error_code = Integer.parseInt(json.getString("error_code"));
                                //JSONObject jsonObject = json.getJSONObject("data");
                                if (serverConnectListener != null)
                                    serverConnectListener.onFailureServerConnection(json, apiType, error_code, message);

                            } catch (NumberFormatException e) {
                                if (serverConnectListener != null)
                                    serverConnectListener.onFailureServerConnection(json, apiType, JSON_PARSING_ERROR, message);

                            } catch (JSONException e) {
                                if (serverConnectListener != null)
                                    serverConnectListener.onFailureServerConnection(json, apiType, JSON_PARSING_ERROR, message);

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (serverConnectListener != null)
                            serverConnectListener.onFailureServerConnection(null, apiType, INTERNET_OR_SERVER_ERROR, "Connection error.");


                    }


                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                    if (responseBody != null) {
                        String string = new String(responseBody);
                        System.out.println("Res " + string);
                    }
                    if (serverConnectListener != null)
                        serverConnectListener.onFailureServerConnection(null, apiType, INTERNET_OR_SERVER_ERROR, "Connection error.");

                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get(String url, RequestParams params, final int apiType) {
        try {
            System.out.println("Params " + params.toString());
            client.get(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {


                        String response = new String(responseBody);
                        System.out.println("Response " + response);
                        JSONObject json = new JSONObject(response);
                        JSONArray success = json.getJSONArray("rates");

                        if (success.length() > 0) {
                            if (serverConnectListener != null)
                                serverConnectListener.onSuccessfulServerConnection(json, apiType);


                        } else {
                            try {
                                int error_code = Integer.parseInt(json.getString("error_code"));
                                //JSONObject jsonObject = json.getJSONObject("data");
                                if (serverConnectListener != null)
                                    serverConnectListener.onFailureServerConnection(json, apiType, error_code, "Connection error.");

                            } catch (JSONException e) {
                                if (serverConnectListener != null)
                                    serverConnectListener.onFailureServerConnection(json, apiType, JSON_PARSING_ERROR, "Connection error.");

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (serverConnectListener != null)
                            serverConnectListener.onFailureServerConnection(null, apiType, INTERNET_OR_SERVER_ERROR, "Connection error.");


                    }


                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        String string = new String(responseBody);
                        System.out.println("Res " + string);
                    }
                    if (serverConnectListener != null)
                        serverConnectListener.onFailureServerConnection(null, apiType, INTERNET_OR_SERVER_ERROR, "Connection error.");

                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
