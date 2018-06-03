package com.mad.leaguebuddy.Model;

/**
 * Created by Malek Darwiche on 22/04/2018.
 * Champion object which utilises multiple constructors depending on the how the actual champion
 * information is retrieved
 */
public class Champion {
    private String mChampionLevel,mChampionPoints,
            mChampionID,mChampionPointsNeeded,
            mChampionName, mChampionTitle,
            mChampionKey;

    /**
     * Basic Champion Constructor that takes in 4 parameters
     * @param mChampionLevel
     * @param mChampionPoints
     * @param mChampionID
     * @param mChampionPointsNeeded
     */
    public Champion(String mChampionLevel, String mChampionPoints, String mChampionID, String mChampionPointsNeeded) {
        this.mChampionLevel = mChampionLevel;
        this.mChampionPoints = mChampionPoints;
        this.mChampionID = mChampionID;
        this.mChampionPointsNeeded = mChampionPointsNeeded;
    }

    /**
     * Champion Constructor that takes in ChampionID as only parameter
     * @param championID
     */
    public Champion(String championID){
        mChampionID = championID;
    }

    /**
     * Constructor with no parameter, creates an empty object
     */
    public Champion(){}

    /**
     * Returns ChampionName
     * @return String
     */
    public String getChampionName() {
        return mChampionName;
    }

    /**
     * Returns ChampionTitle
     * @return String
     */
    public String getChampionTitle() {
        return mChampionTitle;
    }

    /**
     * Returns the ChampionKey
     * @return String
     */
    public String getChampionKey() {
        return mChampionKey;
    }

    /**
     * Returns the ChampionLevel
     * @return String
     */
    public String getChampionLevel() {
        return mChampionLevel;
    }

    /**
     * Returns the championPoints for the current champion
     * @return String
     */
    public String getChampionPoints() {
        return mChampionPoints;
    }

    /**
     * Returns the championID for the current Champion
     * @return String
     */
    public String getChampionID() {
        return mChampionID;
    }

    /**
     * Sets the championName based on parameter
     * @param mChampionName
     */
    public void setChampionName(String mChampionName) {
        this.mChampionName = mChampionName;
    }

    /**
     * Sets the ChampionTitle based on parameter
     * @param mChampionTitle
     */
    public void setChampionTitle(String mChampionTitle) {
        this.mChampionTitle = mChampionTitle;
    }

    /**
     * sets the ChampionKey based on parameter
     * @param mChampionKey
     */
    public void setChampionKey(String mChampionKey) {
        this.mChampionKey = mChampionKey;
    }

    /**
     * Sets the championLevel string based on parameter
     * @param mChampionLevel
     */
    public void setChampionLevel(String mChampionLevel) {
        this.mChampionLevel = mChampionLevel;
    }

    /**
     * Sets the championPoints based on parameter
     * @param mChampionPoints
     */
    public void setChampionPoints(String mChampionPoints) {this.mChampionPoints = mChampionPoints;}

    /**
     * Sets championID based on parameter
     * @param mChampionID
     */
    public void setChampionID(String mChampionID) {
        this.mChampionID = mChampionID;
    }
}
