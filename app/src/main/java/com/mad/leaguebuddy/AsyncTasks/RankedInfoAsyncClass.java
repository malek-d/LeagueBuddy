package com.mad.leaguebuddy.AsyncTasks;

import android.os.AsyncTask;

import com.mad.leaguebuddy.ViewModel.RequestHandler;

import org.json.JSONArray;

/**
 * Java Class which extends the AsyncTask superclass
 * Purpose of this class is to retrieve and return a JSON array of ranked statistics for the given user
 * This AsyncTask handles all forms of competitive ranked information for the given user
 * Upon being fed the proper url it shall return ranked tier, status, wins and losses
 */
public class RankedInfoAsyncClass extends AsyncTask<Void, Void, JSONArray> {

    String mUrl;

    /**
     * Constructor which takes in the url needed for the execution of this API call
     * @param mRankedURL
     */
    public RankedInfoAsyncClass(String mRankedURL) {
        mUrl = mRankedURL;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        RequestHandler rh = new RequestHandler();
        return rh.RequestHandlerAsJsonArray(mUrl);
    }

    @Override
    protected void onPostExecute(JSONArray array) {
    }
}
