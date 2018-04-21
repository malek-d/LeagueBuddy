package com.mad.leaguebuddy.model;

/**
 * Created by Maleks on 21-Apr-18.
 */

public class API {
    private String mHttps = "https://";
    private String mUrl = ".api.riotgames.com/lol/";
    private String summonerURL = "summoner/v3/summoners/by-name/";
    private String mAPIKey = "?api_key=RGAPI-0f76286c-8a5e-41db-ace9-f14f2b98e7a0";
    private String leagueURL = "league/v3/positions/by-summoner/";

    public API(){}

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

    public String getSummonerURL(String summonerName, String region){
        return mHttps + region + mUrl + summonerURL + summonerName + mAPIKey;
    }

    public String getRankedStatsURL(String region, Long accountID){
        return mHttps + region + mUrl + leagueURL + accountID  + mAPIKey;
    }
}
