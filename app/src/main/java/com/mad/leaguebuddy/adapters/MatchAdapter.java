package com.mad.leaguebuddy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.model.Match;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Maleks on 12-May-18.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private ArrayList<Match> mMatchArrayList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mLaneImageView, mMatchChampionIconIV;
        TextView mMapNameTextView, mQueueTypeTextView, mGameDateTextView,  mMatchChampionNameTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mLaneImageView = itemView.findViewById(R.id.laneImageView);
            mMatchChampionIconIV = itemView.findViewById(R.id.matchChampionIconIV);
            mMapNameTextView = itemView.findViewById(R.id.mapNameTextView);
            mQueueTypeTextView = itemView.findViewById(R.id.queueTypeTextView);
            mGameDateTextView = itemView.findViewById(R.id.gameDateTextView);
            mMatchChampionNameTV = itemView.findViewById(R.id.matchChampionNameTV);
        }
    }

    public MatchAdapter(ArrayList<Match> matchArrayList, Context context) {
        mMatchArrayList = matchArrayList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_layout, parent, false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match match = mMatchArrayList.get(position);
        holder.mMapNameTextView.setText(match.getMapName());
        holder.mQueueTypeTextView.setText(match.getQueueType());
        setLaneIcon(match, holder.mLaneImageView);
    }

    private void setLaneIcon(Match match, ImageView laneImageView) {
        switch(match.getLane()){
            case "BOTTOM": laneImageView.setImageResource(R.drawable.bot); break;
            case "MID": laneImageView.setImageResource(R.drawable.mid);break;
            case "JUNGLE": laneImageView.setImageResource(R.drawable.jg);break;
            case "TOP": laneImageView.setImageResource(R.drawable.top);break;
        }
        if(match.getRole().equals("DUO_SUPPORT")){
            laneImageView.setImageResource(R.drawable.sup);
        }
    }

    @Override
    public int getItemCount() {
        return mMatchArrayList.size();
    }


}
