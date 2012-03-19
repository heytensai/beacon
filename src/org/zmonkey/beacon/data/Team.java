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
 * Time: 9:04 PM
 */
public class Team {
    public int number;
    public String objectives;
    public String notes;
    public String type;
    public String[] members;

    public Team(){
    }
    
    public void setMembers(String m){
        members = m.split(",");
    }
    
    public String getMembers(){
        if (members == null || members.length == 0){
            return "";
        }
        StringBuilder s = new StringBuilder();
        int i=0;
        for (; i<members.length-1; i++){
            s.append(members[i]);
            s.append("\n");
        }
        s.append(members[i]);
        return s.toString();
    }
}
