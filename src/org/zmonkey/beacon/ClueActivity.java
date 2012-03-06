package org.zmonkey.beacon;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * User: corey
 * Date: 3/2/12
 * Time: 8:28 PM
 */
public class ClueActivity extends Activity {
    private Handler h;
    public static ClueActivity clue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clue);

        clue = this;

        final Button b;
        b = (Button) findViewById(R.id.clueSend);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClue();
            }
        });

        final EditText t;
        t = (EditText) findViewById(R.id.clueName);
        t.setEnabled(false);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clueNameChanged(t, b);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        enableFields(MainActivity.main.hasMissionNumber());

        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), MainActivity.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                switch (msg.what) {
                    case MainActivity.REQUEST_POST_CLUE:
                        cluePosted((String) msg.obj);
                        break;
                }
                super.handleMessage(msg);
            }
        };

    }

    public void enableFields(boolean enabled){
        EditText t;
        t = (EditText) findViewById(R.id.clueName);
        t.setEnabled(enabled);
        t = (EditText) findViewById(R.id.clueDescription);
        t.setEnabled(enabled);
        t = (EditText) findViewById(R.id.clueLocation);
        t.setEnabled(enabled);
        t = (EditText) findViewById(R.id.clueFoundBy);
        t.setEnabled(enabled);

    }

    public void clueNameChanged(EditText t, Button b){
        if (t.getText().length() > 0){
            b.setEnabled(true);
        }
        else{
            b.setEnabled(false);
        }
    }

    private void cluePosted(String msg){
        String error = MainActivity.apiFailure(msg);
        if (error == null){
            EditText t;
            t = (EditText) findViewById(R.id.clueName);
            t.setText("");
            t = (EditText) findViewById(R.id.clueDescription);
            t.setText("");
            t = (EditText) findViewById(R.id.clueLocation);
            t.setText("");
            t = (EditText) findViewById(R.id.clueFoundBy);
            t.setText("");
            Toast.makeText(getApplicationContext(), "Clue posted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendClue(){
        EditText t;
        t = (EditText) findViewById(R.id.clueName);
        String clueName = t.getText().toString();
        t = (EditText) findViewById(R.id.clueDescription);
        String clueDescription = t.getText().toString();
        t = (EditText) findViewById(R.id.clueLocation);
        String clueLocation = t.getText().toString();
        t = (EditText) findViewById(R.id.clueFoundBy);
        String clueFoundBy = t.getText().toString();

        StringBuffer s = new StringBuffer();
        s.append(MainActivity.API_CLUE_NAME);
        s.append(URLEncoder.encode(clueName));
        s.append("&");
        s.append(MainActivity.API_CLUE_DESCRIPTION);
        s.append(URLEncoder.encode(clueDescription));
        s.append("&");
        s.append(MainActivity.API_CLUE_LOCATION);
        s.append(URLEncoder.encode(clueLocation));
        s.append("&");
        s.append(MainActivity.API_CLUE_FOUNDBY);
        s.append(URLEncoder.encode(clueFoundBy));

        if (MainActivity.main.currentLocation != null){
            String lat = Double.toString(MainActivity.main.currentLocation.getLatitude());
            s.append("&");
            s.append(MainActivity.API_LATITUDE);
            s.append(lat);
            String lon = Double.toString(MainActivity.main.currentLocation.getLongitude());
            s.append("&");
            s.append(MainActivity.API_LONGITUDE);
            s.append(lon);
        }
        else{
            s.append("&");
            s.append(MainActivity.API_LATITUDE);
            s.append("&");
            s.append(MainActivity.API_LONGITUDE);
        }

        MainActivity.main.apiCall(MainActivity.REQUEST_POST_CLUE, h, s.toString());
    }
}