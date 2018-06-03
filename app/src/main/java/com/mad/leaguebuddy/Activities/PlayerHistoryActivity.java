package com.mad.leaguebuddy.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.Model.FirebaseFactory;
import com.mad.leaguebuddy.Handlers.RequestHandler;
import com.mad.leaguebuddy.Handlers.UrlFactory;
import com.mad.leaguebuddy.Adapters.MatchAdapter;
import com.mad.leaguebuddy.Model.Match;
import com.victor.loading.book.BookLoading;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * This activity handles the display of the users match history data from their 20 most recent games
 */
public class PlayerHistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = "tag";

    @BindView(R.id.matchHistoryRV)protected RecyclerView mMatchHistoryRV;
    @BindView(R.id.historyLayout)protected LinearLayout mHistoryLayout;
    @BindView(R.id.CradleLoadingHistoryActivity) protected NewtonCradleLoading mNewtonCradleLoading;

    private ArrayList<Match> mMatchArrayList = new ArrayList<>();
    private MatchAdapter mMatchAdapter;
    private String mAccountId;
    private String mRegion;
    private FirebaseFactory mFirebaseFactory = FirebaseFactory.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        mNewtonCradleLoading.start();
        mMatchAdapter = new MatchAdapter(mMatchArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mMatchHistoryRV.setLayoutManager(layoutManager);
        mMatchHistoryRV.setItemAnimator(new DefaultItemAnimator());
        mMatchHistoryRV.setAdapter(mMatchAdapter);

        Intent intent = getIntent();
        mRegion = mFirebaseFactory.getRegion();
        mAccountId = intent.getStringExtra(MainActivity.ACCOUNT_ID);
        matchHistoryTaskCaller();
    }

    @Override
    protected void onResume() {
        super.onResume();
        matchHistoryTaskCaller();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Method that checks if actual data exists in mAccountId
     * then begins the matchHistoryTask AsyncTask to get Match History for the given user
     */
    private void matchHistoryTaskCaller() {
        if(mAccountId == null){
            Toasty.warning(this, getString(R.string.accountIdErrorString), Toast.LENGTH_SHORT).show();
        } else{
            new matchHistoryTask().execute();
        }
    }

    /**
     * Utilises urlFactory to get the required web service url for getting match history
     * upon retrieving that data in string format we convert it into a JSONArray to parse through
     * in the onPostExecute phase of the task
     */
    private class matchHistoryTask extends AsyncTask<Void, Void, JSONArray> {
        private String mMatchUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UrlFactory UrlFactory = new UrlFactory();
            mMatchUrl = UrlFactory.getMatchUrl(mRegion,mAccountId);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            JSONObject json = requestHandler.RequestHandlerAsJsonObject(mMatchUrl);
            try{
                JSONArray jsonArray = json.getJSONArray("matches");
                return jsonArray;
            } catch(JSONException e){
                e.printStackTrace();
                Log.d(LOG_TAG, "Bad JSON file returned, see stack trace");
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray == null){
                mNewtonCradleLoading.stop();
                mNewtonCradleLoading.setVisibility(View.GONE);
                Toasty.info(PlayerHistoryActivity.this, "No match history data found :(", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "onPostExecute: PlayerHistoryActivity class no data found for user");
            } else{
                mNewtonCradleLoading.stop();
                mNewtonCradleLoading.setVisibility(View.GONE);
                for(int i = 0; i < 20; ++i){
                    try{
                        JSONObject current = jsonArray.getJSONObject(i);
                        Log.d(LOG_TAG, "Check current JSONObject");
                        Match match = new Match(current.getString("lane"), current.getString("gameId"),
                                current.getString("champion"), Long.parseLong(current.getString("timestamp")),
                                current.getString("queue"), current.getString("role"));
                        mMatchArrayList.add(match);
                        mMatchAdapter.notifyItemInserted(mMatchArrayList.size() - 1);
                        Log.d(LOG_TAG, "Match object created");
                    } catch(JSONException e){
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Bad JSON file returned, see stack trace");
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
