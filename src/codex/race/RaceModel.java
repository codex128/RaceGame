/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import java.util.List;

/**
 *
 * @author gary
 */
public class RaceModel {
    
    J3map trackData;
    List<Player> players;
    int forceLaps = -1;

    public void setTrackData(J3map trackData) {
        this.trackData = trackData;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }    
    public void setForcedLaps(int l) {
        forceLaps = l;
    }
    
    public J3map getTrackData() {
        return trackData;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public int getForcedLaps() {
        return forceLaps;
    }
    
    public boolean isForcingLaps() {
        return forceLaps > 0;
    }
    
}
