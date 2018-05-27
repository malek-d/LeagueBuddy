package com.mad.leaguebuddy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.ChampionInfoHandler;
import com.mad.leaguebuddy.ViewModel.SummonerHandler;
import com.mad.leaguebuddy.ViewModel.UrlFactory;
import com.mad.leaguebuddy.model.Champion;
import com.mad.leaguebuddy.model.Match;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;

/**
 * Created by Maleks on 12-May-18.
 * Adapter class that handles displaying previous matches for the user
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private ArrayList<Match> mMatchArrayList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mLaneImageView, mMatchChampionIconIV;
        TextView mMapNameTextView, mQueueTypeTextView, mGameDateTextView, mMatchChampionNameTV, mLaneTv;


        public ViewHolder(View itemView) {
            super(itemView);
            mLaneImageView = itemView.findViewById(R.id.laneImageView);
            mMatchChampionIconIV = itemView.findViewById(R.id.matchChampionIconIV);
            mMapNameTextView = itemView.findViewById(R.id.mapNameTextView);
            mQueueTypeTextView = itemView.findViewById(R.id.queueTypeTextView);
            mGameDateTextView = itemView.findViewById(R.id.gameDateTextView);
            mMatchChampionNameTV = itemView.findViewById(R.id.matchChampionNameTV);
            mLaneTv = itemView.findViewById(R.id.laneTV);
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

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(match.getTimeStamp());
        SimpleDateFormat dateFormat = new SimpleDateFormat("E d/MMM hh:mm:ss a");
        holder.mGameDateTextView.setText(mContext.getString(R.string.gameDateString) + " " + dateFormat.format(cal.getTime()));

        UrlFactory urlFactory = new UrlFactory();
        SummonerHandler summonerHandler = new SummonerHandler();
        ChampionInfoHandler championInfoHandler = new ChampionInfoHandler(mContext);
        Champion champion = championInfoHandler.getChampionFromJson(match.getChampId());
        holder.mMatchChampionNameTV.setText(champion.getChampionName());
        summonerHandler.glideHelper(mContext, urlFactory.getDdragonChampionUrl(champion.getChampionKey()), R.drawable.poro_question, holder.mMatchChampionIconIV);
        holder.mLaneTv.setText(match.getLane());
    }

    private void setLaneIcon(Match match, ImageView laneImageView) {
        switch(match.getLane()){
            case "BOTTOM":
                laneImageView.setImageResource(R.mipmap.bot_lane_icon);
                break;
            case "MID":
                laneImageView.setImageResource(R.mipmap.mid_lane_icon);
                break;
            case "JUNGLE":
                laneImageView.setImageResource(R.mipmap.jungle_icon);
                break;
            case "TOP":
                laneImageView.setImageResource(R.mipmap.top_lane_icon);
                break;
        }
        if(match.getRole().equals("DUO_SUPPORT")){
            laneImageView.setImageResource(R.mipmap.support_icon);
            match.setLane("SUPPORT");
        }
    }
    @Override
    public int getItemCount() {
        return mMatchArrayList.size();
    }
}
