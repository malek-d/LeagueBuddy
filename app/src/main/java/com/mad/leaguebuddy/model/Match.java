package com.mad.leaguebuddy.model;

/**
 * Created by Malek Darwiche on 7/05/2018.
 * Class which is instantiated for individual Match objects
 * This object is used in the PlayerHistoryActivity
 */
public class Match {
    private String mLane;
    private String mGameId;
    private String mChampId;
    private Long mTimeStamp;
    private String mQueueType;
    private String mRole;
    private String mMapName;

    /**
     * Constructor which takes in various parameters based on the information provided by API
     * @param lane
     * @param gameId
     * @param champId
     * @param timeStamp
     * @param queueType
     * @param role
     */
    public Match(String lane, String gameId, String champId, Long timeStamp, String queueType, String role) {
        mLane = lane;
        mGameId = gameId;
        mChampId = champId;
        mTimeStamp = timeStamp;
        mQueueType = queueType;
        mRole = role;

        queueTypeToName(queueType);
    }

    /**
     * Get the lane of this match in string format
     * @return
     */
    public String getLane() {
        return mLane;
    }

    /**
     * Sets the value of lane based on parameter
     * @param lane
     */
    public void setLane(String lane) {
        mLane = lane;
    }

    /**
     * Gets champion id of champion used in match
     * @return
     */
    public String getChampId() {
        return mChampId;
    }

    /**
     * Retuns the timestamp of this match
     * timestamp is the time from Janurary 1st 1990 till time of game
     * @return
     */
    public Long getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * Returns the type of queue/gamemode this match was
     * @return mQueueType
     */
    public String getQueueType() {
        return mQueueType;
    }

    /**
     * Returns the played role for that match
     * @return
     */
    public String getRole() {
        return mRole;
    }

    /**
     * Returns the map name of which the match was played on
     * @return
     */
    public String getMapName() {
        return mMapName;
    }

    /**
     * This function handles identifying what type of map and game mode was played based on the string
     * queue code provided by the API
     * @param queue
     */
    private void queueTypeToName(String queue){
        switch (queue){
            case "400":
                mMapName = "Summoner's Rift";
                mQueueType = "5v5 Draft Pick";
                break;
            case "420":
                mMapName = "Summoner's Rift";
                mQueueType = "5v5 Ranked Solo";
                break;
            case "430":
                mMapName = "Summoner's Rift";
                mQueueType = "5v5 Blind Pick";
                break;
            case "440":
                mMapName = "Summoner's Rift";
                mQueueType = "5v5 Ranked Flex";
                break;
            case "450":
                mMapName = "Howling Abyss";
                mQueueType = "5v5 ARAM";
                break;
            case "460":
                mQueueType = "3v3 Blind Pick";
                mMapName = "Twisted Treeline";
                break;
            case "470":
                mMapName = "Twisted Treeline";
                mQueueType = "3v3 Ranked Flex";
                break;
            case "100":
                mMapName = "Butcher's Bridge";
                mQueueType = "5v5 ARAM";
                break;
            default:
                mMapName= "SPECIAL MAP";
                mQueueType = "EVENT GAMEMODE";
                break;
        }
    }
}
