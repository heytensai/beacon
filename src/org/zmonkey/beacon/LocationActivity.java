package org.zmonkey.beacon;

import android.app.Activity;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.zmonkey.beacon.data.LocationUpdate;

import java.util.Vector;

/**
 * User: corey
 * Date: 3/2/12
 * Time: 8:57 PM
 */
public class LocationActivity extends Activity {
    public static LocationActivity location;
    private Handler h;
    private Vector<LocationUpdate> pendingUpdates;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        location = this;
        pendingUpdates = new Vector<LocationUpdate>();

        Button b = (Button) findViewById(R.id.locationUpdate);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postLocation();
            }
        });
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), MainActivity.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                switch (msg.what) {
                    case RadishworksConnector.REQUEST_POST_LOCATION:
                        locationPosted((String) msg.obj);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        //Toast.makeText(getApplicationContext(), "LocationActivity.onCreate", Toast.LENGTH_SHORT).show();
    }

    protected void onResume(){
        super.onResume();
        //Toast.makeText(getApplicationContext(), "LocationActivity.onResume", Toast.LENGTH_SHORT).show();
    }

    protected void onPause(){
        super.onPause();
        //Toast.makeText(getApplicationContext(), "LocationActivity.onPause", Toast.LENGTH_SHORT).show();
    }

    public void locationPosted(String msg){
        String error = RadishworksConnector.apiFailure(msg);
        if (error == null){
            Toast.makeText(getApplicationContext(), "Location posted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateLocation(Location location){
        TextView lat = (TextView) findViewById(R.id.locationLatitude);
        TextView lon = (TextView) findViewById(R.id.locationLongitude);
        TextView alt = (TextView) findViewById(R.id.locationAltitude);
        Button b = (Button) findViewById(R.id.locationUpdate);
        if (location == null){
            lat.setText("No Data");
            lon.setText("No Data");
            alt.setText("No Data");
            b.setEnabled(false);
        }
        else{
            double _lat = location.getLatitude();
            double _lon = location.getLongitude();
            lat.setText(trimCoord(Double.toString(_lat)) + ", " + Location.convert(_lat, Location.FORMAT_SECONDS));
            lon.setText(trimCoord(Double.toString(_lon)) + ", " + Location.convert(_lon, Location.FORMAT_SECONDS));
            if (location.hasAltitude()){
                double _alt = location.getAltitude();
                alt.setText(Double.toString(_alt) + "m, " + Double.toString(_alt * 3.28) + "ft");
            }
            else{
                alt.setText("No Data");
            }
            b.setEnabled(true);
        }
    }

    private void postLocation(){
        if (MainActivity.main.currentLocation == null){
            return;
        }
        LocationUpdate loc = new LocationUpdate(MainActivity.main.currentLocation);
        pendingUpdates.add(loc);

        NetworkInfo network = MainActivity.connectivity.getActiveNetworkInfo();
        if (network.isConnected()){
            int i = 0;
            for (LocationUpdate update : pendingUpdates){
                if (RadishworksConnector.apiCall(RadishworksConnector.REQUEST_POST_LOCATION, this, h, update.makeParams())){
                    pendingUpdates.remove(i);
                }
                i++;
            }
        }
    }

    private String trimCoord(String coord){
        if (coord == null){
            return "";
        }
        if (coord.length() < 4){
            return coord;
        }
        int dot = coord.indexOf('.');
        return coord.substring(0, dot + 4);
    }
}