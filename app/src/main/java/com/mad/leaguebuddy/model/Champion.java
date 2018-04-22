package com.mad.leaguebuddy.model;

/**
 * Created by Malek Darwiche on 22/04/2018.
 */

public class Champion {
    private String mChampionLevel,mChampionPoints,
            mChampionID,mChampionPointsNeeded,
            mChampionName, mChampionTitle,
            mChampionKey;

    public Champion(String mChampionLevel, String mChampionPoints, String mChampionID, String mChampionPointsNeeded) {
        this.mChampionLevel = mChampionLevel;
        this.mChampionPoints = mChampionPoints;
        this.mChampionID = mChampionID;
        this.mChampionPointsNeeded = mChampionPointsNeeded;
    }


    public String getChampionName() {
        return mChampionName;
    }
    public String getChampionTitle() {
        return mChampionTitle;
    }
    public String getChampionKey() {
        return mChampionKey;
    }
    public String getChampionLevel() {
        return mChampionLevel;
    }
    public String getChampionPoints() {
        return mChampionPoints;
    }
    public String getChampionID() {
        return mChampionID;
    }
    public String getChampionPointsNeeded() {
        return mChampionPointsNeeded;
    }


    public void setChampionName(String mChampionName) {
        this.mChampionName = mChampionName;
    }
    public void setChampionTitle(String mChampionTitle) {
        this.mChampionTitle = mChampionTitle;
    }
    public void setChampionKey(String mChampionKey) {
        this.mChampionKey = mChampionKey;
    }
    public void setChampionLevel(String mChampionLevel) {
        this.mChampionLevel = mChampionLevel;
    }
    public void setChampionPoints(String mChampionPoints) {
        this.mChampionPoints = mChampionPoints;
    }
    public void setChampionID(String mChampionID) {
        this.mChampionID = mChampionID;
    }
    public void setChampionPointsNeeded(String mChampionPointsNeeded) {
        this.mChampionPointsNeeded = mChampionPointsNeeded;
    }
}
