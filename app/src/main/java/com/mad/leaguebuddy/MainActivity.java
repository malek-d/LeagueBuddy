package com.mad.leaguebuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.leaguebuddy.model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mSummonerName, mURL, mRegion, mRankedURL;
    private Long mAccountID;
    private TextView mSummonerNameText, mLevelText,  mRankTextView, mWinsTextView, mLossesTextView, mAverageTextView;
    private API api = new API();
    private OkHttpClient mClient = new OkHttpClient();
    private FirebaseUser mUser;
    private ImageView mProfileIcon;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
                case R.id.navigation_settings:
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
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
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toasty.success(MainActivity.this, getString(R.string.loggedinMessage) + " " + mUser.getEmail()).show();
        mSummonerNameText = findViewById(R.id.summonerName);
        mLevelText = findViewById(R.id.levelText);
        mRankTextView = findViewById(R.id.rankTextView);
        mSummonerNameText.setText(mSummonerName);
        mWinsTextView = findViewById(R.id.winsTextView);
        mLossesTextView = findViewById(R.id.lossesTextView);
        mAverageTextView = findViewById(R.id.winrateTextView);
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals(mUser.getUid())) {
                //Toasty.info(MainActivity.this,ds.child("summonerName").getValue().toString() + ds.child("region").getValue().toString(), Toast.LENGTH_SHORT ).show();
                mSummonerName = ds.child("summonerName").getValue().toString();
                mRegion = ds.child("region").getValue().toString();
                mURL = api.getSummonerURL(mSummonerName, mRegion.toLowerCase());
                summonerTask(mURL);
            }
        }
    }

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
        updateUI(mUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        } else {

        }
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

        @Override
        protected void onPostExecute(JSONObject s) {
            Iterator<String> it = s.keys();
            String iteratorStr;
            while (it.hasNext()) {
                iteratorStr = it.next();

                if (iteratorStr.equals("id")) {
                    try {
                        int id = Integer.parseInt(s.getString(iteratorStr));
                        mAccountID = new Long(id);
                        mRankedURL = api.getRankedStatsURL(mRegion.toLowerCase(), mAccountID);
                        summonerRankTask(mRankedURL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (iteratorStr.equals("profileIconId")) {
                    try {
                        Glide.with(MainActivity.this)
                                .load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/" + s.getString(iteratorStr) + ".png").into(mProfileIcon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (iteratorStr.equals("summonerLevel")) {
                    try {
                        mLevelText.setText("Level: " + s.getString(iteratorStr));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
            for(int i = 0; i < array.length(); ++i){
                try {
                    JSONObject object = array.getJSONObject(i);
                    mRankTextView.setText(object.getString("tier") + " " +object.getString("rank").toString());
                    mWinsTextView.setText(object.getString("wins"));
                    mLossesTextView.setText(object.getString("losses"));
                    mAverageTextView.setText("  " + object.getString("wins") + " || " + object.getString("losses"));
                    setTitle(getString(R.string.tierNameString)+ "  " + object.getString("leagueName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                    }
    }
}

