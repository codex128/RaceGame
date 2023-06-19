/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.race.GameAppState;

/**
 *
 * @author gary
 */
public abstract class ConfigState extends GameAppState {
    
    protected ConfigClient client;
    
    protected void setClient(ConfigClient client) {
        this.client = client;
    }
    public ConfigClient getClient() {
        return client;
    }
    
}
