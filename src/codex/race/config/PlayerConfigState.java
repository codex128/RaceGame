/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.j3map.J3map;
import codex.race.DriverInputScheme;
import codex.race.Functions;
import codex.race.GameAppState;
import codex.race.Player;
import codex.race.ViewWindow;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class PlayerConfigState extends ConfigState implements StateFunctionListener {
    
    private static final String
            INPUT_GROUP = PlayerConfigState.class.getName()+"(input-group)";
    private static final FunctionId
            F_READY = new FunctionId(INPUT_GROUP, "ready");
    
    PlayerConfigWindow[] windows = new PlayerConfigWindow[4];
    ArrayList<DriverInputScheme> availableSchemes = new ArrayList<>();
    LinkedList<Player> players = new LinkedList<>();
    J3map commonCar;
    
    @Override
    protected void init(Application app) {
        
        initWindows();
        initInputs();
        commonCar = (J3map)assetManager.loadAsset("Properties/cars/MyCar.j3map");
        
    }
    @Override
    protected void cleanup(Application app) {
        inputMapper.removeStateListener(this, F_READY);
        inputMapper.deactivateGroup(INPUT_GROUP);
        for (PlayerConfigWindow w : windows) {
            w.cleanup(renderManager);
        }
        for (DriverInputScheme s : Functions.SCHEMES) {
            inputMapper.removeStateListener(this, s.getFunctions());
            s.activateGroup(inputMapper, false);
        }
        availableSchemes.clear();
        players.clear();
        windows = null;
    }    
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        for (PlayerConfigWindow w : windows) {
            w.updateNodeStates(tpf);
        }
    }
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func == F_READY && value != InputState.Off && !players.isEmpty()) {
            exportPlayers();
            client.configFinished(this);
        }
        else if (value != InputState.Off) {
            joinPlayer(func);
        }
    }
    
    private void initWindows() {
        for (int i = 0; i < windows.length; i++) {
            PlayerConfigWindow w = windows[i] = new PlayerConfigWindow(i);
            w.initialize(renderManager, cam.clone(), guiViewPort.getCamera().clone());
            w.setCameraSize(windowSize.x/2f, windowSize.y);
        }
        windows[0].setViewPortSize(new ViewWindow(0f, .25f, .5f, 1f));
        windows[1].setViewPortSize(new ViewWindow(.75f, 1f, .5f, 1f));
        windows[2].setViewPortSize(new ViewWindow(0f, .25f, 0f, .5f));
        windows[3].setViewPortSize(new ViewWindow(.75f, 1f, 0f, .5f));
    }
    private void initInputs() {
        inputMapper.addStateListener(this, F_READY);
        inputMapper.activateGroup(INPUT_GROUP);
        for (DriverInputScheme s : Functions.SCHEMES) {
            availableSchemes.add(s);
            inputMapper.addStateListener(this, s.getFunctions());
            s.activateGroup(inputMapper, true);
        }
    }
    
    private void joinPlayer(FunctionId func) {
        if (players.size() == windows.length) return;
        DriverInputScheme scheme = getAvailableOwnerOf(func);
        if (scheme != null) {
            PlayerConfigWindow w = getNextEmptyWindow();
            if (w != null) {
                availableSchemes.remove(scheme);
                Player p = new Player(players.size()+1);
                players.add(p);
                PlayerConfigVisual v = new PlayerConfigVisual(p, factory);
                v.setCarData(commonCar);
                //v.setCarColor("red");
                v.connect(w);
            }
        }
    }
    private DriverInputScheme getAvailableOwnerOf(FunctionId func) {
        return availableSchemes.stream().filter(s -> s.contains(func)).findAny().orElse(null);
    }
    private PlayerConfigWindow getNextEmptyWindow() {
        for (PlayerConfigWindow w : windows) {
            if (w.isEmpty()) return w;
        }
        return null;
    }
    private void exportPlayers() {
        int index = 0;
        for (PlayerConfigWindow w : windows) {
            if (!w.isEmpty()) {
                Player p = w.getConfigVisual().getPlayer();
                p.setViewPortNumber(index);
            }
            index++;
        }
        client.recieveConfiguredPlayers(players);
    }
    
    public static void initializeMappings(InputMapper im) {
        im.map(F_READY, KeyInput.KEY_RETURN);
    }
    
}
