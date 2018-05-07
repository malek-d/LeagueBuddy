package com.mad.leaguebuddy.model;

/**
 * Created by Malek Darwiche on 7/05/2018.
 */

public class Match {
    private String mLane;
    private String mGameId;
    private String mChampId;
    private Long mTimeStamp;
    private String mQueueType;
    private String mRole;
    private String mMapName;

    public Match(String lane, String gameId, String champId, Long timeStamp, String queueType, String role) {
        mLane = lane;
        mGameId = gameId;
        mChampId = champId;
        mTimeStamp = timeStamp;
        mQueueType = queueType;
        mRole = role;
    }

    public String getLane() {
        return mLane;
    }

    public void setLane(String lane) {
        mLane = lane;
    }

    public String getGameId() {
        return mGameId;
    }

    public void setGameId(String gameId) {
        mGameId = gameId;
    }

    public String getChampId() {
        return mChampId;
    }

    public void setChampId(String champId) {
        mChampId = champId;
    }

    public Long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public String getQueueType() {
        return mQueueType;
    }

    public void setQueueType(String queueType) {
        mQueueType = queueType;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        mRole = role;
    }

    public String getMapName() {
        return mMapName;
    }

    public void setMapName(String mapName) {
        mMapName = mapName;
    }

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
