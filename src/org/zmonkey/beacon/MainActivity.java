package org.zmonkey.beacon;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends TabActivity implements LocationListener
{
    public static final String PREFS_NAME = "org.zmonkey.beacon";
    public static final int APIKEY_LENGTH = 33;
    public static final String FIELD_DELIMITER = "<br/>";
    public static final int REQUEST_LIST_MISSIONS = 0;
    public static final int REQUEST_TEAM_NUMER = 1;
    public static final int REQUEST_TEAM_MEMBERS = 2;
    public static final int REQUEST_TEAM_TYPE = 3;
    public static final int REQUEST_TEAM_OBJECTIVES = 4;
    public static final int REQUEST_TEAM_NOTES = 5;
    public static final int REQUEST_POST_LOCATION = 6;
    public static final int REQUEST_POST_CLUE = 7;
    public static final String API_BASE = "https://www.radishworks.com/SearchManager/api.php?";
    public static final String API_APIKEY = "APIKey=";
    public static final String API_MISSIONID = "MissionID=";
    public static final String API_LATITUDE = "Latitude=";
    public static final String API_LONGITUDE = "Longitude=";
    public static final String API_CLUE_NAME = "ClueName=";
    public static final String API_CLUE_DESCRIPTION = "ClueDescription=";
    public static final String API_CLUE_FOUNDBY = "ClueFoundBy=";
    public static final String API_CLUE_LOCATION = "ClueLocation=";
    public static final String[] API_REQUESTS = {"Get=MissionList", "Get=TeamNumber", "Get=TeamMembers", "Get=TeamType",
            "Get=TeamObjectives", "Get=TeamNotes", "Post=Location", "Post=Clue"
    };
    public static MainActivity main;
    private String apiKey;
    private int missionNumber = -1;
    private Handler h;
    private LocationManager locationManager;
    private static final int LOCATION_UPDATE_TIME = 60000;
    private static final int LOCATION_UPDATE_DISTANCE = 50;
    public Location currentLocation;

    public void onLocationChanged(Location location) {
        LocationActivity.location.updateLocation(location);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        super.onResume();
        //Toast.makeText(getApplicationContext(), "MainActivity.onResume", Toast.LENGTH_SHORT).show();
    }

    protected void onPause(){
        locationManager.removeUpdates(this);
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
        loadPreferences();
        refreshDisplay();
        main = this;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, InfoActivity.class);
        spec = tabHost.newTabSpec("info").setIndicator("Info", res.getDrawable(R.drawable.ic_tab_clue)).setContent(intent);
        tabHost.addTab(spec);

//        intent = new Intent().setClass(this, MapActivity.class);
//        spec = tabHost.newTabSpec("map").setIndicator("Map", res.getDrawable(R.drawable.ic_tab_map)).setContent(intent);
//        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ClueActivity.class);
        spec = tabHost.newTabSpec("clue").setIndicator("Clue", res.getDrawable(R.drawable.ic_tab_clue)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, LocationActivity.class);
        spec = tabHost.newTabSpec("location").setIndicator("Location", res.getDrawable(R.drawable.ic_tab_clue)).setContent(intent);
        tabHost.addTab(spec);

        setupCallbackHandler();
    }

    private void setupCallbackHandler(){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), API_REQUESTS[msg.what] + "-/-" + (String)msg.obj, Toast.LENGTH_SHORT).show();
                switch (msg.what) {
                    case REQUEST_LIST_MISSIONS:
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

    public void apiCall(int requestId, Handler h, String parameters){
        try	{
            String uri = API_BASE + API_APIKEY + apiKey + "&" + API_REQUESTS[requestId];
            if (parameters != null){
                uri = uri + "&" + parameters;
            }
            switch(requestId){
                case REQUEST_TEAM_NUMER:
                case REQUEST_TEAM_MEMBERS:
                case REQUEST_TEAM_OBJECTIVES:
                case REQUEST_TEAM_NOTES:
                case REQUEST_TEAM_TYPE:
                case REQUEST_POST_LOCATION:
                case REQUEST_POST_CLUE:
                    uri = uri + "&" + API_MISSIONID + Integer.toString(missionNumber);
                    break;
            }
            //Toast.makeText(getApplicationContext(), uri, Toast.LENGTH_SHORT).show();
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                Message lmsg;
                lmsg = new Message();
                lmsg.obj = line;
                lmsg.what = requestId;
                h.sendMessage(lmsg);
            }
        }
        catch (Exception e)	{
        }
    }

    public void apiCall(int requestId, Handler h){
        apiCall(requestId, h, null);
    }

    private void listActiveMissions(){
        apiCall(REQUEST_LIST_MISSIONS, h);
    }

    private void loadPreferences(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);

        apiKey = prefs.getString("apikey", "");

        //Toast.makeText(getApplicationContext(), "Loaded Preferences", Toast.LENGTH_SHORT).show();
    }

    public boolean hasApiKey(){
        return !(apiKey == null);
    }

    public void setApiKey(String key){
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        if (key == null){
            edit.putString("apikey", "");
            apiKey = null;
            return;
        }
        if (key.equals("")){
            apiKey = null;
        }
        else{
            apiKey = key;
        }
        edit.putString("apikey", key);
        edit.commit();
    }

    public int getMissionNumber(){
        return missionNumber;
    }

    public boolean hasMissionNumber(){
        return (missionNumber > -1);
    }

    public void refreshDisplay(){
        refreshApiKey();
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

    private void refreshApiKey(){
        TextView t = (TextView) findViewById(R.id.apikey);
        if (hasApiKey()){
            t.setText(apiKey);
        }
        else{
            t.setText("No API Key");
        }
    }

    public String getApiKey(){
        return apiKey;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuSetApiKey:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
                makeApiKeyDialog();
                return true;
            case R.id.menuAbout:
                makeAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        final String[] missionList = missions.split(FIELD_DELIMITER);
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
                missionNumber = missionNumbers[item];
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

        alert.setTitle("Mission Manager");
        alert.setMessage("For Search & Rescue and stuff");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
    public void makeApiKeyDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("API Key");
        alert.setMessage("Enter your API Key");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(getApiKey());
        alert.setView(input);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(APIKEY_LENGTH);
        input.setFilters(FilterArray);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MainActivity.main.setApiKey(input.getText().toString());
                MainActivity.main.refreshDisplay();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
