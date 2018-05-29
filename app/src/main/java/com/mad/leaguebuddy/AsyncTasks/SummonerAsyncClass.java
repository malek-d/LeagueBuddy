package com.mad.leaguebuddy.AsyncTasks;

import android.os.AsyncTask;

import com.mad.leaguebuddy.ViewModel.RequestHandler;

import org.json.JSONObject;

/**
 * This Asynctask class handles presenting all basic summoner/user information as well as providing
 * a code to get profile image
 */
public class SummonerAsyncClass extends AsyncTask<Void, Void, JSONObject> {
    private String mUrl;

    /**
     * Constructor which takes in the url needed to retrieve data on the given user
     * @param url
     */
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
