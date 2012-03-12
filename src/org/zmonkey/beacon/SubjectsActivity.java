package org.zmonkey.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.zmonkey.beacon.data.Subject;

import java.util.Vector;

/**
 * User: corey
 * Date: 3/11/12
 * Time: 3:37 PM
 */
public class SubjectsActivity extends Activity {
    public static SubjectsActivity subjects;
    private Handler h;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
        subjects = this;
        setupCallbackHandler();
        loadSubjects();
    }

    public void loadSubjects(){
        if (MainActivity.main.hasMissionNumber()){
            RadishworksConnector.apiCall(RadishworksConnector.REQUEST_SUBJECT_LIST, this, h);
        }
    }

    private void makeSubjectList(Vector<Subject> v){
        //empty the current list
        LinearLayout layout = (LinearLayout) findViewById(R.id.subjectList);
        if (layout == null){
            Toast.makeText(getApplicationContext(), "Layout is null", Toast.LENGTH_LONG).show();
            return;
        }
        layout.removeAllViews();

        //make an empty list
        if (v == null || v.size() == 0){
            TextView t = new TextView(this);
            t.setText("No Data");
            layout.addView(t);
            return;
        }

        //fill in the list
        for (final Subject s : v){
            TextView t = new TextView(this);
            t.setText(s.toString());
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            t.setPadding(10, 10, 10, 10);

            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subjectClicked(s);
                }
            });

            layout.addView(t);
        }
    }
    
    private void subjectClicked(Subject s){
        
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), RadishworksConnector.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                switch (msg.what) {
                    case RadishworksConnector.REQUEST_SUBJECT_LIST:
                    {
                        makeSubjectList(Subject.parseText((String) msg.obj));
                    }
                    break;
                }
                super.handleMessage(msg);
            }
        };
    }

}