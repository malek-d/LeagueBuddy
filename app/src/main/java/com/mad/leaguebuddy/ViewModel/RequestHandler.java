package com.mad.leaguebuddy.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Maleks on 12-May-18.
 */

public class RequestHandler {
    private OkHttpClient mClient = new OkHttpClient();

    public RequestHandler() {
    }

    public JSONArray RequestHandlerAsJsonArray(String url) {
        String jsonData;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response = mClient.newCall(request).execute();
            jsonData = response.body().string();
            JSONArray array = new JSONArray(jsonData);
            return array;
        } catch(IOException e){
            e.printStackTrace();
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject RequestHandlerAsJsonObject(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = mClient.newCall(request).execute();
            String jsonData = response.body().string();
            return new JSONObject(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String RequestHandlerAsString(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response = mClient.newCall(request).execute();
            return response.body().string();
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
