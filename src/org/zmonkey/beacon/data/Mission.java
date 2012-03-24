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

/**
 * User: corey
 * Date: 3/6/12
 * Time: 9:02 PM
 */
public class Mission implements Storable {
    public int number = -1;
    public String name;
    public String description;
    public String commandPostName;
    public String commandPostLocation;
    public String commandPostGPSCoords;
    public String radioCommandChannel;
    public String radioTacticalChannel;

    @Override
    public StringBuilder store(StringBuilder s) {
        if (s ==  null){
            s = new StringBuilder();
        }
        s.append("class=Mission\n");
        if (number > -1) s.append(" number=" + number + "\n");
        if (name != null) s.append(" name=" + name + "\n");
        if (description != null) s.append(" description=" + description + "\n");
        if (commandPostName != null) s.append(" commandPostName=" + commandPostName + "\n");
        if (commandPostLocation != null) s.append(" commandPostLocation=" + commandPostLocation + "\n");
        if (commandPostGPSCoords != null) s.append(" commandPostGPSCoords=" + commandPostGPSCoords + "\n");
        if (radioCommandChannel != null) s.append(" radioCommandChannel=" + radioCommandChannel + "\n");
        if (radioTacticalChannel != null) s.append(" radioTacticalChannel=" + radioTacticalChannel + "\n");
        return s;
    }

    @Override
    public void load(String s) {
        if (s == null || s.equals("")){
            return;
        }
        String[] lines = s.split("\n");
        boolean me = false;
        for (String line : lines){
            if (me){
                if (!line.equals("class=Mission") && line.startsWith("class=")){
                    me = false;
                }
                else{
                    if (line.startsWith(" number=")){
                        number = Integer.parseInt(line.substring(8));
                    }
                    else if (line.startsWith(" name=")){
                        name = line.substring(6);
                    }
                    else if (line.startsWith(" description=")){
                        description = line.substring(13);
                    }
                    else if (line.startsWith(" commandPostName=")){
                        commandPostName = line.substring(17);
                    }
                    else if (line.startsWith(" commandPostLocation=")){
                        commandPostLocation = line.substring(21);
                    }
                    else if (line.startsWith(" commandPostGPSCoords=")){
                        commandPostGPSCoords = line.substring(22);
                    }
                    else if (line.startsWith(" radioCommandChannel=")){
                        radioCommandChannel = line.substring(21);
                    }
                    else if (line.startsWith(" radioTacticalChannel=")){
                        radioTacticalChannel = line.substring(22);
                    }
                }
            }
            else{
                if (line.equals("class=Mission")){
                    me = true;
                }
            }
        }
    }
}
