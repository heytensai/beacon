/*
 Copyright (C) 2012 Corey Edwards

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.zmonkey.beacon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.zmonkey.beacon.data.DataManager;
import org.zmonkey.beacon.data.Subject;

import java.util.Vector;

/**
 * User: corey
 * Date: 3/11/12
 * Time: 3:37 PM
 */
public class SubjectsActivity extends Activity {
    public static SubjectsActivity subjects;
    public Handler h;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
        subjects = this;
        makeSubjectList(DataManager.data.subjects);
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
            t.setText(s.bio());
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
        Intent intent = new Intent(this, SubjectDetailActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("subject", s);
        intent.putExtras(b);
        startActivity(intent);
    }

}