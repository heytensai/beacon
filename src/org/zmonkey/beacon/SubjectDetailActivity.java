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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
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

            t = (TextView) findViewById(R.id.subjectDateLastSeen);
            if (subject.dateLastSeen != null){
                t.setText(subject.dateLastSeen);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectTimeLastSeen);
            if (subject.timeLastSeen != null){
                t.setText(subject.timeLastSeen);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectLastLocation);
            if (subject.lastLocation != null){
                t.setText(subject.lastLocation);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectLastGps);
            if (subject.lastGps != null){
                t.setText(subject.lastGps);
                final Subject s = subject;
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gpsClicked(s);
                    }
                });
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectHairStyle);
            if (subject.hairStyle != null){
                t.setText(subject.hairStyle);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectComplexion);
            if (subject.complexion != null){
                t.setText(subject.complexion);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectBuild);
            if (subject.build != null){
                t.setText(subject.build);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectShirt);
            if (subject.shirt != null){
                t.setText(subject.shirt);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectPants);
            if (subject.pants != null){
                t.setText(subject.pants);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectJacket);
            if (subject.jacket != null){
                t.setText(subject.jacket);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectShoes);
            if (subject.shoes != null){
                t.setText(subject.shoes);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectSocks);
            if (subject.socks != null){
                t.setText(subject.socks);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectGloves);
            if (subject.gloves != null){
                t.setText(subject.gloves);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectInnerWear);
            if (subject.innerWear != null){
                t.setText(subject.innerWear);
            }
            else{
                t.setText("");
            }

            t = (TextView) findViewById(R.id.subjectOuterWear);
            if (subject.outerWear != null){
                t.setText(subject.outerWear);
            }
            else{
                t.setText("");
            }
        }
    }

    private void gpsClicked(Subject subject){
        if (subject.lastGps == null){
            return;
        }
        String alias = "(Subject)";
        if (subject.name != null || !subject.name.equals("")){
            alias = "(" + subject.name + ")";
        }
        String coords = subject.lastGps;
        String uri = "geo:" + coords + "?q=" + coords + alias;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        try {
            startActivity(i);
        }
        catch (ActivityNotFoundException e){
            uri = "http://maps.google.com/maps?q=" + coords + alias;
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            try {
                startActivity(i);
            }
            catch (ActivityNotFoundException e2){
                Toast.makeText(this, "No mapping intent found for " + uri, Toast.LENGTH_LONG).show();
            }
        }
    }
}