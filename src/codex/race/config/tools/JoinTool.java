/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config.tools;

import codex.race.config.tools.PlayerBuildTool;
import codex.race.DriverInputScheme;
import codex.race.Functions;
import codex.race.Player;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 *
 * @author gary
 */
public class JoinTool extends PlayerBuildTool implements StateFunctionListener {
    
    public JoinTool() {
        super(true);
    }
    public JoinTool(boolean enable) {
        super(enable);
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
    public void onPlayerAdded(Player p) {}
    @Override
    public void onPlayerRemoved(Player p) {}
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (!enabled) return;
        joinPlayerOf(func);
    }
    
    private void joinPlayerOf(FunctionId func) {
        DriverInputScheme scheme = getInputSchemeOf(func);
        if (!state.getPlayers().stream().anyMatch(p -> p.getInputScheme() == scheme)) {
            Player p = createPlayer();
            p.setInputScheme(scheme);
            state.getPlayers().add(p);
        }
    }
    private DriverInputScheme getInputSchemeOf(FunctionId func) {
        for (DriverInputScheme s : Functions.SCHEMES) {
            if (s.contains(func)) {
                return s;
            }
        }
        return null;
    }
    private Player createPlayer() {
        return new Player();
    }
    
}
