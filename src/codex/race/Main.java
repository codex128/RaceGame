package codex.race;

import codex.j3map.J3map;
import codex.j3map.J3mapFactory;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.StringProcessor;
import codex.jmeutil.scene.SceneGraphIterator;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.input.KeyInput;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.InputMapper;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

	BulletAppState bulletapp;
	Driver[] drivers;
	Spatial track;
	final int numPlayers = 4;
	final int numLaps = 5;
	ArrayList<LapTrigger> triggers = new ArrayList<>();
	
	public Main() {
		super((AppState) null);
	}
	
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
		GuiGlobals.initialize(this);
		GuiGlobals.getInstance().setCursorEventsEnabled(false);
		InputMapper im = GuiGlobals.getInstance().getInputMapper();
		
		assetManager.registerLoader(J3mapFactory.class, "j3map");
		J3mapFactory.registerAllProcessors(FloatProcessor.class, StringProcessor.class);
		
		bulletapp = new BulletAppState();
		//bulletapp.setDebugEnabled(true);
		stateManager.attach(bulletapp);
		
		Spatial track = assetManager.loadModel("Models/racetrack.j3o");
		track.setMaterial(assetManager.loadMaterial("Materials/track_material.j3m"));
		rootNode.attachChild(track);
		
		ArrayList<Transform> starts = new ArrayList<>();
		for (Spatial spatial : new SceneGraphIterator(track)) {
			if (spatial.getName().startsWith("start")) {
				starts.add(spatial.getWorldTransform());
				continue;
			}
			Double trigger = spatial.getUserData("trigger");
			if (trigger != null) {
				spatial.setCullHint(Spatial.CullHint.Always);
				int index = trigger.intValue();
				int i = 0;
				for (LapTrigger t : triggers) {
					if (t.getIndex() > index) {
						break;
					}
					i++;
				}
				triggers.add(i, new LapTrigger(spatial, index));
				continue;
			}
			if (spatial.getName().equals("racetrack")) {
				this.track = spatial;
			}
			if (spatial instanceof Geometry) {				
				RigidBodyControl staticbody = new RigidBodyControl(0f);
				spatial.addControl(staticbody);
				getPhysicsSpace().add(staticbody);
			}
		}
		
		rootNode.addLight(new DirectionalLight(new Vector3f(0f, -1f, 0f)));
		rootNode.addLight(new DirectionalLight(new Vector3f(1f, -1f, 1f), ColorRGBA.DarkGray));
		rootNode.addLight(new DirectionalLight(new Vector3f(-1f, -1f, -1f), ColorRGBA.DarkGray));
		
		J3map carData = (J3map)assetManager.loadAsset("Properties/MyCar.j3map");
		drivers = new Driver[numPlayers];
		
		drivers[0] = createP1(carData, "p1", numPlayers);
		if (numPlayers >= 2) {
			drivers[1] = createP2(carData, "p2", numPlayers);
		}
		if (numPlayers >= 3) {
			drivers[2] = createP3(carData, "p3", numPlayers);
		}
		if (numPlayers >= 4) {
			drivers[3] = createP4(carData, "p4", numPlayers);
		}
		
		Iterator<Transform> s = starts.iterator();
		Transform t = new Transform();
		for (int i = 0; i < drivers.length && s.hasNext(); i++) {
			rootNode.attachChild(drivers[i].getVehicle().getSpatial());
			getPhysicsSpace().add(drivers[i].getVehicle());
			t.set(s.next());
			drivers[i].getVehicle().setPhysicsLocation(t.getTranslation());
			drivers[i].getVehicle().setPhysicsRotation(t.getRotation());
		}
		
    }
    @Override
    public void simpleUpdate(float tpf) {
		for (Driver d : drivers) {
			d.update(tpf);
			d.detectLapTriggers(triggers, numLaps);
			d.minimizeCarOcclusion(track);
			if (d.getVehicle().getPhysicsLocation().y < -20f) {
				int i = wrap(d.getNextTriggerIndex()-1, 0, triggers.size()-1);
				Spatial s = triggers.get(i).getSpatial();
				d.getVehicle().setPhysicsLocation(s.getWorldTranslation());
				d.getVehicle().setPhysicsRotation(s.getWorldRotation());
				d.getVehicle().setLinearVelocity(Vector3f.ZERO);
				d.getVehicle().setAngularVelocity(Vector3f.ZERO);
			}
		}
	}
    @Override
    public void simpleRender(RenderManager rm) {}
	
	private Driver createP1(J3map carData, String id, int n) {
		Driver d = new Driver(id);
		VehicleControl v = d.createVehicle(assetManager, carData);
		v.getSpatial().setMaterial(assetManager.loadMaterial("Materials/red_car_material.j3m"));
		d.setCamera(cam);
		d.setViewSize(getViewSize(0, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(),
				KeyInput.KEY_UP, KeyInput.KEY_DOWN, KeyInput.KEY_RCONTROL, KeyInput.KEY_RIGHT, KeyInput.KEY_LEFT);
		return d;
	}
	private Driver createP2(J3map carData, String id, int n) {
		Driver d = new Driver(id);
		d.createVehicle(assetManager, carData).getSpatial().setMaterial(assetManager.loadMaterial("Materials/blue_car_material.j3m"));
		d.createPersonalViewPort(renderManager, cam).attachScene(rootNode);
		d.setViewSize(getViewSize(1, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(),
				KeyInput.KEY_W, KeyInput.KEY_S, KeyInput.KEY_F, KeyInput.KEY_D, KeyInput.KEY_A);
		return d;
	}
	private Driver createP3(J3map carData, String id, int n) {
		Driver d = new Driver(id);
		d.createVehicle(assetManager, carData).getSpatial().setMaterial(assetManager.loadMaterial("Materials/green_car_material.j3m"));
		d.createPersonalViewPort(renderManager, cam).attachScene(rootNode);
		d.setViewSize(getViewSize(2, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(),
				KeyInput.KEY_I, KeyInput.KEY_K, KeyInput.KEY_SEMICOLON, KeyInput.KEY_L, KeyInput.KEY_J);
		return d;
	}
	private Driver createP4(J3map carData, String id, int n) {
		Driver d = new Driver(id);
		d.createVehicle(assetManager, carData).getSpatial().setMaterial(assetManager.loadMaterial("Materials/yellow_car_material.j3m"));
		d.createPersonalViewPort(renderManager, cam).attachScene(rootNode);
		d.setViewSize(getViewSize(3, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(),
				KeyInput.KEY_NUMPAD8, KeyInput.KEY_NUMPAD5, KeyInput.KEY_NUMPADENTER, KeyInput.KEY_NUMPAD6, KeyInput.KEY_NUMPAD4);
		return d;
	}
	private Vector4f getViewSize(int i, int n) {
		assert i < n;
		switch (n) {
			case 1: return new Vector4f(0f, 1f, 0f, 1f);
			case 2: switch (i) {
				case 0:  return new Vector4f(0f, .5f, .25f, .75f);
				default: return new Vector4f(.5f, 1f, .25f, .75f);
			}
			default: switch (i) {
				case 0:  return new Vector4f(0f, .5f, .5f, 1f);
				case 1: return new Vector4f(.5f, 1f, .5f, 1f);
				case 2:  return new Vector4f(0f, .5f, 0f, .5f);
				default:  return new Vector4f(.5f, 1f, 0f, .5f);
			}
		}
	}
	private PhysicsSpace getPhysicsSpace() {
		return bulletapp.getPhysicsSpace();
	}

	private int wrap(int n, int min, int max) {
		if (n > max) return min+(n-max-1);
		else if (n < min) return max-(min-n-1);
		else return n;
	}
	
}
