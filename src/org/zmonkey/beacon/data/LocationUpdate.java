package org.zmonkey.beacon.data;

import android.location.Location;
import org.zmonkey.beacon.MainActivity;
import org.zmonkey.beacon.RadishworksConnector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: corey
 * Date: 3/8/12
 * Time: 9:07 PM
 */
public class LocationUpdate {
    public Location location;
    public Date time;
    
    public LocationUpdate(Location l){
        time = new Date();
        location = l;
    }
    
    public String makeParams(){
        StringBuilder s = new StringBuilder();
        
        if (location != null){
            s.append("&");
            s.append(RadishworksConnector.API_LATITUDE);
            s.append(Double.toString(MainActivity.main.currentLocation.getLatitude()));
            s.append("&");
            s.append(RadishworksConnector.API_LONGITUDE);
            s.append(Double.toString(MainActivity.main.currentLocation.getLongitude()));
        }

        if (time != null){
            SimpleDateFormat format;
            s.append("&");
            s.append(RadishworksConnector.API_CLUETIME);
            format = new SimpleDateFormat("HH:mm:ss");
            s.append(format.format(time));
            s.append("&");
            s.append(RadishworksConnector.API_CLUEDATE);
            format = new SimpleDateFormat("yyyy-MM-dd");
            s.append(format.format(time));
        }

        return s.toString();
    }
}
