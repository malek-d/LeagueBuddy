package com.mad.leaguebuddy.Handlers;

import android.content.Context;

import com.mad.leaguebuddy.Model.Champion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maleks on 12-May-18.
 * This Class handles all backend logic that relates to traversing through the champions.json
 * file located in the assets folder
 */

public class ChampionInfoHandler {

    String mJson;

    /**
     * Constructor for this object which just takes in the activity context to be able to
     * access assets
     * @param context
     */
    public ChampionInfoHandler(Context context) {
        mJson = loadJSONFromAsset(context);
    }

    /**
     * This function calls upon the JSON file itself and returns it in a UTF-8 string format
     * @param mContext
     * @return
     */
    public String loadJSONFromAsset(Context mContext) {
        String json;
        try{
            InputStream is = mContext.getAssets().open("champions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * This function utilises the championID provided to traverse through mJson to get the specified
     * Champion aswell as any related information to that specific character
     * @param championID
     * @return
     */
    public Champion getChampionFromJson( String championID) {
        JSONObject jo;
        try{
            jo = new JSONObject(mJson);
            JSONObject jsonObject = jo.getJSONObject(championID);
            Champion champion = new Champion();
            champion.setChampionKey(jsonObject.getString("key"));
            champion.setChampionName(jsonObject.getString("name"));
            champion.setChampionTitle(jsonObject.getString("title"));
            return champion;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
