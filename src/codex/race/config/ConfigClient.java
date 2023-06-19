/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.race.config;

import codex.j3map.J3map;
import codex.race.Player;
import java.util.List;

/**
 *
 * @author gary
 */
public interface ConfigClient {
    
    public void recieveConfiguredPlayers(List<Player> players);
    public void recieveSelectedRaceData(J3map raceData);
    public void configFinished(ConfigState config);
    
}
