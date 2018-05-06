package com.mad.leaguebuddy.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.data.urlFactory;
import com.mad.leaguebuddy.model.Summoner;
import com.mad.leaguebuddy.model.SummonerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerSearchActivity extends AppCompatActivity {

    private MaterialSpinner regionSpinner;
    private EditText mSummonerName;
    private RecyclerView mResultRV;
    private ProgressBar mProgress;
    private ArrayList<Summoner> mSummoners = new ArrayList<>();
    private urlFactory mUrlFactor = new urlFactory();
    private String mRegion;
    private SummonerAdapter mAdapter;
    private String mName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regionSpinner = findViewById(R.id.regionSpinner);
        mSummonerName = findViewById(R.id.searchSummonerNameEditText);
        mResultRV = findViewById(R.id.searchResultRV);
        mProgress = findViewById(R.id.progressBar);
        String regionsTemp[] = getResources().getStringArray(R.array.regions);

        mAdapter = new SummonerAdapter(mSummoners, PlayerSearchActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mResultRV.setLayoutManager(layoutManager);
        mResultRV.setItemAnimator(new DefaultItemAnimator());
        mResultRV.setAdapter(mAdapter);

        regionSpinner.setItems(getResources().getStringArray(R.array.regions));
        mRegion = mUrlFactor.returnRegion(regionsTemp[0]);
        regionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mRegion = item;
            }
        });
    }

    public void commenceSearch(View view) {
        mName = mSummonerName.getText().toString();
        new searchForSummonerTask().execute();
    }

    private class searchForSummonerTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
            mResultRV.setVisibility(View.INVISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(mUrlFactor.getSummonerURL(mName, mRegion))
                    .build();
            OkHttpClient client = new OkHttpClient();
            Response response;
            try{
                response = client.newCall(request).execute();
                String jsonData = response.body().string();
                return new JSONObject(jsonData);
            } catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if(jsonObject == null){
                //TODO add better exception handling here
                Log.d("TAG", "no user returned");
            }
            try{
                String sumName = jsonObject.getString("name");
                String sumLevel = jsonObject.getString("summonerLevel");
                String accountId = jsonObject.getString("accountId");
                String sumId = jsonObject.getString("id");
                String iconId = jsonObject.getString("profileIconId");
                String revisionDateString = jsonObject.getString("revisionDate");
                Long revisionDate = Long.parseLong(revisionDateString);
                Summoner summoner = new Summoner(sumName, iconId, accountId, sumId,revisionDate, sumLevel);

                mSummoners.add(summoner);
                mAdapter.notifyDataSetChanged();
            } catch(JSONException e){
                e.printStackTrace();
            } catch(NullPointerException e){
                Toasty.error(PlayerSearchActivity.this, "Summoner does not exist!", Toast.LENGTH_SHORT).show();
            }
            mProgress.setVisibility(View.INVISIBLE);
            mResultRV.setVisibility(View.VISIBLE);

        }
    }
}
