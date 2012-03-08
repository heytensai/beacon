package org.zmonkey.beacon;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends TabActivity implements LocationListener
{
    //public static final String PREFS_NAME = "org.zmonkey.beacon";
    public static MainActivity main;
    public static final Mission mission = new Mission();
    public static final Team team = new Team();
    private static final int LOCATION_UPDATE_TIME = 60000;
    private static final int LOCATION_UPDATE_DISTANCE = 50;
    
    private static final int OPTIONS_SETTINGS = 0;
    private static final int OPTIONS_REFRESH = 1;
    private static final int OPTIONS_ABOUT = 2;

    private Handler h;
    private LocationManager locationManager;
    public Location currentLocation;

    public void onLocationChanged(Location location) {
        if (LocationActivity.location != null){
            LocationActivity.location.updateLocation(location);
        }
        currentLocation = location;
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
        //Toast.makeText(getApplicationContext(), "MainActivity.onStart", Toast.LENGTH_SHORT).show();
    }

    protected void onRestart(){
        super.onRestart();
        //Toast.makeText(getApplicationContext(), "MainActivity.onRestart", Toast.LENGTH_SHORT).show();
    }

    protected void onResume(){
        if (locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, this);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
    }

    protected void onDestroy(){
        super.onDestroy();
        //Toast.makeText(getApplicationContext(), "MainActivity.onDestroy", Toast.LENGTH_SHORT).show();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupButtons();
        setupMission();
        refreshDisplay();
        main = this;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, InfoActivity.class);
        spec = tabHost.newTabSpec("info").setIndicator("Info", res.getDrawable(R.drawable.ic_tab_info)).setContent(intent);
        tabHost.addTab(spec);

//        intent = new Intent().setClass(this, MapActivity.class);
//        spec = tabHost.newTabSpec("map").setIndicator("Map", res.getDrawable(R.drawable.ic_tab_map)).setContent(intent);
//        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ClueActivity.class);
        spec = tabHost.newTabSpec("clue").setIndicator("Clue", res.getDrawable(R.drawable.ic_tab_clue)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, LocationActivity.class);
        spec = tabHost.newTabSpec("location").setIndicator("Location", res.getDrawable(R.drawable.ic_tab_location)).setContent(intent);
        tabHost.addTab(spec);

        setupCallbackHandler();
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), API_REQUESTS[msg.what] + "-/-" + (String)msg.obj, Toast.LENGTH_SHORT).show();
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
    }

    private void setupButtons(){

    }

    private void listActiveMissions(){
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_LIST_MISSIONS, this, h);
    }

    public boolean hasApiKey(){
        return !PreferenceManager.getDefaultSharedPreferences(this).getString("apikey", "").equals("");
    }

    public boolean hasMissionNumber(){
        return (mission.number > -1);
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
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case OPTIONS_ABOUT:
                makeAboutDialog();
                return true;
            case OPTIONS_REFRESH:
                if (InfoActivity.info != null){
                    InfoActivity.info.loadMissionDetails();
                }
                return true;
        }
        return false;
    }

    private void makeSelectMissionDialog(String missions){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Mission Select");

        String failure = apiFailure(missions);
        if (failure != null){
            alert.setMessage(failure);
            alert.setPositiveButton("Ok", null);
            alert.show();
            return;
        }
        //Toast.makeText(getApplicationContext(), missions, Toast.LENGTH_SHORT).show();

        final String[] missionList = missions.split(RadishworksConnector.FIELD_DELIMITER);
        final String[] missionNames = new String[missionList.length];
        final int[] missionNumbers = new int[missionList.length];
        for (int i=0; i<missionNames.length; i++){
            String s = missionList[i];
            String[] f = s.split(",", 3);
            missionNames[i] = f[1] + " " + f[2];
            missionNumbers[i] = Integer.parseInt(f[0]);
        }
        alert.setItems(missionNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mission.number = missionNumbers[item];
                TextView t = (TextView)findViewById(R.id.mission);
                t.setText(missionNames[item]);
                refreshButtons();
                if (InfoActivity.info != null){
                    InfoActivity.info.loadMissionDetails();
                }
                if (ClueActivity.clue != null){
                    ClueActivity.clue.enableFields(true);
                }
            }
        });
        alert.show();
    }

    public static String apiFailure(String s){
        if (s == null || s.equals("")){
            return "No data returned";
        }
        if (s.equals("<300>")){
            return "Missing API Key";
        }
        if (s.equals("<301>")){
            return "Bad API Key";
        }
        if (s.equals("<302>")){
            return "No command";
        }
        if (s.equals("<303>")){
            return "No email address";
        }
        if (s.equals("<304>")){
            return "No permissions";
        }
        if (s.equals("<305>")){
            return "No mission selected";
        }
        if (s.equals("<306>")){
            return "No permissions to that mission";
        }
        if (s.equals("<309>")){
            return "Sorry, no missions";
        }
        if (s.equals("<310>")){
            return "Invalid Latitude Longitude";
        }
        if (s.equals("<311>")){
            return "No team assignment";
        }
        if (s.equals("<312>")){
            return "Invalid Post add value";
        }
        if (s.equals("<313>")){
            return "Invalid Get request";
        }
        if (s.equals("<314>")){
            return "Missing clue name";
        }
        if (s.equals("<315>")){
            return "Posted image is too large";
        }

        return null;
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
