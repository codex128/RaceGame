/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.j3map.J3map;
import codex.race.GameFactory;
import codex.race.Player;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Label;


/**
 *
 * @author gary
 */
public class PlayerConfigVisual {
    
    private static final String NAME_PREFIX = "PlayerConfigVisual-";
    
    private final Player player;
    private final GameFactory factory;
    private Node scene, gui;
    private PlayerConfigWindow window;
    
    public PlayerConfigVisual(Player player, GameFactory factory) {
        this.player = player;
        this.factory = factory;
        initializeVisuals();
        loadNativeProperties();
    }
    
    private void initializeVisuals() {
        Label pnum = new Label("P");
        pnum.setFontSize(40f);
        pnum.setLocalTranslation(5f, 80f, 0);
        attachGui(pnum, "pnum");
        Label controls = new Label("Control: "+player.getInputScheme().getId());
        controls.setFontSize(30f);
        controls.setLocalTranslation(5f, 35f, 0f);
        attachGui(controls, "controls");
    }
    private void loadNativeProperties() {
        if (player.getCarColor() == null) {
            throw new NullPointerException("Missing car color!");
        }
        if (player.getCarData() != null) {
            setCarData(player.getCarData());
        }
        setPlayerNumber(player.getPlayerNumber());
    }
    
    public void connect(PlayerConfigWindow w) {
        if (window != null) return;
        window = w;
        window.setConfigVisual(this);
        window.getSceneNode().attachChild(scene);
        window.getGuiNode().attachChild(gui);
    }
    public void disconnect() {
        if (window == null) return;
        scene.removeFromParent();
        gui.removeFromParent();
        window.setConfigVisual(null);
        window = null;
    }
    
    public void setCarData(J3map carData) {
        assert carData != null;
        detach("car");
        player.setCarData(carData);
        Spatial model = factory.createCarModel(player.getCarData(), player.getCarColor());
        attach(model, "car");
    }
    public void setCarColor(String color) {
        assert color != null;
        player.setCarColor(color);
        Spatial car = getSpatial("car");
        if (car != null) {
            car.setMaterial(factory.createCarMaterial(player.getCarData(), player.getCarColor()));
        }
    }
    public void setPlayerNumber(int n) {
        player.setPlayerNumber(n);
        Label pnum = (Label)getGuiSpatial("pnum");
        if (pnum != null) {
            pnum.setText("P"+n);
        }
    }
    
    private void attach(Spatial spatial, String name) {
        spatial.setName(NAME_PREFIX+name);
        scene.attachChild(spatial);
    }
    private void attachGui(Spatial spatial, String name) {
        spatial.setName(NAME_PREFIX+name);
        gui.attachChild(spatial);
    }
    private void detach(String name) {
        scene.detachChildNamed(NAME_PREFIX+name);
    }
    private void detachGui(String name) {
        gui.detachChildNamed(NAME_PREFIX+name);
    }
    private Spatial getSpatial(String name) {
        return scene.getChild(NAME_PREFIX+name);
    }
    private Spatial getGuiSpatial(String name) {
        return gui.getChild(NAME_PREFIX+name);
    }

    public Player getPlayer() {
        return player;
    }
    public Node getScene() {
        return scene;
    }
    public Node getGui() {
        return gui;
    }
    public PlayerConfigWindow getConfigWindow() {
        return window;
    }    
    public boolean isConnected() {
        return window != null;
    }
    
}
