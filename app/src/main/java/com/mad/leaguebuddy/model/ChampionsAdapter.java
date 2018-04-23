package com.mad.leaguebuddy.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.leaguebuddy.MainActivity;
import com.mad.leaguebuddy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Malek Darwiche on 22/04/2018.
 */

public class ChampionsAdapter extends RecyclerView.Adapter<ChampionsAdapter.MyViewHolder> {

    private ArrayList<Champion> championsList;
    private Context mContext;
    private String mRegion;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mChampionName, mChampionLevel, mChampionPoints;
        private ImageView mChampionIcon;
        private ProgressBar mProgressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            mChampionName = itemView.findViewById(R.id.championName);
            mChampionIcon = itemView.findViewById(R.id.championIcon);
            mChampionLevel = itemView.findViewById(R.id.championLevel);
            mChampionPoints = itemView.findViewById(R.id.championPoints);
            mProgressBar = itemView.findViewById(R.id.itemProgressBar);

        }
    }


    public ChampionsAdapter(ArrayList<Champion> championsList, Context context, String region) {
        this.championsList = championsList;
        this.mContext = context;
        this.mRegion = region;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.champion_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Champion champion = championsList.get(position);
        holder.mChampionLevel.setText(mContext.getString(R.string.masteryLevelString) + " " + champion.getChampionLevel());
        holder.mChampionPoints.setText(mContext.getString(R.string.pointsString) + " " + champion.getChampionPoints());
        championInfoTask(position, holder.mChampionName, holder.mChampionIcon, holder.mProgressBar);
    }

    private void championInfoTask(int position, TextView champName, ImageView champIcon, ProgressBar progressBar) {
        new getChampionInfoTask(position, champName, champIcon, progressBar).execute();
    }

    @Override
    public int getItemCount() {
        return championsList.size();
    }


    private class getChampionInfoTask extends AsyncTask<Void, Void, String> {
        private Champion mChamp;
        private TextView championNameTV; //TV = TextView
        private ImageView championIconIV; //IV = ImageView
        int championArrayPosition;
        private ProgressBar progressBar;

        public getChampionInfoTask(int position, TextView champName, ImageView champIcon, ProgressBar progress) {
            championNameTV = champName;
            championArrayPosition = position;
            championIconIV = champIcon;
            progressBar = progress;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String obj = loadJSONFromAsset();
            return obj;
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            try {
                mChamp = championsList.get(championArrayPosition);
                JSONObject jo = new JSONObject(jsonObject);
                JSONObject champion = jo.getJSONObject(mChamp.getChampionID());
                mChamp.setChampionKey(champion.getString("key"));
                mChamp.setChampionName(champion.getString("name"));
                mChamp.setChampionTitle(champion.getString("title"));
                championNameTV.setText(mChamp.getChampionName() + "\n" + mChamp.getChampionTitle());
                Glide.with(mContext)
                        .load("https://ddragon.leagueoflegends.com/cdn/8.8.1/img/champion/" + mChamp.getChampionKey() + ".png")
                        .into(championIconIV);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);

        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("champions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
