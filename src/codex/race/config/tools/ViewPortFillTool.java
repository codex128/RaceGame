/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config.tools;

import codex.race.Player;
import codex.race.config.PlayerConfigWindow;
import com.jme3.scene.Node;

/**
 *
 * @author gary
 */
public class ViewPortFillTool extends PlayerBuildTool {

    public ViewPortFillTool() {
        super(true);
    }
    public ViewPortFillTool(boolean enabled) {
        super(enabled);
    }    
    
    @Override
    protected void init() {}
    @Override
    protected void cleanup() {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {}
    @Override
    public void onPlayerAdded(Player p) {
        for (PlayerConfigWindow w : state.getWindows()) {
            if (w.getPlayer() == null) {
                w.setPlayer(p);
            }
        }
    }
    @Override
    public void onPlayerRemoved(Player p) {
        for (PlayerConfigWindow w : state.getWindows()) {
            if (w.getPlayer() == p) {
                w.setPlayer(null);
            }
        }
    }
    
}
