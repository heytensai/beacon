package org.zmonkey.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        Button b;

        b = (Button) findViewById(R.id.refresh);
        b.setEnabled(false);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMissionDetails();
            }
        });

        setupCallbackHandler();

        info = this;
    }

    public void loadMissionDetails(){
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_NUMER, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_MEMBERS, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_TYPE, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_OBJECTIVES, this, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_NOTES, this, h);
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), MainActivity.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
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
                Button b;
                b = (Button) findViewById(R.id.refresh);
                if (b != null){
                    b.setEnabled(true);
                }
            }
        };
    }

}