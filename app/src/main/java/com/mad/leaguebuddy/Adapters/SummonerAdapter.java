package com.mad.leaguebuddy.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.Handlers.SummonerHandler;
import com.mad.leaguebuddy.Handlers.UrlFactory;
import com.mad.leaguebuddy.Model.Summoner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maleks on 05-May-18.
 * This adapter is used within the PlayerSearchActivity and is used to properly display searched
 * users within the RecyclerView
 */

public class SummonerAdapter extends RecyclerView.Adapter<SummonerAdapter.ViewHolder> {
    private UrlFactory mUrlFactory = new UrlFactory();
    private ArrayList<Summoner> mSummoners;
    private Context mContext;

    /**
     * Constructor for this class, takes in an ArrayList of Summoners, activity context as well as
     * the region which is used later on with the AsyncTask
     * @param summoners
     * @param context
     */
    public SummonerAdapter(ArrayList<Summoner> summoners, Context context) {
        mSummoners = summoners;
        mContext = context;
    }

    /**
     * Inherited/Required class due to extending RecyclerView.Adapter
     * Holds all our UI and widgets to be used later on
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mSummonerName, mSummonerLevel, mLastOnline;

        /**
         * Holds all our widgets/views that will be used in this instance of the adapter
         * @param view
         */
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
