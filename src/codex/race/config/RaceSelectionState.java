/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.j3map.J3map;
import codex.race.GameAppState;
import com.jme3.app.Application;

/**
 *
 * @author gary
 */
public class RaceSelectionState extends ConfigState {
    
    boolean findSingleRace = true;
    
    public RaceSelectionState() {}
    public RaceSelectionState(boolean findSingleRace) {
        this.findSingleRace = findSingleRace;
    }
    
    @Override
    protected void init(Application app) {
    
        // right now, immediately pass a race or grand prix
        // this temporarily bypasses needing a gui for race selection
        if (findSingleRace) {
            client.recieveSelectedRaceData((J3map)assetManager.loadAsset("Properties/races/racetrack1.j3map"));
        }
        else {
            client.recieveSelectedRaceData((J3map)assetManager.loadAsset("Properties/races/GrandPrix1.j3map"));
        }
        client.configFinished(this);
        
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
}
