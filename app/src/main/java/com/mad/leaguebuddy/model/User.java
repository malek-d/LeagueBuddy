package com.mad.leaguebuddy.model;


/**
 * Created by Maleks on 21-Apr-18.
 */

public class User {
    String mSummonerName;
    String mRegion;

    public User(){}

    public User(String name, String region){
        mSummonerName = name;
        mRegion = region;
    }

    public String getSummonerName() {
        return mSummonerName;
    }

    public void setSummonerName(String mSummonerName) {
        this.mSummonerName = mSummonerName;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String mRegion) {
        this.mRegion = mRegion;
    }
}
