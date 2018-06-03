package com.mad.leaguebuddy.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.AsyncTasks.SummonerAsyncClass;
import com.mad.leaguebuddy.AsyncTasks.RankedInfoAsyncClass;
import com.mad.leaguebuddy.Model.FirebaseFactory;
import com.mad.leaguebuddy.Handlers.UrlFactory;
import com.mad.leaguebuddy.Adapters.ChampionsAdapter;
import com.mad.leaguebuddy.Handlers.RequestHandler;
import com.mad.leaguebuddy.Handlers.SummonerHandler;
import com.mad.leaguebuddy.Model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * This is the main class/ main function of this app, it displays all user information ranging from
 * basic user details down to the top 10 most played user details for the specified user.
 */
public class MainActivity extends AppCompatActivity {
    public static final String ACCOUNT_ID = "accountId";
    private static final String TAG = "tag";

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

    private SummonerHandler mSummonerHandler = new SummonerHandler();
    private FirebaseFactory mFirebaseFactory = FirebaseFactory.getInstance(this);
    private UrlFactory UrlFactory = new UrlFactory();

    private String mSummonerName, mURL, mRegion, mRankedURL, mAccountId;
    private Long mSummonerId;
    @BindView(R.id.summonerName) protected TextView mSummonerNameText;
    @BindView(R.id.levelText) protected TextView mLevelText;
    @BindView(R.id.rankTextView) protected TextView mRankTextView;
    @BindView(R.id.winsTextView) protected TextView mWinsTextView;
    @BindView(R.id.lossesTextView) protected TextView mLossesTextView;
    @BindView(R.id.winrateTextView) protected TextView mAverageTextView;
    @BindView(R.id.mLastOnlineTV) protected TextView mLastOnlineTextView;
    @BindView(R.id.soloqueueTitleTV) protected TextView mSoloQueueTitle;
    @BindView(R.id.profileImageView) protected ImageView mProfileIcon;
    @BindView(R.id.rankIcon) protected ImageView mRankIcon;
    @BindView(R.id.championMasteryView) protected RecyclerView mRecyclerView;
    @BindView(R.id.statsProgressBar) protected ProgressBar mProgressBar;
    @BindView(R.id.statsLayout) protected LinearLayout mStatsLayout;

    private ArrayList<Champion> mChampionList = new ArrayList<>();
    private ChampionsAdapter mAdapter;
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
                    Toasty.success(MainActivity.this, getString(R.string.homeClickString), Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_search:
                    Intent intent = new Intent(MainActivity.this, PlayerSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_notifications:
                    Intent historyIntent = new Intent(MainActivity.this, PlayerHistoryActivity.class);
                    historyIntent.putExtra(ACCOUNT_ID, mAccountId);
                    startActivity(historyIntent);
                    break;
                case R.id.navigation_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Custom font binding
        Typeface font = Typeface.createFromAsset(getAssets(), "Elianto-Regular.ttf");
        mSoloQueueTitle.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "cinzel_regular.ttf");
        mSummonerNameText.setTypeface(font);

        mLevelText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                JSONArray jsonArray;
                try {
                    jsonArray = new RankedInfoAsyncClass(mRankedURL).execute().get();
                    displayRankedInfo(jsonArray);
                    masteryTask(UrlFactory.getChampionMasteryUrl(mSummonerId.toString(), mRegion));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mAdapter = new ChampionsAdapter(mChampionList, MainActivity.this, mRegion);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRef = mFirebaseFactory.getRef();
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mUser = mFirebaseFactory.getUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseFactory.getAuth().removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Method handles finding the correct user based on the provided snapshot of my Firebase Database
     * when found we save the username and region of the given user to be used in other activities
     * when needed
     *
     * @param dataSnapshot
     */
    public void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals(mUser.getUid())) {
                mFirebaseFactory.setUserName(ds.child("summonerName").getValue().toString());
                mSummonerName = mFirebaseFactory.getUserName();
                mSummonerNameText.setText(mSummonerName);
                mFirebaseFactory.setRegion(ds.child("region").getValue().toString());
                mRegion = mFirebaseFactory.getRegion();
                mURL = UrlFactory.getSummonerURL(mSummonerName, mRegion.toLowerCase());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new SummonerAsyncClass(mURL).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                displaySummonerInfo(jsonObject);
            }
        }
    }

    /**
     * Display info retrieved from the AsyncTask class of RankedInfoAsyncClass
     *
     * @param array
     */
    private void displayRankedInfo(JSONArray array) {
        try {

            mStatsLayout.setVisibility(View.VISIBLE);
            JSONObject object = array.getJSONObject(0);
            mRankTextView.setText(object.getString("tier") + " " + object.getString("rank"));
            mWinsTextView.setText(object.getString("wins"));
            mLossesTextView.setText(object.getString("losses"));

            int wins = Integer.parseInt(object.getString("wins"));
            int losses = Integer.parseInt(object.getString("losses"));
            double winrate = (wins + losses);
            winrate = Math.round(((wins / winrate) * 100) * 100) / 100;
            if (winrate >= 50) {
                mAverageTextView.setTextColor(getResources().getColor(R.color.androidGreen));
            } else {
                mAverageTextView.setTextColor(getResources().getColor(R.color.negativeWinrateRed));
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

    /**
     * Display summoner info retrieved from the AsyncTask in SummonerAsyncClass
     *
     * @param s
     */
    private void displaySummonerInfo(JSONObject s) {
        try {
            int id = Integer.parseInt(s.getString("id"));
            mSummonerId = new Long(id);

            mAccountId = s.getString("accountId");
            mRankedURL = UrlFactory.getRankedStatsURL(mRegion.toLowerCase(), mSummonerId);
            mSummonerHandler.glideHelper(MainActivity.this, UrlFactory.getDdragonImageUrl
                    (s.getString("profileIconId")), R.drawable.poro_question, mProfileIcon);
            mLevelText.setText(getString(R.string.levelString) + " " + s.getString("summonerLevel"));

            Long i = Long.valueOf(s.getString("revisionDate"));
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("E d/MMM hh:mm:ss a");
            mLastOnlineTextView.setText(getString(R.string.lastOnlineString) + " " +
                    dateFormat.format(cal.getTime()));
        } catch (JSONException e) {
            Log.d(TAG, "JSON FILE ERROR" + e);
        }
    }

    /**
     * This function instantiates the class championMasteryTask AsyncTask by also providing the
     * champion mastery url
     *
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
            mProgressBar.setVisibility(View.GONE);
            for (int i = 0; i < 10; ++i) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Champion champion = new Champion(object.getString("championLevel"),
                            object.getString("championPoints"),
                            object.getString("championId"),
                            object.getString("championPointsUntilNextLevel"));
                    mChampionList.add(champion);
                } catch (org.json.JSONException e) {
                    Log.d(TAG, "JSON FILE ERROR" + e);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

