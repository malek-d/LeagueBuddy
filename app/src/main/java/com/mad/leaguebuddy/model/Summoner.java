package com.mad.leaguebuddy.model;

/**
 * Created by Maleks on 24-Apr-18.
 */

public class Summoner {
    private String mSummonerName;
    private String mIconID;
    private String mAccountID;
    private String mSummonerID;
    private Long mRevisionDate;
    private String mSummonerLevel;

    public Summoner(String summonerName, String iconID, String accountID, String summonerID, Long revisionDate, String summonerLevel) {
        mSummonerName = summonerName;
        mIconID = iconID;
        mAccountID = accountID;
        mSummonerID = summonerID;
        mRevisionDate = revisionDate;
        mSummonerLevel = summonerLevel;
    }

    public String getSummonerName() {
        return mSummonerName;
    }

    public void setSummonerName(String summonerName) {
        mSummonerName = summonerName;
    }

    public String getIconID() {
        return mIconID;
    }

    public void setIconID(String iconID) {
        mIconID = iconID;
    }

    public String getAccountID() {
        return mAccountID;
    }

    public void setAccountID(String accountID) {
        mAccountID = accountID;
    }

    public String getSummonerID() {
        return mSummonerID;
    }

    public void setSummonerID(String summonerID) {
        mSummonerID = summonerID;
    }

    public Long getRevisionDate() {
        return mRevisionDate;
    }

    public void setRevisionDate(Long revisionDate) {
        mRevisionDate = revisionDate;
    }

    public String getSummonerLevel() {
        return mSummonerLevel;
    }

    public void setSummonerLevel(String summonerLevel) {
        mSummonerLevel = summonerLevel;
    }
}
