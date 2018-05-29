package com.mad.leaguebuddy.ViewModel;

import android.os.AsyncTask;
import org.json.JSONArray;

/**
 * Created by Maleks on 29-May-18.
 */

public class RankedInfoAsyncTask extends AsyncTask<Void, Void, JSONArray> {
    /**
     * This AsyncTask handles all forms of competitive ranked information for the given user
     * Upon being fed the proper url it shall return ranked tier, status, wins and losses
     */
    String url;

    public RankedInfoAsyncTask(String mRankedURL) {
        url = mRankedURL;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        RequestHandler rh = new RequestHandler();
        return rh.RequestHandlerAsJsonArray(url);
    }

    @Override
    protected void onPostExecute(JSONArray array) {
    }
}
