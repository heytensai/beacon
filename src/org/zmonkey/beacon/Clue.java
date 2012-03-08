package org.zmonkey.beacon;

import android.location.Location;
import android.text.format.DateFormat;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * User: corey
 * Date: 3/6/12
 * Time: 9:11 PM
 */
public class Clue {
    public String name;
    public String description;
    public String foundBy;
    public String foundAt;
    public Date time;
    public Location location;

    public Clue(){
        time = new Date();
    }
    
    public String toParams(){
        StringBuilder s = new StringBuilder();
        s.append(RadishworksConnector.API_CLUE_NAME);
        if (name != null){
            s.append(URLEncoder.encode(name));
        }
        s.append("&");
        s.append(RadishworksConnector.API_CLUE_DESCRIPTION);
        if (description != null){
            s.append(URLEncoder.encode(description));
        }
        s.append("&");
        s.append(RadishworksConnector.API_CLUE_LOCATION);
        if (foundAt != null){
            s.append(URLEncoder.encode(foundAt));
        }
        s.append("&");
        s.append(RadishworksConnector.API_CLUE_FOUNDBY);
        if (foundBy != null){
            s.append(URLEncoder.encode(foundBy));
        }

        s.append("&");
        s.append(RadishworksConnector.API_LATITUDE);
        if (location != null){
            String lat = Double.toString(location.getLatitude());
            s.append(lat);
        }
        s.append("&");
        s.append(RadishworksConnector.API_LONGITUDE);
        if (location != null){
            String lon = Double.toString(location.getLongitude());
            s.append(lon);
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
