package org.zmonkey.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
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
        MainActivity.main.apiCall(MainActivity.REQUEST_TEAM_NUMER, h);
        MainActivity.main.apiCall(MainActivity.REQUEST_TEAM_MEMBERS, h);
        MainActivity.main.apiCall(MainActivity.REQUEST_TEAM_TYPE, h);
        MainActivity.main.apiCall(MainActivity.REQUEST_TEAM_OBJECTIVES, h);
        MainActivity.main.apiCall(MainActivity.REQUEST_TEAM_NOTES, h);
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), MainActivity.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                switch (msg.what) {
                    case MainActivity.REQUEST_TEAM_NUMER:
                    {
                        TextView t = (TextView) findViewById(R.id.teamNumber);
                        t.setText((String) msg.obj);
                    }
                    break;
                    case MainActivity.REQUEST_TEAM_MEMBERS:
                    {
                        TextView t = (TextView) findViewById(R.id.teamMembers);
                        String s = (String) msg.obj;
                        s = s.replaceAll(",", "\n");
                        t.setText(s);
                    }
                    break;
                    case MainActivity.REQUEST_TEAM_TYPE:
                    {
                        TextView t = (TextView) findViewById(R.id.teamType);
                        t.setText((String) msg.obj);
                    }
                    break;
                    case MainActivity.REQUEST_TEAM_OBJECTIVES:
                    {
                        TextView t = (TextView) findViewById(R.id.teamObjectives);
                        t.setText((String) msg.obj);
                    }
                    break;
                    case MainActivity.REQUEST_TEAM_NOTES:
                    {
                    }
                    break;
                }
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