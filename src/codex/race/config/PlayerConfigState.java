/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.j3map.J3map;
import codex.race.DriverInputScheme;
import codex.race.Functions;
import codex.race.Player;
import codex.race.ViewWindow;
import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.ArrayList;

/**
 *
 * @author gary
 */
public class PlayerConfigState extends ConfigState implements StateFunctionListener {
    
    private static final String[]
            COLORS = "red/blue/green/yellow".split("/");
    private static final FunctionId
            F_ADVANCE = new FunctionId(PlayerConfigState.class.getName(), "advance");
    
    final int maxPlayers;
    ArrayList<Player> players = new ArrayList<>();
    PlayerConfigWindow[] windows;
    J3map commonCar;
    
	public PlayerConfigState(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        if (this.maxPlayers >= COLORS.length) {
            throw new IllegalArgumentException("Not enough colors for "+this.maxPlayers+" player(s)!");
        }
    }
	
	@Override
	protected void init(Application app) {
        
        commonCar = (J3map)assetManager.loadAsset("Properties/cars/MyCar.j3map");
        initViewPorts();
        
        inputMapper.addStateListener(this, F_ADVANCE);
        inputMapper.activateGroup(PlayerConfigState.class.getName());
        
    }
	@Override
	protected void cleanup(Application app) {
        inputMapper.removeStateListener(this, F_ADVANCE);
        inputMapper.deactivateGroup(PlayerConfigState.class.getName());
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
        joinPlayerOf(func);
    }
    
    private void initViewPorts() {
        windows = new PlayerConfigWindow[maxPlayers];
        Camera mainCam = cam.clone();
        mainCam.setFov(50f);
        Camera guiCam = guiViewPort.getCamera().clone();
        for (int i = 0; i < windows.length && i < COLORS.length; i++) {
            PlayerConfigWindow w = windows[i] = new PlayerConfigWindow(COLORS[i]);
            w.initialize(renderManager, mainCam, guiCam, new ViewWindow(i, maxPlayers));
        }
    }
    
    private void joinPlayerOf(FunctionId func) {
        DriverInputScheme scheme = getInputSchemeOf(func);
        if (!players.stream().anyMatch(p -> p.getInputScheme() == scheme)) {
            Player p = createPlayer();
            addPlayer(p);
            p.setPlayerNumber(players.size());
            p.setInputScheme(scheme);
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
    private void addPlayer(Player p) {
        if (players.size() == maxPlayers) return;
        players.add(p);
        for (PlayerConfigWindow w : windows) {
            if (w.getPlayer() == null) {
                w.setPlayer(p);
                initVisuals(w);
                return;
            }
        }
        throw new NullPointerException("All viewports filled, cannot properly add player!");
    }
    private void initVisuals(PlayerConfigWindow window) {
        window.getPlayer().setCarData(commonCar);
        J3map carData = window.getPlayer().getCarData();
        Spatial model = assetManager.loadModel(carData.getString("model"));
        Material mat = assetManager.loadMaterial(carData.getString("material"));
        Texture tex = assetManager.loadTexture(new TextureKey(carData.getString("texture").replace((CharSequence)"$", window.getColor())));
        mat.setTexture("DiffuseMap", tex);
        model.setMaterial(mat);
        window.getScene().attachChild(model);
        window.getScene().addLight(new DirectionalLight(new Vector3f(1f, -1f, 1f)));
        window.getScene().addLight(new AmbientLight(ColorRGBA.Gray));
    }    
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public PlayerConfigWindow[] getWindows() {
        return windows;
    }
    public PlayerConfigWindow getPlayerWindow(Player p) {
        for (PlayerConfigWindow w : windows) {
            if (w.getPlayer() == p) return w;
        }
        return null;
    }
	
}
