package com.mad.leaguebuddy.ViewModel;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mad.leaguebuddy.R;

/**
 * Created by Maleks on 12-May-18.
 * This class is a helper class of sorts that aids with displaying proper images and user information
 * based on provided information for functions
 */
public class SummonerHandler {

    /**
     * Empty constructor to create an empty object
     */
    public SummonerHandler(){}

    /**
     * Based on the provided tier string we check what string equals to and set mRankIcon based on that
     * @param tier
     * @param mRankIcon
     */
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

    /**
     * Glide helper function that can be used by any activity when provided with the correct parameters
     * @param context
     * @param url
     * @param resourceId
     * @param iv
     */
    public void glideHelper(Context context, String url, int resourceId, ImageView iv){
        Glide.with(context)
                .load(url)
                .placeholder(resourceId)
                .into(iv);
    }
}
