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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.data.urlFactory;
import com.mad.leaguebuddy.model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private TextView mSummonerNameText, mLevelText, mRankTextView, mWinsTextView,
            mLossesTextView, mAverageTextView, lastOnlineTextView, mSoloQueueTitle;

    private ImageView mProfileIcon, mRankIcon;
    private ArrayList<Champion> mChampionList = new ArrayList<>();
    private ChampionsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mStatsLayout;
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
        mProfileIcon = findViewById(R.id.profileImageView);
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
        Toasty.success(MainActivity.this, getString(R.string.loggedinMessage) + " " + mUser.getEmail()).show(); //On Authentication Successful

        //Binding All my views
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //--------------
        mSummonerNameText = findViewById(R.id.summonerName);
        mLevelText = findViewById(R.id.levelText);
        mRankTextView = findViewById(R.id.rankTextView);
        mWinsTextView = findViewById(R.id.winsTextView);
        mLossesTextView = findViewById(R.id.lossesTextView);
        mAverageTextView = findViewById(R.id.winrateTextView);
        mRankIcon = findViewById(R.id.rankIcon);
        lastOnlineTextView = findViewById(R.id.mLastOnlineTV);
        mProgressBar = findViewById(R.id.statsProgressBar);
        mSoloQueueTitle = findViewById(R.id.soloqueueTitleTV);
        mStatsLayout = findViewById(R.id.statsLayout);
        //End Binding

        //Adding custom font to our table title here
        Typeface font = Typeface.createFromAsset(getAssets(), "Elianto-Regular.ttf");
        mSoloQueueTitle.setTypeface(font);

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
                //Adding custom font here again
                Typeface font = Typeface.createFromAsset(getAssets(), "cinzel_regular.ttf");
                mSummonerNameText.setTypeface(font);
                mSummonerNameText.setText(mSummonerName);
                mRegion = ds.child("region").getValue().toString();
                mURL = urlFactory.getSummonerURL(mSummonerName, mRegion.toLowerCase());
                summonerTask(mURL);

                mAdapter = new ChampionsAdapter(mChampionList, MainActivity.this, mRegion);
                recyclerView = findViewById(R.id.championMasteryView);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
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
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = mClient.newCall(request).execute();
                String jsonData = response.body().string();
                return new JSONObject(jsonData);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(JSONObject s) {
            try{
                int id = Integer.parseInt(s.getString("id"));
                mAccountID = new Long(id);
                mRankedURL = urlFactory.getRankedStatsURL(mRegion.toLowerCase(), mAccountID);
                summonerRankTask(mRankedURL);
                //--------------
                Glide.with(MainActivity.this)
                        .load(urlFactory.getDdragonImageUrl(s.getString("profileIconId")))
                        .placeholder(R.drawable.poro_question)
                        .into(mProfileIcon);
                mProfileIcon.setBackground(getResources().getDrawable(R.drawable.image_shape));
                //--------------
                mLevelText.setText(getString(R.string.levelString)+ " " + s.getString("summonerLevel"));
                //--------------
                Long i = new Long(s.getString("revisionDate"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(i);
                SimpleDateFormat dateFormat = new SimpleDateFormat("E d/MMM hh:mm:ss a");
                    lastOnlineTextView.setText(getString(R.string.lastOnlineString) + " " +  dateFormat.format(cal.getTime()));
            }catch(JSONException e){

            }
        }
    }

    private class getRankedInfoTask extends AsyncTask<Void, Void, JSONArray> {
        String url;
        String jsonData;

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
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = mClient.newCall(request).execute();
                jsonData = response.body().string();
                JSONArray array = new JSONArray(jsonData);
                return array;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            for (int i = 0; i < array.length(); ++i) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    mRankTextView.setText(object.getString("tier") + " " + object.getString("rank").toString());
                    mWinsTextView.setText(object.getString("wins"));
                    mLossesTextView.setText(object.getString("losses"));
                    int wins = Integer.parseInt(object.getString("wins"));
                    int losses = Integer.parseInt(object.getString("losses"));
                    double winrate = (wins + losses);
                    winrate = wins / winrate;
                    winrate = winrate * 100;
                    winrate = Math.round(winrate * 100) / 100;
                    if (winrate >= 50) {
                        mAverageTextView.setTextColor(getResources().getColor(R.color.androidGreen));
                    } else {
                        mAverageTextView.setTextColor(getResources().getColor(R.color.negativeWinrateRed));
                    }
                    mAverageTextView.setText(winrate + "%");
                    setTitle(getString(R.string.tierNameString) + "  " + object.getString("leagueName"));
                    setRankIcon(object.getString("tier"));

                    masteryTask(urlFactory.getChampionMasteryUrl(mAccountID.toString(), mRegion));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @param championMasteryUrl
     */
    private void masteryTask(String championMasteryUrl) {
        new championMasteryTask(championMasteryUrl).execute();
    }

    /**
     * Sets the Tier icon depending the ranked tier provided
     *
     * @param tier
     */
    private void setRankIcon(String tier) {
        switch (tier) {
            case "BRONZE":
                mRankIcon.setImageResource(R.drawable.bronze_rank_icon);
                break;
            case "SILVER":
                mRankIcon.setImageResource(R.drawable.silver_rank_icon);
                break;
            case "GOLD":
                mRankIcon.setImageResource(R.drawable.gold_rank_icon);
                break;
            case "PLATINUM":
                mRankIcon.setImageResource(R.drawable.platinum_rank_icon);
                break;
            case "DIAMOND":
                mRankIcon.setImageResource(R.drawable.platinum_rank_icon);
                break;
            case "MASTER":
                mRankIcon.setImageResource(R.drawable.master_rank_icon);
                break;
            case "CHALLENGER":
                mRankIcon.setImageResource(R.drawable.challenger_rank_icon);
                break;
        }
    }

    /**
     * AsyncTask which gets champion ID, Mastery Level & Mastery Points
     * champion Object is created with these options and then stored in mChampionList
     * to be forwarded to the adapter
     */
    private class championMasteryTask extends AsyncTask<Void, Void, JSONArray> {
        private String masteryURL;
        private String jsonData;

        public championMasteryTask(String championMasteryUrl) {
            masteryURL = championMasteryUrl;
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(masteryURL)
                    .build();

            try {
                Response response = mClient.newCall(request).execute();
                jsonData = response.body().string();
                JSONArray array = new JSONArray(jsonData);
                return array;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
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

