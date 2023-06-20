/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.j3map.J3map;
import codex.race.DriverInputScheme;
import codex.race.Functions;
import codex.race.GameFactory;
import codex.race.Player;
import codex.race.ViewWindow;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector4f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
    ArrayList<String> availableColors = new ArrayList<>();
    LinkedList<Player> players = new LinkedList<>();
    J3map commonCar;
    Node gui = new Node("gui");
    
    @Override
    protected void init(Application app) {
        
        availableColors.addAll(Arrays.asList(GameFactory.COLORS));
        
        initGui();
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
        renderManager.removePostView(PlayerConfigState.class.getName()+"(gui)");
        availableSchemes.clear();
        availableColors.clear();
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
        gui.updateLogicalState(tpf);
        gui.updateGeometricState();
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
    
    private void initGui() {
        gui.setCullHint(Spatial.CullHint.Never);
        gui.setQueueBucket(RenderQueue.Bucket.Gui);
        ViewPort vp = renderManager.createPostView(PlayerConfigState.class.getName()+"(gui)", guiViewPort.getCamera().clone());
        vp.attachScene(gui);
        //vp.setClearFlags(true, true, true);
        vp.setBackgroundColor(ColorRGBA.randomColor());
        new ViewWindow(new Vector4f(.5f, 1.5f, 0f, 1f)).applyToCamera(vp.getCamera());
        vp.getCamera().resize((int)windowSize.x/2, (int)windowSize.y, true);
    }
    private void initWindows() {
        for (int i = 0; i < windows.length; i++) {
            PlayerConfigWindow w = windows[i] = new PlayerConfigWindow(i);
            w.initialize(renderManager, cam.clone(), guiViewPort.getCamera().clone());
            w.setCameraSize(windowSize.x/2f, windowSize.y);
        }
        windows[0].setViewPortSize(new ViewWindow(0f, .5f, .5f, 1f));
        windows[1].setViewPortSize(new ViewWindow(1.5f, 2f, .5f, 1f));
        windows[2].setViewPortSize(new ViewWindow(0f, .5f, 0f, .5f));
        windows[3].setViewPortSize(new ViewWindow(1.5f, 2f, 0f, .5f));
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
                p.setInputScheme(scheme);
                players.add(p);
                PlayerConfigVisual v = new PlayerConfigVisual(p, factory);
                v.setCarData(commonCar);
                v.setCarColor(availableColors.remove(0));
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
                System.out.println("there is a player");
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
