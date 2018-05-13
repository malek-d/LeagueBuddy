package com.mad.leaguebuddy.ViewModel;

import android.content.Context;

import com.mad.leaguebuddy.model.Champion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maleks on 12-May-18.
 */

public class ChampionInfoHandler {

    String mJson;

    public ChampionInfoHandler(Context context) {
        mJson = loadJSONFromAsset(context);
    }

    public String loadJSONFromAsset(Context mContext) {
        String json = null;
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

    public Champion getChampionFromJson( String championID) {
        JSONObject jo = null;
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
