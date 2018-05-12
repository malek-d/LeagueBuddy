package com.mad.leaguebuddy.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.adapters.ChampionsAdapter;
import com.mad.leaguebuddy.ViewModel.RequestHandler;
import com.mad.leaguebuddy.ViewModel.SummonerHandler;
import com.mad.leaguebuddy.ViewModel.urlFactory;
import com.mad.leaguebuddy.model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    //Here Begins my member declarations
    /*
    * Handle firebase stuff separately
    * */
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private urlFactory urlFactory = new urlFactory();
    private OkHttpClient mClient = new OkHttpClient();

    private String mSummonerName, mURL, mRegion, mRankedURL;
    private Long mAccountID;
    private TextView mSummonerNameText, mLevelText, mRankTextView, mWinsTextView, mLossesTextView,
            mAverageTextView,lastOnlineTextView, mSoloQueueTitle;

    private ImageView mProfileIcon, mRankIcon;
    private ArrayList<Champion> mChampionList = new ArrayList<>();
    private ChampionsAdapter mAdapter;

    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mStatsLayout;
    private SummonerHandler mSummonerHandler = new SummonerHandler();
    //Here ends my member declarations

    /**
     * Bottom navigation bar initialization
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_search:
                    Intent intent = new Intent(MainActivity.this, PlayerSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_notifications:

                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Firebase declarations here
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        //Listener to get current user from FireBase Database
        //Handle this in separate class
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Widget Binding start
        mSummonerNameText = findViewById(R.id.summonerName);
        mLevelText = findViewById(R.id.levelText);
        mRankTextView = findViewById(R.id.rankTextView);
        mWinsTextView = findViewById(R.id.winsTextView);
        mLossesTextView = findViewById(R.id.lossesTextView);
        mAverageTextView = findViewById(R.id.winrateTextView);
        lastOnlineTextView = findViewById(R.id.mLastOnlineTV);
        mSoloQueueTitle = findViewById(R.id.soloqueueTitleTV);
        mProfileIcon = findViewById(R.id.profileImageView);
        mRankIcon = findViewById(R.id.rankIcon);
        mProgressBar = findViewById(R.id.statsProgressBar);
        mStatsLayout = findViewById(R.id.statsLayout);
        recyclerView = findViewById(R.id.championMasteryView);
        //Widget binding end

        //Custom font binding
        Typeface font = Typeface.createFromAsset(getAssets(), "Elianto-Regular.ttf");
        mSoloQueueTitle.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "cinzel_regular.ttf");
        mSummonerNameText.setTypeface(font);

        mLevelText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                summonerRankTask(mRankedURL);
                masteryTask(urlFactory.getChampionMasteryUrl(mAccountID.toString(), mRegion));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAdapter = new ChampionsAdapter(mChampionList, MainActivity.this, mRegion);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Method handles finding the correct user based on the provided snapshot of my Firebase Database
     * when found we set initialize the adapter for the champion mastery layout at the bottom
     * also call the summonerTask to load summoner name and display profile Icon
     * @param dataSnapshot
     */
    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals(mUser.getUid())) {
                mSummonerName = ds.child("summonerName").getValue().toString();
                mSummonerNameText.setText(mSummonerName);
                mRegion = ds.child("region").getValue().toString();
                mURL = urlFactory.getSummonerURL(mSummonerName, mRegion.toLowerCase());
                summonerTask(mURL);

            }
        }
    }

    /**
     * Calls the getRankedInfoTask AsyncTask to call a riot urlFactory to
     * retrieve player information and also ranked queue information
     * @param mRankedURL
     */
    private void summonerRankTask(String mRankedURL) {
        new getRankedInfoTask(mRankedURL).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mUser = mAuth.getCurrentUser();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void summonerTask(String url) {
        new getSummonerTask(url).execute();
    }

    class getSummonerTask extends AsyncTask<Void, Void, JSONObject> {
        String url;


        public getSummonerTask(String string) {
            url = string;
        }


        @Override
        protected JSONObject doInBackground(Void... strings) {
            RequestHandler rh = new RequestHandler();
            return rh.RequestHandlerAsJsonObject(url);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(JSONObject s) {
            try{
                int id = Integer.parseInt(s.getString("id"));
                mAccountID = new Long(id);
                mRankedURL = urlFactory.getRankedStatsURL(mRegion.toLowerCase(), mAccountID);
                mSummonerHandler.glideHelper(MainActivity.this, urlFactory.getDdragonImageUrl
                        (s.getString("profileIconId")),R.drawable.poro_question, mProfileIcon);
                mLevelText.setText(getString(R.string.levelString)+ " " + s.getString("summonerLevel"));

                Long i = new Long(s.getString("revisionDate"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(i);
                SimpleDateFormat dateFormat = new SimpleDateFormat("E d/MMM hh:mm:ss a");
                    lastOnlineTextView.setText(getString(R.string.lastOnlineString) + " " +  dateFormat.format(
                            cal.getTime()));
            }catch(JSONException e){

            }
        }
    }

    private class getRankedInfoTask extends AsyncTask<Void, Void, JSONArray> {
        String url;

        public getRankedInfoTask(String mRankedURL) {
            url = mRankedURL;
        }

        @Override
        protected void onPreExecute() {
            mStatsLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            RequestHandler rh = new RequestHandler();
            return rh.RequestHandlerAsJsonArray(url);
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            try {
                    JSONObject object = array.getJSONObject(0);
                    mRankTextView.setText(object.getString("tier") + " " + object.getString("rank"));
                    mWinsTextView.setText(object.getString("wins"));
                    mLossesTextView.setText(object.getString("losses"));

                    int wins = Integer.parseInt(object.getString("wins"));
                    int losses = Integer.parseInt(object.getString("losses"));
                    double winrate = (wins + losses);
                    winrate = Math.round(((wins / winrate) * 100) * 100) / 100;
                    if (winrate >= 50) {
                        mAverageTextView.setTextColor(getColor(R.color.androidGreen));
                    } else {
                        mAverageTextView.setTextColor(getColor(R.color.negativeWinrateRed));
                    }
                    mAverageTextView.setText(new StringBuilder().append(winrate).append(getString
                            (R.string.percentString)).toString());
                    setTitle(getString(R.string.tierNameString) + "  " + object.getString("leagueName"));
                    mSummonerHandler.setRankIcon(object.getString("tier"), mRankIcon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * @param championMasteryUrl
     */
    private void masteryTask(String championMasteryUrl) {
        new championMasteryTask(championMasteryUrl).execute();
    }

    /**
     * AsyncTask which gets champion ID, Mastery Level & Mastery Points
     * champion Object is created with these options and then stored in mChampionList
     * to be forwarded to the adapter
     */
    private class championMasteryTask extends AsyncTask<Void, Void, JSONArray> {
        private String masteryURL;

        public championMasteryTask(String championMasteryUrl) {
            masteryURL = championMasteryUrl;
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            RequestHandler rh = new RequestHandler();
            return rh.RequestHandlerAsJsonArray(masteryURL);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            for (int i = 0; i < 10; ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Champion champion = new Champion(object.getString("championLevel"),
                            object.getString("championPoints"),
                            object.getString("championId"),
                            object.getString("championPointsUntilNextLevel"));
                    mChampionList.add(champion);
                } catch (org.json.JSONException e) {

                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

