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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import org.zmonkey.beacon.data.DataManager;
import org.zmonkey.beacon.data.Subject;

import java.io.*;

public class MainActivity extends TabActivity implements LocationListener
{
    //public static final String PREFS_NAME = "org.zmonkey.beacon";
    public static MainActivity main;
    private static final int LOCATION_UPDATE_TIME = 60000;
    private static final int LOCATION_UPDATE_DISTANCE = 50;
    
    private static final int OPTIONS_SETTINGS = 0;
    private static final int OPTIONS_REFRESH = 1;
    private static final int OPTIONS_ABOUT = 2;

    private Handler h;
    private LocationManager locationManager;
    public static ConnectivityManager connectivity;
    public ProgressDialog progressDialog;

    public void onLocationChanged(Location location) {
        if (LocationActivity.location != null){
            LocationActivity.location.updateLocation(location);
        }
        DataManager.data.currentLocation = location;
        //Toast.makeText(getApplicationContext(), "MainActivity.onLocationChanged", Toast.LENGTH_SHORT).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {
        //Toast.makeText(getApplicationContext(), "MainActivity.onProviderEnabled", Toast.LENGTH_SHORT).show();
    }

    public void onProviderDisabled(String provider) {
        if (LocationActivity.location != null){
            LocationActivity.location.updateLocation(null);
        }
        //Toast.makeText(getApplicationContext(), "MainActivity.onProviderDisabled", Toast.LENGTH_SHORT).show();
    }


    protected void onStart(){
        super.onStart();
        loadData();
        refreshDisplay();
        if (InfoActivity.info != null){
            InfoActivity.info.refreshViews();
        }
        //Toast.makeText(getApplicationContext(), "MainActivity.onStart", Toast.LENGTH_SHORT).show();
    }

    protected void onRestart(){
        super.onRestart();
        //Toast.makeText(getApplicationContext(), "MainActivity.onRestart", Toast.LENGTH_SHORT).show();
    }

    protected void onResume(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs == null || prefs.getBoolean("usegps", true)){
            if (locationManager != null){
                int updateTime = (prefs == null) ? prefs.getInt("gpsUpdateInterval", LOCATION_UPDATE_TIME) : LOCATION_UPDATE_TIME;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime, LOCATION_UPDATE_DISTANCE, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, updateTime, LOCATION_UPDATE_DISTANCE, this);
                DataManager.data.currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        super.onResume();
        //Toast.makeText(getApplicationContext(), "MainActivity.onResume", Toast.LENGTH_SHORT).show();
    }

    protected void onPause(){
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }
        super.onPause();
        //Toast.makeText(getApplicationContext(), "MainActivity.onPause", Toast.LENGTH_SHORT).show();
    }

    protected void onStop(){
        super.onStop();
        //Toast.makeText(getApplicationContext(), "MainActivity.onStop", Toast.LENGTH_SHORT).show();

        saveData();
    }

    protected void onDestroy(){
        super.onDestroy();
        //Toast.makeText(getApplicationContext(), "MainActivity.onDestroy", Toast.LENGTH_SHORT).show();
    }

    private void loadData(){
        StringBuilder input = new StringBuilder();
        try{
            FileInputStream stream = openFileInput(getString(R.string.settingsfile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String s;
            while ((s = reader.readLine()) != null){
                input.append(s);
                input.append("\n");
            }
            stream.close();
            String saveData = input.toString();
            DataManager.data.myTeam.load(saveData);
            DataManager.data.activeMission.load(saveData);
            DataManager.data.subjects = Subject.loadAll(saveData);
        }
        catch (IOException e){
            //whatever, just give up.
        }
    }

    private void saveData(){
        StringBuilder output = new StringBuilder();
        if (DataManager.data.myTeam != null){
            DataManager.data.myTeam.store(output);
        }
        if (DataManager.data.activeMission != null){
            DataManager.data.activeMission.store(output);
        }
        for (Subject subject : DataManager.data.subjects){
            subject.store(output);
        }

        try{
            FileOutputStream stream = openFileOutput(getString(R.string.settingsfile), Context.MODE_PRIVATE);
            stream.write(output.toString().getBytes());
            stream.close();
        }
        catch (IOException e){
            //whatever, just give up.
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main = this;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, InfoActivity.class);
        spec = tabHost.newTabSpec("info").setIndicator("Info", res.getDrawable(R.drawable.ic_tab_info)).setContent(intent);
        tabHost.addTab(spec);

        //intent = new Intent().setClass(this, MapActivity.class);
        //spec = tabHost.newTabSpec("map").setIndicator("Map", res.getDrawable(R.drawable.ic_tab_map)).setContent(intent);
        //tabHost.addTab(spec);

        intent = new Intent().setClass(this, ClueActivity.class);
        spec = tabHost.newTabSpec("clue").setIndicator("Clue", res.getDrawable(R.drawable.ic_tab_clue)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SubjectsActivity.class);
        spec = tabHost.newTabSpec("subjects").setIndicator("Subjects", res.getDrawable(R.drawable.ic_tab_subjects)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, LocationActivity.class);
        spec = tabHost.newTabSpec("location").setIndicator("Location", res.getDrawable(R.drawable.ic_tab_location)).setContent(intent);
        tabHost.addTab(spec);

        setupCallbackHandler();

        setupMission();
        setupButtons();
        refreshDisplay();
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), API_REQUESTS[msg.what] + "-/-" + (String)msg.obj, Toast.LENGTH_SHORT).show();
                if (progressDialog != null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                switch (msg.what) {
                    case RadishworksConnector.REQUEST_LIST_MISSIONS:
                        makeSelectMissionDialog((String) msg.obj);
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void setupMission(){
        TextView t = (TextView) findViewById(R.id.mission);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listActiveMissions();
            }
        });
        setupMissionFont();
    }
    
    private void setupMissionFont(){
        TextView t = (TextView) findViewById(R.id.mission);
        String s = PreferenceManager.getDefaultSharedPreferences(this).getString("missionFontSize", getString(R.string.settings_missionFontSize_default));
        int fontSize = Integer.parseInt(s);
        if (fontSize > 10){
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
    }

    private void setupButtons(){

    }

    private void listActiveMissions(){
        progressDialog = ProgressDialog.show(this, "", getString(R.string.loading));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RadishworksConnector.apiCall(RadishworksConnector.REQUEST_LIST_MISSIONS, MainActivity.this, h);
            }
        });
        thread.start();
    }

    public boolean hasApiKey(){
        return !PreferenceManager.getDefaultSharedPreferences(this).getString("apikey", "").equals("");
    }

    public boolean hasMissionNumber(){
        return (DataManager.data.activeMission.number > -1);
    }

    public void refreshDisplay(){
        refreshMission();
        refreshButtons();
    }

    private void refreshMission(){
        boolean enabled = false;

        if (hasApiKey()){
            enabled = true;
        }
        TextView t = (TextView)findViewById(R.id.mission);
        t.setEnabled(enabled);
        if (DataManager.data != null && DataManager.data.activeMission.name != null){
            t.setText(DataManager.data.activeMission.name);
        }
    }
    
    private void refreshButtons(){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(Menu.NONE, OPTIONS_SETTINGS, 0, "Settings");
        menu.add(Menu.NONE, OPTIONS_REFRESH, 0, "Refresh");
        menu.add(Menu.NONE, OPTIONS_ABOUT, 0, "About");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case OPTIONS_SETTINGS:
                startActivityForResult(new Intent(this, SettingsActivity.class), OPTIONS_SETTINGS);
                return true;
            case OPTIONS_ABOUT:
                makeAboutDialog();
                return true;
            case OPTIONS_REFRESH:
                refreshData();
                return true;
        }
        return false;
    }

    public void refreshData(){
        DataManager.data.loadMissionDetails(this);
        DataManager.data.loadSubjects(this);
        if (ClueActivity.clue != null){
            ClueActivity.clue.enableFields(true);
        }
        if (SubjectsActivity.subjects != null){
            SubjectsActivity.subjects.makeSubjectList(DataManager.data.subjects);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case OPTIONS_SETTINGS:
                setupMissionFont();
                break;
        }
    }

    private void makeSelectMissionDialog(String missions){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Mission Select");

        String failure = RadishworksConnector.apiFailure(missions);
        if (failure != null){
            alert.setMessage(failure);
            alert.setPositiveButton("Ok", null);
            alert.show();
            return;
        }
        //Toast.makeText(getApplicationContext(), missions, Toast.LENGTH_SHORT).show();

        //TODO: convert these arrays to a vector
        final String[] missionList = missions.split(RadishworksConnector.FIELD_DELIMITER);
        int count = missionList.length - 1;
        if (missionList[count] == null || missionList[count].equals("")){
            count--;
        }
        final String[] missionNames = new String[count+1];
        final int[] missionNumbers = new int[count+1];
        for (int i=0; i<count+1; i++){
            String s = missionList[i];
            String[] f = s.split(",", 3);
            if (f.length > 0){
                missionNames[i] = f[1] + " " + f[2];
                missionNumbers[i] = Integer.parseInt(f[0]);
            }
            else{
                missionNames[i] = "No mission";
                missionNumbers[i] = 999;
            }
        }
        alert.setItems(missionNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                DataManager.data.activeMission.number = missionNumbers[item];
                DataManager.data.activeMission.name = missionNames[item];
                TextView t = (TextView)findViewById(R.id.mission);
                t.setText(DataManager.data.activeMission.name);
                refreshButtons();
                refreshData();
            }
        });
        alert.show();
    }

    public void makeAboutDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.app_name));
        alert.setMessage(getString(R.string.about));

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
}
