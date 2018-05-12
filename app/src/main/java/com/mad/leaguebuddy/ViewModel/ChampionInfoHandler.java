package com.mad.leaguebuddy.ViewModel;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maleks on 12-May-18.
 */

public class ChampionInfoHandler {
    public ChampionInfoHandler(){}

    public String loadJSONFromAsset(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("champions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
