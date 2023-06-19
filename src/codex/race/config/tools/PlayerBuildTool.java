/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config.tools;

import codex.race.Player;
import codex.race.config.PlayerConfigState;
import com.jme3.asset.AssetManager;

/**
 *
 * @author gary
 */
public abstract class PlayerBuildTool {
    
    protected PlayerConfigState state;
    protected AssetManager assetManager;
    protected boolean enabled;
    
    public PlayerBuildTool() {
        this(false);
    }
    public PlayerBuildTool(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void initialize(PlayerConfigState state) {
        this.state = state;
        assetManager = this.state.getAssetManager();
        init();
        if (enabled) onEnable();
    }
    public void clean() {
        if (enabled) {
            enabled = false;
            onDisable();
        }
        cleanup();
        this.state = null;
    }
    public void setEnabled(boolean enable) {
        if (!enabled && enable) {
            enabled = true;
            onEnable();
        }
        else if (enabled && !enable) {
            enabled = false;
            onDisable();
        }
    }
    
    protected abstract void init();
    protected abstract void cleanup();
    protected abstract void onEnable();
    protected abstract void onDisable();
    public abstract void update(float tpf);
    public abstract void onPlayerAdded(Player p);
    public abstract void onPlayerRemoved(Player p);
    
    public boolean isEnabled() {
        return enabled;
    }
    
}
