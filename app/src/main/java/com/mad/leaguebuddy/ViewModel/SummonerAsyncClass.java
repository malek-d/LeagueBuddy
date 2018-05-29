package com.mad.leaguebuddy.ViewModel;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;


/**
 * This Asynctask class handles presenting all basic summoner/user information as well as providing
 * a code to get profile image
 */
public class SummonerAsyncClass extends AsyncTask<Void, Void, JSONObject> {
    private String mUrl;

    public SummonerAsyncClass(String url) {
        mUrl = url;
    }

    @Override
    protected JSONObject doInBackground(Void... strings) {
        RequestHandler rh = new RequestHandler();
        return rh.RequestHandlerAsJsonObject(mUrl);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
    }
}
