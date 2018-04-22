package com.mad.leaguebuddy.model;

/**
 * Created by Malek Darwiche on 22/04/2018.
 */

public class Champion {
    private String mChampionLevel,mChampionPoints, mChampionID,mChampionPointsNeeded, mChampionName, mChampionTitle, mChampionKey;

    public String getChampionName() {
        return mChampionName;
    }

    public void setChampionName(String mChampionName) {
        this.mChampionName = mChampionName;
    }

    public String getChampionTitle() {
        return mChampionTitle;
    }

    public void setChampionTitle(String mChampionTitle) {
        this.mChampionTitle = mChampionTitle;
    }

    public String getChampionKey() {
        return mChampionKey;
    }

    public void setChampionKey(String mChampionKey) {
        this.mChampionKey = mChampionKey;
    }

    public Champion(String mChampionLevel, String mChampionPoints, String mChampionID, String mChampionPointsNeeded) {
        this.mChampionLevel = mChampionLevel;
        this.mChampionPoints = mChampionPoints;
        this.mChampionID = mChampionID;
        this.mChampionPointsNeeded = mChampionPointsNeeded;
    }

    public String getChampionLevel() {
        return mChampionLevel;
    }

    public void setChampionLevel(String mChampionLevel) {
        this.mChampionLevel = mChampionLevel;
    }

    public String getChampionPoints() {
        return mChampionPoints;
    }

    public void setChampionPoints(String mChampionPoints) {
        this.mChampionPoints = mChampionPoints;
    }

    public String getChampionID() {
        return mChampionID;
    }

    public void setChampionID(String mChampionID) {
        this.mChampionID = mChampionID;
    }

    public String getChampionPointsNeeded() {
        return mChampionPointsNeeded;
    }

    public void setChampionPointsNeeded(String mChampionPointsNeeded) {
        this.mChampionPointsNeeded = mChampionPointsNeeded;
    }
}
