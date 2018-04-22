package com.mad.leaguebuddy.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.leaguebuddy.MainActivity;
import com.mad.leaguebuddy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private LinearLayout mChampionLayout;
    private API api = new API();
    private String mRegion, mChampionName, mChampion, mChampionKey, mChampionTitle;
    private OkHttpClient mClient = new OkHttpClient();
    private boolean mBoolean = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mChampionName, mChampionLevel, mChampionPoints;
        private ImageView mChampionIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            mChampionName = itemView.findViewById(R.id.championName);
            mChampionIcon = itemView.findViewById(R.id.championIcon);
            mChampionLevel = itemView.findViewById(R.id.championLevel);
            mChampionLayout = itemView.findViewById(R.id.championLayout);
            mChampionPoints = itemView.findViewById(R.id.championPoints);

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
        holder.mChampionLevel.setText(mContext.getString(R.string.masteryLevelString) + " " +champion.getChampionLevel());
        holder.mChampionPoints.setText("Points needed: " + " " + champion.getChampionPointsNeeded());

        String url = api.getChampionInfoUrl(champion.getChampionID(), mRegion);
        championInfoTask(url, champion);
        while(!mBoolean){}
        holder.mChampionName.setText(champion.getChampionName() + "\n" + champion.getChampionTitle());
        Glide.with(mContext)
                .load("https://ddragon.leagueoflegends.com/cdn/8.8.1/img/champion/" + champion.getChampionKey() +".png")
                .into(holder.mChampionIcon);
    }

    private void championInfoTask(String url, Champion champ) {
        new getChampionInfoTask(url, champ).execute();
    }

    @Override
    public int getItemCount() {
        return championsList.size();
    }

    private class getChampionInfoTask extends AsyncTask<Void, Void, Void>{
        private String mURL;
        private Champion champ;

        public getChampionInfoTask(String url, Champion champion) {
            mURL = url;
            champ = champion;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(mURL)
                    .build();
            JSONObject object;
            try {
                Response response = mClient.newCall(request).execute();
                String jsonData = response.body().string();
                object = new JSONObject(jsonData);
                Iterator<String> it = object.keys();
                String iteratorString;
                while(it.hasNext()){
                    iteratorString = it.next();
                    switch (iteratorString){
                        case "name" :
                            champ.setChampionName(object.getString(iteratorString));
                            champ.setChampionTitle(object.getString("title"));
                            break;
                        case "key":
                            champ.setChampionKey(object.getString(iteratorString));
                            break;
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mBoolean = true;
            return null;
        }
    }
}
