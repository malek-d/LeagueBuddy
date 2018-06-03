package com.mad.leaguebuddy.Handlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Maleks on 12-May-18.
 * Class handles the bulk of any request handling made to RIOT API's or DDRAGON servers for info/
 * images
 * Two functions which return data in specified data structures
 */
public class RequestHandler {
    private OkHttpClient mClient = new OkHttpClient();

    /**
     * Constructor for this class
     * Instantiate this object as empty
     */
    public RequestHandler() {
    }

    /**
     * Utilises OKHTTP to make a request to a RIOT web server and returns the data in a JSONARRAY
     * format
     * @param url
     * @return JSONArray
     */
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

    /**
     * Utilises OKHTTP to make a request to a RIOT web server and returns the data in a JSONOBJECT
     * format
     * @param url
     * @return JSONObject
     */
    public JSONObject RequestHandlerAsJsonObject(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = mClient.newCall(request).execute();
            String jsonData = response.body().string();
            return new JSONObject(jsonData);

        }catch (IOException e){e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return null;
    }
}
