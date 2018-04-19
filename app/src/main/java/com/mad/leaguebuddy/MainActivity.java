package com.mad.leaguebuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final int RESULT_REGISTER = 999;


    public final static int REQUEST_CODE = 0;
    private static final String TAG = "";
    public static final String USER_KEY = "";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mTextMessage;

    //private String mUrl = "https://oc1.api.riotgames.com/lol/summoner/v3/summoners/by-name/";
    //private String mKey = "RGAPI-9323199c-92b9-49f4-ade1-124160937f82";
    private OkHttpClient mClient = new OkHttpClient();
    private FirebaseUser mUser;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final Button button = findViewById(R.id.getSummonerBtn);
        Intent intent = getIntent();
        mUser = (FirebaseUser) intent.getExtras().get(USER_KEY);
        Toasty.success(MainActivity.this, getString(R.string.loggedinMessage)+ " " + mUser.getEmail()).show();
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

    class getSummonerTask extends AsyncTask<Void, Void, String> {
        String url;

        public getSummonerTask(String string) {
            url = string;
        }

        @Override
        protected String doInBackground(Void... strings) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = mClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mTextMessage.setText(s);
        }
    }
}
