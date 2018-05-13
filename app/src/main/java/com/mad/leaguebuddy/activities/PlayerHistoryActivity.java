package com.mad.leaguebuddy.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.RequestHandler;
import com.mad.leaguebuddy.ViewModel.UrlFactory;
import com.mad.leaguebuddy.adapters.MatchAdapter;
import com.mad.leaguebuddy.model.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class PlayerHistoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @BindView(R.id.matchHistoryRV)
    protected RecyclerView mMatchHistoryRV;
    @BindView(R.id.historyLayout)
    protected LinearLayout mHistoryLayout;

    private ArrayList<Match> mMatchArrayList = new ArrayList<>();
    private MatchAdapter mMatchAdapter;
    private String mAccountId;
    private String mRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mMatchAdapter = new MatchAdapter(mMatchArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mMatchHistoryRV.setLayoutManager(layoutManager);
        mMatchHistoryRV.setItemAnimator(new DefaultItemAnimator());
        mMatchHistoryRV.setAdapter(mMatchAdapter);

        Intent intent = getIntent();

        mRegion = intent.getStringExtra(MainActivity.REGION);
        mAccountId = intent.getStringExtra(MainActivity.ACCOUNT_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        matchHistoryTaskCaller();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void matchHistoryTaskCaller() {
        if(mAccountId == null){
            Toasty.warning(this, getString(R.string.accountIdErrorString), Toast.LENGTH_SHORT).show();
        } else{
            new matchHistoryTask().execute();
        }
    }

    private class matchHistoryTask extends AsyncTask<Void, Void, JSONArray> {
        private String mMatchUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UrlFactory UrlFactory = new UrlFactory();
            mMatchUrl = UrlFactory.getMatchUrl(mAccountId, mRegion);
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray == null){
            } else{
                for(int i = 0; i < 20; ++i){
                    try{
                        JSONObject current = jsonArray.getJSONObject(i);
                        Match match = new Match(current.getString("lane"), current.getString("gameId"),
                                current.getString("champion"), Long.parseLong(current.getString("timestamp")),
                                current.getString("queue"), current.getString("role"));

                        mMatchArrayList.add(match);
                        mMatchAdapter.notifyItemInserted(mMatchArrayList.size() - 1);


                    } catch(JSONException e){
                        e.printStackTrace();
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
