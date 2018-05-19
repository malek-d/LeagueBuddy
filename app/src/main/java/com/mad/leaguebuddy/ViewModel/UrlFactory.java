package com.mad.leaguebuddy.ViewModel;

/**
 * Created by Maleks on 21-Apr-18.
 */

/**
 * Backbone handler class for this project which constructs and provides all web services URLs
 * needed to make adequate calls for data to be displayed
 * all strings are static final as they just need to be provided, not changed
 */
public class UrlFactory {
    private static final String HTTPS = "https://";
    private static final String RIOT_URL = ".api.riotgames.com/lol/";
    private static final String SUMMONER_URL = "summoner/v3/summoners/by-name/";
    private static final String API_KEY = "?api_key=RGAPI-0f76286c-8a5e-41db-ace9-f14f2b98e7a0";
    private static final String LEAGUE_URL = "league/v3/positions/by-summoner/";
    private static final String CHAMPION_MASTERY_URL = "champion-mastery/v3/champion-masteries/by-summoner/";
    private static final String DDRAGON_IMAGE_URL = "http://ddragon.leagueoflegends.com/cdn/8.10.1/img/profileicon/";
    private static final String PNG_FORMAT = ".png";
    private static final String MATCH_URL = "match/v3/matchlists/by-account/";
    private static final String DDRAGON_CHAMPION_URL = "ddragon.leagueoflegends.com/cdn/8.10.1/img/champion/";

    public UrlFactory(){}

    public String returnRegion(String region){
        switch (region){
            case "OCE": return "oc1";
            case "RU" : return "ru";
            case "KR" : return "kr";
            case "BR" : return "br1";
            case "JP" : return "jp1";
            case "NA" : return "na1";
            case "EUN" : return "eun1";
            case  "EUW" : return  "euw1";
            case "TR" : return "tr1";
            case "LA" : return "la1";
        }
        return null;
    }

    public String getDdragonChampionUrl(String champId){
        return HTTPS + DDRAGON_CHAMPION_URL + champId +  PNG_FORMAT;
    }

    public String getMatchUrl(String region, String accountId){
        return HTTPS + region + RIOT_URL + MATCH_URL + accountId + API_KEY;
    }

    public String getDdragonImageUrl(String iconId){
        return DDRAGON_IMAGE_URL + iconId + PNG_FORMAT;
    }

    public String getChampionMasteryUrl(String summonerID, String region){
        return HTTPS + region + RIOT_URL + CHAMPION_MASTERY_URL + summonerID + API_KEY;
    }

    public String getSummonerURL(String summonerName, String region){
        return HTTPS + region + RIOT_URL + SUMMONER_URL + summonerName + API_KEY;
    }

    public String getRankedStatsURL(String region, Long accountID){
        return HTTPS + region + RIOT_URL + LEAGUE_URL + accountID  + API_KEY;
    }
}
