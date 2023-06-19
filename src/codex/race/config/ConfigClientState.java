/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.race.GameAppState;
import com.jme3.app.Application;


/**
 *
 * @author gary
 */
public abstract class ConfigClientState extends GameAppState implements ConfigClient {
    
    protected ConfigState[] configs;
    private int index = 0;
    
    public ConfigClientState() {}
    public ConfigClientState(ConfigState... config) {
        this.configs = config;
        for (ConfigState c : this.configs) {
            c.setClient(this);
        }
    }
    
    @Override
    protected void init(Application app) {
        if (configs.length > 0) {
            getStateManager().attach(configs[index]);
        }
        else {
            allConfigsComplete();
        }
    }
    @Override
    public void configFinished(ConfigState config) {
        getStateManager().detach(config);
        if (index < configs.length-1) {
            getApplication().enqueue(() -> getStateManager().attach(configs[++index]));
        }
        else {
            allConfigsComplete();
        }
    }
    
    public abstract void allConfigsComplete();
    
    public ConfigState[] getConfigs() {
        return configs;
    }
    public int getConfigIndex() {
        return index;
    }
    
}
