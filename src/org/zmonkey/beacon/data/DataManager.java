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
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import org.zmonkey.beacon.*;

import java.util.Vector;

/**
 * User: corey
 * Date: 3/15/12
 * Time: 9:09 PM
 */
public class DataManager {
    public static final DataManager data = new DataManager();
    public static Handler h;

    public DataManager(){
        activeMission = new Mission();
        currentLocation = null;
        initFields();

        setupCallbackHandler(MainActivity.main.getApplicationContext());
    }

    private void initFields(){
        subjects = new Vector<Subject>();
        myTeam = new Team();
        pendingUpdates = new Vector<LocationUpdate>();
        pendingClues = new Vector<Clue>();
    }

    public Mission activeMission;
    public Vector<Subject> subjects;
    public Team myTeam;
    public Vector<LocationUpdate> pendingUpdates;
    public Vector<Clue> pendingClues;
    public Location currentLocation;

    public void loadSubjects(Context context){
        if (activeMission == null || activeMission.number == -1){
            return;
        }
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_SUBJECT_LIST, context, h);
    }

    public void loadMissionDetails(Context context){
        //erase any previous config
        initFields();

        if (activeMission == null|| activeMission.number == -1){
            return;
        }
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_NUMER, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_MEMBERS, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_TYPE, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_OBJECTIVES, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_TEAM_NOTES, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_MISSION_DESC, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_CMD_NAME, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_CMD_LOCATION, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_CMD_GPS, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_RADIO_COMMAND, context, h);
        RadishworksConnector.apiCall(RadishworksConnector.REQUEST_RADIO_TACTICAL, context, h);
    }

    private void setupCallbackHandler(final Context context){
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Toast.makeText(getApplicationContext(), RadishworksConnector.API_REQUESTS[msg.what] + "-/-" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                String failure = RadishworksConnector.apiFailure((String) msg.obj);
                if (failure != null){
                    //Toast.makeText(context, RadishworksConnector.API_REQUESTS[msg.what] + "-/-" + msg.obj, Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (msg.what) {
                    case RadishworksConnector.REQUEST_TEAM_NUMER:
                        DataManager.data.myTeam.number = Integer.parseInt((String) msg.obj);
                        break;
                    case RadishworksConnector.REQUEST_TEAM_MEMBERS:
                        DataManager.data.myTeam.setMembers((String) msg.obj);
                        break;
                    case RadishworksConnector.REQUEST_TEAM_TYPE:
                        DataManager.data.myTeam.type = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_TEAM_OBJECTIVES:
                        DataManager.data.myTeam.objectives = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_TEAM_NOTES:
                        DataManager.data.myTeam.notes = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_MISSION_DESC:
                        DataManager.data.activeMission.description = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_CMD_NAME:
                        DataManager.data.activeMission.commandPostName = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_CMD_LOCATION:
                        DataManager.data.activeMission.commandPostLocation = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_CMD_GPS:
                        DataManager.data.activeMission.commandPostGPSCoords = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_RADIO_COMMAND:
                        DataManager.data.activeMission.radioCommandChannel = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_RADIO_TACTICAL:
                        DataManager.data.activeMission.radioTacticalChannel = (String) msg.obj;
                        break;
                    case RadishworksConnector.REQUEST_SUBJECT_LIST:
                        DataManager.data.subjects = Subject.parseText((String) msg.obj);
                        break;
                }
                switch (msg.what) {
                    case RadishworksConnector.REQUEST_TEAM_NUMER:
                    case RadishworksConnector.REQUEST_TEAM_MEMBERS:
                    case RadishworksConnector.REQUEST_TEAM_TYPE:
                    case RadishworksConnector.REQUEST_TEAM_OBJECTIVES:
                    case RadishworksConnector.REQUEST_TEAM_NOTES:
                    case RadishworksConnector.REQUEST_MISSION_DESC:
                    case RadishworksConnector.REQUEST_CMD_NAME:
                    case RadishworksConnector.REQUEST_CMD_LOCATION:
                    case RadishworksConnector.REQUEST_CMD_GPS:
                    case RadishworksConnector.REQUEST_RADIO_COMMAND:
                    case RadishworksConnector.REQUEST_RADIO_TACTICAL:
                        if (InfoActivity.info != null){
                            InfoActivity.info.refreshViews();
                        }
                        break;
                    case RadishworksConnector.REQUEST_SUBJECT_LIST:
                        if (SubjectsActivity.subjects != null){
                            SubjectsActivity.subjects.makeSubjectList(DataManager.data.subjects);
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

}
