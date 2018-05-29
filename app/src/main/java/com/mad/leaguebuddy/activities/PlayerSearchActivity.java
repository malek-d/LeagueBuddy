package com.mad.leaguebuddy.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.RequestHandler;
import com.mad.leaguebuddy.ViewModel.UrlFactory;
import com.mad.leaguebuddy.model.Summoner;
import com.mad.leaguebuddy.adapters.SummonerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This activity allows the user to search up for other users in any other given region
 * Upon searching if a match is found we return that user with basic information displayed in the
 * RecyclerView
 */
public class PlayerSearchActivity extends AppCompatActivity {

    @BindView(R.id.regionSpinner)protected MaterialSpinner mRegionSpinner;
    @BindView(R.id.searchSummonerNameEditText)protected EditText mSummonerName;
    @BindView(R.id.searchResultRV)protected RecyclerView mResultRV;
    @BindView(R.id.progressBar) protected ProgressBar mProgress;

    private ArrayList<Summoner> mSummoners = new ArrayList<>();
    private UrlFactory mUrlFactor = new UrlFactory();
    private String mRegion;
    private SummonerAdapter mAdapter;
    private String mName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mAdapter = new SummonerAdapter(mSummoners, PlayerSearchActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mResultRV.setLayoutManager(layoutManager);
        mResultRV.setItemAnimator(new DefaultItemAnimator());
        mResultRV.setAdapter(mAdapter);

        mRegionSpinner.setItems(getResources().getStringArray(R.array.regions));
        mRegionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mRegion = mUrlFactor.returnRegion(item);
            }
        });
    }

    /**
     * This method is called from an onClick by the search button in the xml
     * Converts the inputted text field into a string then calls upon the asynctask for the search
     * function to commence
     * @param view
     */
    public void commenceSearch(View view) {
        mName = mSummonerName.getText().toString();
        new searchForSummonerTask().execute();
    }

    /**
     * This Asynctask calls upon the user WEB API where a string summoner name is provider
     * and user information is returned if a match is found
     * We create a Summoner object then which is fed into the adapter
     */
    private class searchForSummonerTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
            mResultRV.setVisibility(View.INVISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            JSONObject jsonObject = requestHandler.RequestHandlerAsJsonObject(mUrlFactor.getSummonerURL(mName, mRegion));
            return  jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if(jsonObject == null){
                Toasty.error(PlayerSearchActivity.this, "Summoner does not exist!", Toast.LENGTH_SHORT).show();
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

                mSummoners.add(0, summoner);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
