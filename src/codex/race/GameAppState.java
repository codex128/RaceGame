/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.InputMapper;

/**
 *
 * @author gary
 */
public abstract class GameAppState extends BaseAppState {
	
	protected AssetManager assetManager;
	protected RenderManager renderManager;
	protected InputManager inputManager;
    protected InputMapper inputMapper;
	protected Camera cam;
	protected ViewPort viewPort, guiViewPort;
	protected Vector2f windowSize;
	protected BulletAppState bullet;
    protected GameFactory factory;
	protected Node rootNode, guiNode;
	
	@Override
	protected void initialize(Application app) {
		assetManager = app.getAssetManager();
		renderManager = app.getRenderManager();
		inputManager = app.getInputManager();
        inputMapper = GuiGlobals.getInstance().getInputMapper();
		cam = app.getCamera();
		viewPort = app.getViewPort();
		guiViewPort = app.getGuiViewPort();
		windowSize = new Vector2f(app.getContext().getSettings().getWidth(), app.getContext().getSettings().getHeight());
		bullet = getState(BulletAppState.class);
        factory = getState(GameFactory.class);
		if (app instanceof SimpleApplication) {
			SimpleApplication sa = (SimpleApplication)app;
			rootNode = sa.getRootNode();
			guiNode = sa.getGuiNode();
		}
		init(app);
	}
	protected abstract void init(Application app);

    public AssetManager getAssetManager() {
        return assetManager;
    }
    public RenderManager getRenderManager() {
        return renderManager;
    }
    public InputManager getInputManager() {
        return inputManager;
    }
    public Camera getCam() {
        return cam;
    }
    public ViewPort getGuiViewPort() {
        return guiViewPort;
    }
    public Vector2f getWindowSize() {
        return windowSize;
    }
    public BulletAppState getBullet() {
        return bullet;
    }
    public Node getRootNode() {
        return rootNode;
    }
    public Node getGuiNode() {
        return guiNode;
    }    
	public PhysicsSpace getPhysicsSpace() {
		if (bullet == null) return null;
		else return bullet.getPhysicsSpace();
	}
	
}
