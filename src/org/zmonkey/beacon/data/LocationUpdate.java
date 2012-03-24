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
            s.append(Double.toString(DataManager.data.currentLocation.getLatitude()));
            s.append("&");
            s.append(RadishworksConnector.API_LONGITUDE);
            s.append(Double.toString(DataManager.data.currentLocation.getLongitude()));
        }

        if (time != null){
            SimpleDateFormat format;
            s.append("&");
            s.append(RadishworksConnector.API_TIME);
            format = new SimpleDateFormat("HH:mm:ss");
            s.append(format.format(time));
            s.append("&");
            s.append(RadishworksConnector.API_DATE);
            format = new SimpleDateFormat("yyyy-MM-dd");
            s.append(format.format(time));
        }

        return s.toString();
    }
}
