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
