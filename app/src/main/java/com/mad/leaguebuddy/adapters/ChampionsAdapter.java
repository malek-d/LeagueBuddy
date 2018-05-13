package com.mad.leaguebuddy.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.ChampionInfoHandler;
import com.mad.leaguebuddy.ViewModel.SummonerHandler;
import com.mad.leaguebuddy.ViewModel.UrlFactory;
import com.mad.leaguebuddy.model.Champion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Malek Darwiche on 22/04/2018.
 */

public class ChampionsAdapter extends RecyclerView.Adapter<ChampionsAdapter.MyViewHolder> {

    private ArrayList<Champion> mChampList;
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


    public ChampionsAdapter(ArrayList<Champion> mChampList, Context context, String region) {
        this.mChampList = mChampList;
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
        Champion champion = mChampList.get(position);
        holder.mChampionLevel.setText(mContext.getString(R.string.masteryLevelString) + " " + champion.getChampionLevel());
        holder.mChampionPoints.setText(mContext.getString(R.string.pointsString) + " " + champion.getChampionPoints());
        championInfoTask(position, holder.mChampionName, holder.mChampionIcon, holder.mProgressBar);
    }

    private void championInfoTask(int position, TextView champName, ImageView champIcon, ProgressBar progressBar) {
        new getChampionInfoTask(position, champName, champIcon, progressBar).execute();
    }

    @Override
    public int getItemCount() {
        return mChampList.size();
    }


    private class getChampionInfoTask extends AsyncTask<Void, Void, String> {
        private Champion mChamp;
        private TextView championNameTV;
        private ImageView championIconIV;
        private int mChampionArrayPosition;
        private ProgressBar progressBar;

        public getChampionInfoTask(int position, TextView champName, ImageView champIcon, ProgressBar progress) {
            championNameTV = champName;
            mChampionArrayPosition = position;
            championIconIV = champIcon;
            progressBar = progress;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            ChampionInfoHandler handler = new ChampionInfoHandler(mContext);
            return handler.loadJSONFromAsset(mContext);
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            try {
                mChamp = mChampList.get(mChampionArrayPosition);
                JSONObject jo = new JSONObject(jsonObject);
                JSONObject champion = jo.getJSONObject(mChamp.getChampionID());
                mChamp.setChampionKey(champion.getString("key"));
                mChamp.setChampionName(champion.getString("name"));
                mChamp.setChampionTitle(champion.getString("title"));
                championNameTV.setText(mChamp.getChampionName() + "\n" + mChamp.getChampionTitle());
                SummonerHandler summonerHandler = new SummonerHandler();
                UrlFactory UrlFactory = new UrlFactory();
                summonerHandler.glideHelper(mContext, UrlFactory.getDdragonChampionUrl(mChamp.getChampionKey()), 0 , championIconIV );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

}
