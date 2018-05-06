package com.mad.leaguebuddy.data;

/**
 * Created by Maleks on 21-Apr-18.
 */

public class urlFactory {
    private static final String mHttps = "https://";
    private static final String mUrl = ".api.riotgames.com/lol/";
    private static final String summonerURL = "summoner/v3/summoners/by-name/";
    private static final String mAPIKey = "?api_key=RGAPI-0f76286c-8a5e-41db-ace9-f14f2b98e7a0";
    private static final String leagueURL = "league/v3/positions/by-summoner/";
    private static final String CHAMPION_MASTERY_URL = "champion-mastery/v3/champion-masteries/by-summoner/";
    private static final String CHAMPION_INFO_URL = "static-data/v3/champions/";
    private static final String DDRAGON_IMAGE_URL = "http://ddragon.leagueoflegends.com/cdn/8.8.1/img/profileicon/";
    private static final String PNG_FORMAT = ".png";

    public urlFactory(){}

    public String returnRegion(String region){
        switch (region){
            case "OCE": return "oc1";
            case "RU" : return "ru";
            case "KR" : return "kr";
            case "BR" : return "br1";
            case "JP" : return "jp1";
            case "NA" : return "na";
            case "EUN" : return "eun1";
            case  "EUW" : return  "euw1";
            case "TR" : return "tr1";
            case "LA" : return "la1";
        }
        return null;
    }

    public String getDdragonImageUrl(String iconId){
        return DDRAGON_IMAGE_URL + iconId + PNG_FORMAT;
    }

    public String getChampionMasteryUrl(String summonerID, String region){
        return mHttps + region + mUrl + CHAMPION_MASTERY_URL + summonerID + mAPIKey;
    }

    public String getChampionInfoUrl(String champID, String region){
        return mHttps + region + mUrl + CHAMPION_INFO_URL + champID + mAPIKey;
    }

    public String getSummonerURL(String summonerName, String region){
        return mHttps + region + mUrl + summonerURL + summonerName + mAPIKey;
    }

    public String getRankedStatsURL(String region, Long accountID){
        return mHttps + region + mUrl + leagueURL + accountID  + mAPIKey;
    }
}
