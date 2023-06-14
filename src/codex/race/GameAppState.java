/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.jmeutil.assets.AssetCacheState;
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

/**
 *
 * @author gary
 */
public abstract class GameAppState extends BaseAppState {
	
	protected AssetManager assetManager;
	protected RenderManager renderManager;
	protected InputManager inputManager;
	protected Camera cam;
	protected ViewPort viewPort, guiViewPort;
	protected Vector2f windowSize;
	protected BulletAppState bullet;
	protected Node rootNode, guiNode;
	
	@Override
	protected void initialize(Application app) {
		assetManager = app.getAssetManager();
		renderManager = app.getRenderManager();
		inputManager = app.getInputManager();
		cam = app.getCamera();
		viewPort = app.getViewPort();
		guiViewPort = app.getGuiViewPort();
		windowSize = new Vector2f(app.getContext().getSettings().getWidth(), app.getContext().getSettings().getHeight());
		bullet = getState(BulletAppState.class);
		if (app instanceof SimpleApplication) {
			SimpleApplication sa = (SimpleApplication)app;
			rootNode = sa.getRootNode();
			guiNode = sa.getGuiNode();
		}
		init(app);
	}
	protected abstract void init(Application app);
	
	protected PhysicsSpace getPhysicsSpace() {
		if (bullet == null) return null;
		else return bullet.getPhysicsSpace();
	}
	
}
