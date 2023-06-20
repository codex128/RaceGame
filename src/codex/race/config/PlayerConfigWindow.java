/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race.config;

import codex.race.ViewWindow;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 *
 * @author gary
 */
public class PlayerConfigWindow {
    
    private static final String VIEWPORT_ID = "player-config-window-n";
    
    private final int vpn;
    private ViewPort viewPort, guiViewPort;
    private Node scene, gui;
    private PlayerConfigVisual visual;
    
    public PlayerConfigWindow(int vpn) {
        this.vpn = vpn;
    }

    public void initialize(RenderManager rm, Camera sceneCam, Camera guiCam) {
        viewPort = rm.createMainView(VIEWPORT_ID+vpn, sceneCam);
        viewPort.attachScene(scene);
        guiViewPort = rm.createPostView(VIEWPORT_ID+vpn+"-gui", guiCam);
        guiViewPort.attachScene(gui);
        //guiViewPort.setClearFlags(false, false, false);
        guiViewPort.setBackgroundColor(ColorRGBA.randomColor());
    }
    public void setViewPortSize(ViewWindow view) {
        view.applyToCamera(viewPort.getCamera());
        view.applyToCamera(guiViewPort.getCamera());
    }
    public void setCameraSize(float width, float height) {
        viewPort.getCamera().resize((int)width, (int)height, true);
    }
    public void cleanup(RenderManager rm) {
        rm.removeMainView(viewPort);
        rm.removePostView(guiViewPort);
        viewPort = null;
        guiViewPort = null;
    }
    
    public void updateNodeStates(float tpf) {
        if (viewPort != null) {
            scene.updateLogicalState(tpf);
            scene.updateGeometricState();
        }
        if (guiViewPort != null) {
            gui.updateLogicalState(tpf);
            gui.updateGeometricState();
        }
    }
    protected void setConfigVisual(PlayerConfigVisual visual) {
        this.visual = visual;
    }
    
    public int getViewPortNumber() {
        return vpn;
    }
    public ViewPort getViewPort() {
        return viewPort;
    }
    public ViewPort getGuiViewPort() {
        return guiViewPort;
    }
    public Node getSceneNode() {
        return scene;
    }
    public Node getGuiNode() {
        return gui;
    }
    public PlayerConfigVisual getConfigVisual() {
        return visual;
    }
    public boolean isEmpty() {
        return visual == null;
    }
    
}
