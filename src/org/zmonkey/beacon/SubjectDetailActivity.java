package org.zmonkey.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.zmonkey.beacon.data.Subject;

/**
 * User: corey
 * Date: 3/11/12
 * Time: 10:04 PM
 */
public class SubjectDetailActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjectdetail);
        
        //retrieve subject detail
        Bundle b = getIntent().getExtras();
        Subject subject = null;
        if (b != null){
            subject = (Subject) b.getSerializable("subject");
        }

        if (subject != null){
            TextView t;
    
            t = (TextView) findViewById(R.id.subjectName);
            if (subject.name != null){
                t.setText(subject.name);
            }
            else{
                t.setText("");
            }
    
            t = (TextView) findViewById(R.id.subjectNickname);
            if (subject.name != null){
                t.setText(subject.nickname);
            }
            else{
                t.setText("");
            }
    
            t = (TextView) findViewById(R.id.subjectAge);
            if (subject.name != null){
                t.setText(subject.age);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectSex);
            if (subject.name != null){
                t.setText(subject.sex);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectHeight);
            if (subject.name != null){
                t.setText(subject.height);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectWeight);
            if (subject.name != null){
                t.setText(subject.weight);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectEyeColor);
            if (subject.name != null){
                t.setText(subject.eyeColor);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectHairColor);
            if (subject.name != null){
                t.setText(subject.hairColor);
            }
            else{
                t.setText("");
            }
        }
    }


}