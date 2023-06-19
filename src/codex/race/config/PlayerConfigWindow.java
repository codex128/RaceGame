/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.race.Player;
import codex.race.ViewWindow;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.simsilica.lemur.Label;

/**
 *
 * @author gary
 */
public class PlayerConfigWindow {
    
    private static final String
            VIEWPORT_ID = "config-window-p",
            GUIVIEWPORT_ID = "config-gui-window-p";
    
    private String color;
    private Player player;
    private ViewPort viewPort, guiViewPort;
    private Node scene, gui;

    public PlayerConfigWindow(String color) {
        this.color = color;
    }
    
    public void initialize(RenderManager rm, Camera mainCam, Camera guiCam, ViewWindow window) {
        viewPort = rm.createMainView(VIEWPORT_ID+player.getViewPortNumber(), mainCam);
        viewPort.attachScene(scene = new Node());
        guiViewPort = rm.createPostView(GUIVIEWPORT_ID+player.getViewPortNumber(), guiCam);
        guiViewPort.attachScene(gui = new Node());
    }
    public void setPlayer(Player p) {
        player = p;
    }
    
    public void updateNodeStates(float tpf) {
        scene.updateLogicalState(tpf);
        scene.updateGeometricState();
        gui.updateLogicalState(tpf);
        gui.updateGeometricState();
    }
    
    public String getColor() {
        return color;
    }
    public Player getPlayer() {
        return player;
    }
    public ViewPort getViewPort() {
        return viewPort;
    }
    public ViewPort getGuiViewPort() {
        return guiViewPort;
    }
    public Node getScene() {
        return scene;
    }
    public Node getGui() {
        return gui;
    }
    
}
