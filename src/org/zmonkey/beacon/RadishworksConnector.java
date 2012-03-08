package org.zmonkey.beacon;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: corey
 * Date: 3/6/12
 * Time: 3:48 PM
 */
public class RadishworksConnector {

    public static final int APIKEY_LENGTH = 33;
    public static final String FIELD_DELIMITER = "\n";
    public static final int REQUEST_LIST_MISSIONS = 0;
    public static final int REQUEST_TEAM_NUMER = 1;
    public static final int REQUEST_TEAM_MEMBERS = 2;
    public static final int REQUEST_TEAM_TYPE = 3;
    public static final int REQUEST_TEAM_OBJECTIVES = 4;
    public static final int REQUEST_TEAM_NOTES = 5;
    public static final int REQUEST_POST_LOCATION = 6;
    public static final int REQUEST_POST_CLUE = 7;
    public static final String API_BASE = "https://www.radishworks.com/SearchManager/api.php?";
    public static final String API_APIKEY = "APIKey=";
    public static final String API_MISSIONID = "MissionID=";
    public static final String API_LATITUDE = "Latitude=";
    public static final String API_LONGITUDE = "Longitude=";
    public static final String API_CLUE_NAME = "ClueName=";
    public static final String API_CLUE_DESCRIPTION = "ClueDescription=";
    public static final String API_CLUE_FOUNDBY = "ClueFoundBy=";
    public static final String API_CLUE_LOCATION = "ClueLocation=";
    public static final String API_CLUETIME = "ClueTime=";
    public static final String API_CLUEDATE = "ClueDate=";
    public static final String[] API_REQUESTS = {"Get=MissionList", "Get=TeamNumber", "Get=TeamMembers", "Get=TeamType",
            "Get=TeamObjectives", "Get=TeamNotes", "Post=Location", "Post=Clue"
    };

    public static void apiCall(int requestId, Context context, Handler h){
        apiCall(requestId, context, h, null);
    }
    
    public static void apiCall(int requestId, Context context, Handler h, String parameters){
        try	{
            String uri = API_BASE + API_APIKEY +
                    PreferenceManager.getDefaultSharedPreferences(context).getString("apikey", "").toString() +
                    "&" + API_REQUESTS[requestId];
            if (parameters != null){
                uri = uri + "&" + parameters;
            }
            switch(requestId){
                case REQUEST_TEAM_NUMER:
                case REQUEST_TEAM_MEMBERS:
                case REQUEST_TEAM_OBJECTIVES:
                case REQUEST_TEAM_NOTES:
                case REQUEST_TEAM_TYPE:
                case REQUEST_POST_LOCATION:
                case REQUEST_POST_CLUE:
                    uri = uri + "&" + API_MISSIONID + Integer.toString(MainActivity.mission.number);
                    break;
            }
            //Toast.makeText(getApplicationContext(), uri, Toast.LENGTH_SHORT).show();
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                Message lmsg;
                lmsg = new Message();
                lmsg.obj = line;
                lmsg.what = requestId;
                h.sendMessage(lmsg);
            }
        }
        catch (Exception e)	{
        }
    }

}
