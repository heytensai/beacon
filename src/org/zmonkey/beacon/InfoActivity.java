package org.zmonkey.beacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.zmonkey.beacon.data.DataManager;

/**
 * User: corey
 * Date: 3/2/12
 * Time: 8:54 PM
 */
public class InfoActivity extends Activity {
    public static InfoActivity info;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        info = this;

        //make GPS coords clickable
        final TextView t = (TextView) findViewById(R.id.commandPostGps);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataManager.data.activeMission == null){
                    return;
                }
                if (DataManager.data.activeMission.commandPostGPSCoords == null){
                    return;
                }
                String uri = "geo:" + t.getText();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                try {
                    startActivity(i);
                }
                catch (ActivityNotFoundException e){
                    uri = "http://maps.google.com/maps?q=" + t.getText();
                    i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    try {
                        startActivity(i);
                    }
                    catch (ActivityNotFoundException e2){
                        Toast.makeText(InfoActivity.this, "No mapping intent found for " + uri, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        refreshViews();
    }
    
    public void onDataChanged(int field, Object value){
        TextView t = (TextView) findViewById(field);
        t.setText((String) value);
    }

    public void refreshViews(){
        TextView t;

        String teamInfo = Integer.toString(DataManager.data.myTeam.number);
        if (DataManager.data.myTeam.type == null || DataManager.data.myTeam.number < 1){
            return;
        }

        teamInfo = teamInfo + " - " + DataManager.data.myTeam.type;
        t = (TextView) findViewById(R.id.teamNumber);
        t.setText(teamInfo);

        t = (TextView) findViewById(R.id.teamMembers);
        t.setText(DataManager.data.myTeam.getMembers());
        t = (TextView) findViewById(R.id.teamObjectives);
        t.setText(DataManager.data.myTeam.objectives);
        t = (TextView) findViewById(R.id.teamNotes);
        t.setText(DataManager.data.myTeam.notes);
        t = (TextView) findViewById(R.id.missionDescription);
        t.setText(DataManager.data.activeMission.description);
        t = (TextView) findViewById(R.id.commandPostName);
        t.setText(DataManager.data.activeMission.commandPostName);
        t = (TextView) findViewById(R.id.commandPostLocation);
        t.setText(DataManager.data.activeMission.commandPostLocation);
        t = (TextView) findViewById(R.id.commandPostGps);
        t.setText(DataManager.data.activeMission.commandPostGPSCoords);
        t = (TextView) findViewById(R.id.radioCommand);
        t.setText(DataManager.data.activeMission.radioCommandChannel);
        t = (TextView) findViewById(R.id.radioTactical);
        t.setText(DataManager.data.activeMission.radioTacticalChannel);
    }

}