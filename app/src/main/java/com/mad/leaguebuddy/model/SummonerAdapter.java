package com.mad.leaguebuddy.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.data.urlFactory;

import java.util.ArrayList;

/**
 * Created by Maleks on 05-May-18.
 */

public class SummonerAdapter extends RecyclerView.Adapter<SummonerAdapter.ViewHolder> {
    private urlFactory mUrlFactory = new urlFactory();
    private ArrayList<Summoner> mSummoners;
    private Context mContext;

    public SummonerAdapter(ArrayList<Summoner> summoners, Context context) {
        mSummoners = summoners;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mSummonerName, mSummonerLevel;

        public ViewHolder(View view) {
            super(view);
            mIcon = view.findViewById(R.id.summonerIcon);
            mSummonerLevel = view.findViewById(R.id.summonerLevel);
            mSummonerName = view.findViewById(R.id.summonerName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summoner_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Summoner summoner = mSummoners.get(position);
        holder.mSummonerName.setText(summoner.getSummonerName());
        holder.mSummonerLevel.setText(mContext.getResources().getString(R.string.levelString)+ " "  + summoner.getSummonerLevel());

        Glide.with(mContext).load(mUrlFactory.getDdragonImageUrl(summoner.getIconID()))
                .placeholder(R.drawable.poro_question)
                .into(holder.mIcon);
    }

    @Override
    public int getItemCount() {
        return mSummoners.size();
    }
}
