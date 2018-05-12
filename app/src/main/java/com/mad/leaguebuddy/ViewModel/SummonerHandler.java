package com.mad.leaguebuddy.ViewModel;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mad.leaguebuddy.R;

/**
 * Created by Maleks on 12-May-18.
 */

public class SummonerHandler {
    public SummonerHandler(){}

    public void setRankIcon(String tier, ImageView mRankIcon) {
        switch (tier) {
            case "BRONZE":
                mRankIcon.setImageResource(R.drawable.bronze_rank_icon);
                break;
            case "SILVER":
                mRankIcon.setImageResource(R.drawable.silver_rank_icon);
                break;
            case "GOLD":
                mRankIcon.setImageResource(R.drawable.gold_rank_icon);
                break;
            case "PLATINUM":
                mRankIcon.setImageResource(R.drawable.platinum_rank_icon);
                break;
            case "DIAMOND":
                mRankIcon.setImageResource(R.drawable.platinum_rank_icon);
                break;
            case "MASTER":
                mRankIcon.setImageResource(R.drawable.master_rank_icon);
                break;
            case "CHALLENGER":
                mRankIcon.setImageResource(R.drawable.challenger_rank_icon);
                break;
        }
    }

    public void glideHelper(Context context, String url, int resourceId, ImageView iv){
        Glide.with(context).load(url).placeholder(resourceId).into(iv);
    }


}
