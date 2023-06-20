/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import com.jme3.app.Application;

/**
 *
 * @author gary
 */
public class ConfigManager extends ConfigState {
    
    final ConfigState[] configs;
    ConfigState current;
    int index = -1;
    
    public ConfigManager(ConfigClient client, ConfigState... configs) {
        setClient(client);
        this.configs = configs;
        setClientOfChildren();
    }
    
    @Override
    protected void init(Application app) {
        advance();
    }
    @Override
    protected void cleanup(Application app) {
        if (current != null) {
            getStateManager().detach(current);
            index = configs.length;
        }
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
    private void setClientOfChildren() {        
        for (ConfigState c : configs) {
            c.setClient(client);
        }
    }
    
    public void advance() {
        if (current != null) {
            getStateManager().detach(current);
        }
        index++;
        if (index < configs.length) {
            current = configs[index];
            getApplication().enqueue(() -> getStateManager().attach(current));
        }
        else {
            current = null;
            client.allConfigsFinished();
        }
    }
    public void setIndex(int i) {
        assert i >= 0 && i < configs.length;
        index = i;
    }
    
    public ConfigState[] getConfigs() {
        return configs;
    }
    public ConfigState getCurrent() {
        return current;
    }
    public int getIndex() {
        return index;
    }
    
}
