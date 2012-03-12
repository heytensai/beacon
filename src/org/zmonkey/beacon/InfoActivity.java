package org.zmonkey.beacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

/**
 * User: corey
 * Date: 3/2/12
 * Time: 8:54 PM
 */
public class InfoActivity extends Activity {
    private Handler h;
    public static InfoActivity info;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        setupCallbackHandler();

        info = this;
    }

    public void loadMissionDetails(){
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_NUMER, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_MEMBERS, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_TYPE, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_OBJECTIVES, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_NOTES, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_MISSION_DESC, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_CMD_NAME, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_CMD_LOCATION, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_CMD_GPS, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_RADIO_COMMAND, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_RADIO_TACTICAL, this, h);
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), RadishworksConnector.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                String failure = RadishworksConnector.apiFailure((String) msg.obj);
                if (failure != null){
                    Toast.makeText(getApplicationContext(), RadishworksConnector.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (msg.what) {
                    case RadishworksConnector.REQUEST_TEAM_NUMER:
                    {
                        MainActivity.team.number = Integer.parseInt((String) msg.obj);
                    }
                    break;
                    case RadishworksConnector.REQUEST_TEAM_MEMBERS:
                    {
                        TextView t = (TextView) findViewById(R.id.teamMembers);
                        String s = (String) msg.obj;
                        s = s.replaceAll(",", "\n");
                        t.setText(s);
                    }
                    break;
                    case RadishworksConnector.REQUEST_TEAM_TYPE:
                    {
                        MainActivity.team.type = (String) msg.obj;
                    }
                    break;
                    case RadishworksConnector.REQUEST_TEAM_OBJECTIVES:
                    {
                        TextView t = (TextView) findViewById(R.id.teamObjectives);
                        t.setText((String) msg.obj);
                    }
                    break;
                    case RadishworksConnector.REQUEST_TEAM_NOTES:
                    {
                        TextView t = (TextView) findViewById(R.id.teamNotes);
                        t.setText((String) msg.obj);
                    }
                    case RadishworksConnector.REQUEST_MISSION_DESC:
                    {
                        TextView t = (TextView) findViewById(R.id.missionDescription);
                        t.setText((String) msg.obj);
                    }
                    case RadishworksConnector.REQUEST_CMD_NAME:
                    {
                        TextView t = (TextView) findViewById(R.id.commandPostName);
                        t.setText((String) msg.obj);
                    }
                    case RadishworksConnector.REQUEST_CMD_LOCATION:
                    {
                        TextView t = (TextView) findViewById(R.id.commandPostLocation);
                        t.setText((String) msg.obj);
                    }
                    case RadishworksConnector.REQUEST_CMD_GPS:
                    {
                        TextView t = (TextView) findViewById(R.id.commandPostGps);
                        t.setText((String) msg.obj);
                    }
                    case RadishworksConnector.REQUEST_RADIO_COMMAND:
                    {
                        TextView t = (TextView) findViewById(R.id.radioCommand);
                        t.setText((String) msg.obj);
                    }
                    case RadishworksConnector.REQUEST_RADIO_TACTICAL:
                    {
                        TextView t = (TextView) findViewById(R.id.radioTactical);
                        t.setText((String) msg.obj);
                    }
                    break;
                }

                String teamInfo = Integer.toString(MainActivity.team.number);
                if (MainActivity.team.type != null){
                    teamInfo = teamInfo + " - " + MainActivity.team.type;
                }
                TextView t = (TextView) findViewById(R.id.teamNumber);
                t.setText(teamInfo);

                super.handleMessage(msg);
            }
        };
    }

}