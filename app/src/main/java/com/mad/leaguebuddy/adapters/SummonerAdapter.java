package com.mad.leaguebuddy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.SummonerHandler;
import com.mad.leaguebuddy.ViewModel.UrlFactory;
import com.mad.leaguebuddy.model.Summoner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maleks on 05-May-18.
 */

public class SummonerAdapter extends RecyclerView.Adapter<SummonerAdapter.ViewHolder> {
    private UrlFactory mUrlFactory = new UrlFactory();
    private ArrayList<Summoner> mSummoners;
    private Context mContext;

    public SummonerAdapter(ArrayList<Summoner> summoners, Context context) {
        mSummoners = summoners;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mSummonerName, mSummonerLevel, mLastOnline;

        public ViewHolder(View view) {
            super(view);
            mIcon = view.findViewById(R.id.summonerIcon);
            mSummonerLevel = view.findViewById(R.id.summonerLevel);
            mSummonerName = view.findViewById(R.id.summonerName);
            mLastOnline = view.findViewById(R.id.searchLastOnlineTV);
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

        Long i = new Long(summoner.getRevisionDate());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(i);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E d/MMM hh:mm:ss a");

        holder.mLastOnline.setText(mContext.getString(R.string.lastOnlineString) + " " + dateFormat.format(cal.getTime()));
        SummonerHandler summonerHandler = new SummonerHandler();
        summonerHandler.glideHelper(mContext,mUrlFactory.getDdragonImageUrl(summoner.getIconID()),
                R.drawable.poro_question, holder.mIcon);
    }

    @Override
    public int getItemCount() {
        return mSummoners.size();
    }
}
