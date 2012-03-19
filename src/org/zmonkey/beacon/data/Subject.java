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

import java.io.Serializable;
import java.util.Vector;

/**
 * User: corey
 * Date: 3/11/12
 * Time: 3:16 PM
 */
public class Subject implements Serializable {
    public int id;
    public String name;
    public String nickname;
    public String dateLastSeen;
    public String timeLastSeen;
    public String lastLocation;
    public String lastGps;
    public String sex;
    public String age;
    public String height;
    public String weight;
    public String eyeColor;
    public String hairColor;
    public String hairStyle;
    public String complexion;
    public String build;
    public String shirt;
    public String pants;
    public String jacket;
    public String shoes;
    public String socks;
    public String gloves;
    public String innerWear;
    public String outerWear;
    
    public static Vector<Subject> parseText(String text){
        Vector<Subject> v = new Vector<Subject>();

        if (text == null || text.equals("")){
            return v;
        }

        String[] n = text.split("\n");

        for (String i : n){
            String[] a = i.split(",", -1);

            Subject s = new Subject();
            s.id = Integer.parseInt(a[0]);
            s.name = a[1];
            s.nickname = a[2];
            s.dateLastSeen = a[3];
            s.timeLastSeen = a[4];
            s.lastLocation = a[5];
            s.lastGps = a[6];
            s.sex = a[7];
            s.age = a[8];
            s.height = a[9];
            s.weight = a[10];
            s.eyeColor = a[11];
            s.hairColor = a[12];
            s.hairStyle = a[13];
            s.complexion = a[14];
            s.build = a[15];
            s.shirt = a[16];
            s.pants = a[17];
            s.jacket = a[18];
            s.shoes = a[19];
            s.socks = a[20];
            s.gloves = a[21];
            s.innerWear = a[22];
            s.outerWear = a[23];

            v.add(s);
        }

        return v;
    }
    
    public String toString(){
        if (name == null){
            return "";
        }
        StringBuilder s = new StringBuilder();
        s.append(name);
        
        if (sex != null){
            s.append(", ");
            s.append(sex);
        }
        
        if (age != null){
            s.append(", Age ");
            s.append(age);
        }

        return s.toString();
    }
}
