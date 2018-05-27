package com.mad.leaguebuddy.model;

/**
 * Created by Maleks on 24-Apr-18.
 * This class handles all summoner information that is retrieved by the API call
 * this class is predominately used in the PlayerSearchActivity class
 */
public class Summoner {
    private String mSummonerName;
    private String mIconID;
    private String mAccountID;
    private String mSummonerID;
    private Long mRevisionDate;
    private String mSummonerLevel;

    /**
     * Singular constructor for this class
     * This constructor takes in a couple of variables based on the information returned by the API
     * @param summonerName
     * @param iconID
     * @param accountID
     * @param summonerID
     * @param revisionDate
     * @param summonerLevel
     */
    public Summoner(String summonerName, String iconID, String accountID, String summonerID, Long revisionDate, String summonerLevel) {
        mSummonerName = summonerName;
        mIconID = iconID;
        mAccountID = accountID;
        mSummonerID = summonerID;
        mRevisionDate = revisionDate;
        mSummonerLevel = summonerLevel;
    }

    /**
     * Returns summoner name of this object
     * @return
     */
    public String getSummonerName() {
        return mSummonerName;
    }

    /**
     * Sets the summoner name of this object based on parameter
     * @param summonerName
     */
    public void setSummonerName(String summonerName) {
        mSummonerName = summonerName;
    }

    /**
     * returns the iconID of this object to be used in a DDRAGON call for proper image display
     * @return
     */
    public String getIconID() {
        return mIconID;
    }

    /**
     * Returns the last log in date for the searched user
     * In long format because API returns milliseconds from Januray 1st 1990 till last online date
     * @return
     */
    public Long getRevisionDate() {
        return mRevisionDate;
    }

    /**
     * Returns the current summoner level
     * @return
     */
    public String getSummonerLevel() {
        return mSummonerLevel;
    }

}
