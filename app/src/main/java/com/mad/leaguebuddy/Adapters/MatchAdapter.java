package com.mad.leaguebuddy.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.Handlers.ChampionInfoHandler;
import com.mad.leaguebuddy.Handlers.SummonerHandler;
import com.mad.leaguebuddy.Handlers.UrlFactory;
import com.mad.leaguebuddy.Model.Champion;
import com.mad.leaguebuddy.Model.Match;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maleks on 12-May-18.
 * Adapter class that handles displaying previous matches for the user
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private ArrayList<Match> mMatchArrayList;
    private Context mContext;

    /**
     * Inner class which is required by the RecyclerView.Adapter
     * This class holds and instantiates all widgets and views that will be used in this Recycler-
     * view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mLaneImageView, mMatchChampionIconIV;
        TextView mMapNameTextView, mQueueTypeTextView, mGameDateTextView, mMatchChampionNameTV, mLaneTv;

        /**
         * Binds all widgets to their respective view in the xml
         * @param itemView
         */
        private ViewHolder(View itemView) {
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

    /**
     * Constructor of this class, takes in an ArrayList of matches aswell as the Activity context
     * @param matchArrayList An arralist containing Match objects to display within recyclerView
     * @param context The activities context
     */
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

    /**
     * Displays a specific icon based for every match based lane value which then alters the look of
     * laneImageView
     * @param match
     * @param laneImageView
     */
    private void setLaneIcon(Match match, ImageView laneImageView) {
        switch(match.getLane()){
            case "BOTTOM":
                match.setLane(mContext.getString(R.string.botLaneString1));
                laneImageView.setImageResource(R.mipmap.bot_lane_icon);
                break;
            case "MID":
                match.setLane(mContext.getString(R.string.midLaneString));
                laneImageView.setImageResource(R.mipmap.mid_lane_icon);
                break;
            case "JUNGLE":
                match.setLane(mContext.getString(R.string.jungleString));
                laneImageView.setImageResource(R.mipmap.jungle_icon);
                break;
            case "TOP":
                match.setLane(mContext.getString(R.string.topLaneString));
                laneImageView.setImageResource(R.mipmap.top_lane_icon);
                break;
        }
        if(match.getRole().equals("DUO_SUPPORT")){
            laneImageView.setImageResource(R.mipmap.support_icon);
            match.setLane(mContext.getString(R.string.supportString));
        }
    }
    @Override
    public int getItemCount() {
        return mMatchArrayList.size();
    }
}
