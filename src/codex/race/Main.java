package codex.race;

import codex.j3map.J3map;
import codex.j3map.J3mapFactory;
import codex.j3map.processors.BooleanProcessor;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.IntegerProcessor;
import codex.j3map.processors.J3mapImporter;
import codex.j3map.processors.StringProcessor;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
	
	public static final ColorRGBA SKY_COLOR = new ColorRGBA(.1f, .1f, .1f, 1f);
	
	
	public Main() {
		super((AppState) null);
	}
	
    public static void main(String[] args) {
        Main app = new Main();
		AppSettings as = new AppSettings(true);
		as.setUseJoysticks(true);
		app.setSettings(as);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
		GuiGlobals.initialize(this);
		GuiGlobals.getInstance().setCursorEventsEnabled(false);
		Functions.initialize(GuiGlobals.getInstance().getInputMapper());
		
		assetManager.registerLoader(J3mapFactory.class, "j3map");
		J3mapFactory.registerAllProcessors(
				BooleanProcessor.class,
				StringProcessor.class,
				FloatProcessor.class,
				IntegerProcessor.class);
		J3mapFactory.registerProcessor(new J3mapImporter(assetManager));
		
		cam.setFrustumPerspective(120f, cam.getAspect(), .5f, 100f);
		
		BulletAppState bulletapp = new BulletAppState();
		//bulletapp.setDebugEnabled(true);
		stateManager.attach(bulletapp);
		
		stateManager.attach(new GrandPrixState(
				(J3map)assetManager.loadAsset("Properties/races/GrandPrix1.j3map")));
		
		// disable basic viewports
		viewPort.setBackgroundColor(SKY_COLOR);
		viewPort.detachScene(rootNode);
		guiViewPort.detachScene(guiNode);
		
    }
    @Override
    public void simpleUpdate(float tpf) {		
		
	}
    @Override
    public void simpleRender(RenderManager rm) {}
	
}
