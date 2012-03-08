package org.zmonkey.beacon;

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
}
