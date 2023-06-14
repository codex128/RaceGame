/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import codex.jmeutil.Timer;
import codex.jmeutil.TimerListener;
import codex.jmeutil.audio.AudioModel;
import codex.jmeutil.audio.SFXSpeaker;
import codex.jmeutil.listen.Listenable;
import codex.jmeutil.scene.SceneGraphIterator;
import com.jme3.app.Application;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gary
 */
public class RaceState extends GameAppState implements DriverListener,
		Listenable<RaceListener>, TimerListener {
	
	Node scene = new Node("race-scene");
	J3map trackData;
	Player[] players;
	Spatial track;
	ArrayList<LapTrigger> triggers = new ArrayList<>();
	LinkedList<RaceListener> listeners = new LinkedList<>();
	Driver[] drivers;
	int numLaps = -1;
	float deathzone;
	int driversFinished = 0;
    Timer countdown = new Timer(3f);
	Timer afterward = new Timer(5f);
	SFXSpeaker music;
    SFXSpeaker raceStart;
	
	public RaceState() {}
	public RaceState(J3map trackData, Player[] players) {
		this.trackData = trackData;
		this.players = players;
	}
	public RaceState(J3map trackData, Player[] players, int laps) {
		this(trackData, players);
		numLaps = laps;
	}
	
	@Override
	protected void init(Application app) {
		rootNode.attachChild(scene);		
		scene.addLight(new DirectionalLight(new Vector3f(0f, -1f, 0f), ColorRGBA.LightGray));
		//scene.addLight(new DirectionalLight(new Vector3f(1f, -1f, 1f), ColorRGBA.DarkGray));
		//scene.addLight(new DirectionalLight(new Vector3f(-1f, -1f, -1f), ColorRGBA.DarkGray));
		if (trackData != null && players != null) {
			load(trackData, players);
			trackData = null;
			players = null;
		}
        countdown.setCycleMode(Timer.CycleMode.ONCE);
        countdown.addListener(this);
		afterward.setCycleMode(Timer.CycleMode.ONCE);
		afterward.addListener(this);
        raceStart = new SFXSpeaker(assetManager, new AudioModel((J3map)assetManager.loadAsset("Properties/sounds/start_race.j3map")));
    }
	@Override
	protected void cleanup(Application app) {
		scene.removeFromParent();
		scene.detachAllChildren();
		if (getPhysicsSpace() != null) {
			for (Spatial spatial : new SceneGraphIterator(track)) {
				RigidBodyControl rigidbody = spatial.getControl(RigidBodyControl.class);
				if (rigidbody != null) getPhysicsSpace().remove(rigidbody);
			}
			for (Driver d : drivers) {
				getPhysicsSpace().remove(d.getVehicle());
			}
		}
		for (Driver d : drivers) {
			d.cleanupViewPorts(renderManager);
			d.cleanupInputs(GuiGlobals.getInstance().getInputMapper());
			inputManager.removeRawInputListener(d);
		}
		drivers = null;
		clearAllListeners();
		afterward.clearAllListeners();
		music.stop();
	}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
	@Override
	public void update(float tpf) {
		for (Driver d : drivers) {
			d.update(tpf);
			d.detectLapTriggers(triggers, numLaps);
			if (d.getVehicle().getPhysicsLocation().y < deathzone) {
				int i = wrap(d.getNextTriggerIndex()-1, 0, triggers.size()-1);
				Spatial s = triggers.get(i).getSpatial();
				d.warp(s.getWorldTranslation(), s.getWorldRotation());
			}
			d.updateNodeStates(tpf);
		}
        countdown.update(tpf);
		afterward.update(tpf);
	}
	@Override
	public void onDriverFinish(Driver driver) {
		driversFinished++;
		Label label = new Label(driversFinished+getNumberSuffix(driversFinished));
		driver.getGui().attachChild(label);
		label.setFontSize(50f);
		label.setColor(ColorRGBA.White);
		label.setLocalTranslation(windowSize.x/2f, windowSize.y/2f, 0f);
		if (driversFinished == drivers.length
				|| (drivers.length >= 2 && driversFinished >= drivers.length-1)) {
			System.out.println("start afterward timer");
			afterward.start();
		}
	}
	@Override
	public void onTimerFinish(Timer timer) {
        if (timer == countdown) {
            raceStart.play();
            for (Driver d : drivers) {
                d.getVehicle().setLinearFactor(Vector3f.UNIT_XYZ);
                d.getVehicle().setAngularFactor(Vector3f.UNIT_XYZ);
            }
        }
        else if (timer == afterward) {
			notifyListeners(l -> l.onRaceComplete(this));
		}
	}
	@Override
	public Collection<RaceListener> getListeners() {
		return listeners;
	}
	
	public void load(J3map trackData, Player... players) {
		load(trackData, Arrays.asList(players));
	}
	public void load(J3map trackData, List<Player> players) {
		assert players.size() >= 1 && players.size() <= 4;
		
		track = assetManager.loadModel(trackData.getString("model"));
		//track.setMaterial(assetManager.loadMaterial("Materials/track_material.j3m"));
		scene.attachChild(track);
		
		deathzone = trackData.getFloat("deathzone", -20f);
		if (numLaps < 0) numLaps = trackData.getInteger("laps", 3);
		
		// music
		music = new SFXSpeaker(assetManager, new AudioModel(trackData.getJ3map("music")));
		music.play();
		
		ArrayList<Transform> starts = new ArrayList<>();
		SceneGraphIterator it = new SceneGraphIterator(track);
		for (Spatial spatial : it) {
			if (spatial.getName().startsWith("start")) {
				starts.add(spatial.getWorldTransform());
				it.ignoreChildren();
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
				it.ignoreChildren();
				continue;
			}
			Double mass = spatial.getUserData("mass");
			if (spatial instanceof Geometry || mass != null) {
				RigidBodyControl rigidbody = new RigidBodyControl(mass != null ? mass.floatValue() : 0f);
				spatial.addControl(rigidbody);
				getPhysicsSpace().add(rigidbody);
				it.ignoreChildren();
			}
		}
		
		drivers = new Driver[players.size()];		
		drivers[0] = createP1(players.get(0), drivers.length);
		if (drivers.length >= 2) {
			drivers[1] = createP2(players.get(1), drivers.length);
		}
		if (drivers.length >= 3) {
			drivers[2] = createP3(players.get(2), drivers.length);
		}
		if (drivers.length >= 4) {
			drivers[3] = createP4(players.get(3), drivers.length);
		}
		
		Iterator<Transform> s = starts.iterator();
		Transform t = new Transform();
		for (int i = 0; i < drivers.length && s.hasNext(); i++) {
			scene.attachChild(drivers[i].getVehicle().getSpatial());
			getPhysicsSpace().add(drivers[i].getVehicle());
            drivers[i].getVehicle().setLinearFactor(Vector3f.UNIT_Y);
            drivers[i].getVehicle().setAngularFactor(new Vector3f(.5f, 0f, .5f));
			t.set(s.next());
			drivers[i].configureGui(assetManager, windowSize);
			drivers[i].warp(t.getTranslation(), t.getRotation());
			Light[] lights = drivers[i].createHeadlights();
			for (Light l : lights) scene.addLight(l);
			drivers[i].addListener(this);
		}
        
        countdown.start();
		
	}
	
	private Driver createP1(Player player, int n) {
		Driver d = new Driver(player);
		VehicleControl v = d.createVehicle(assetManager);
		v.getSpatial().setMaterial(assetManager.loadMaterial("Materials/red_car_material.j3m"));
		d.setCamera(cam);
		//d.setAccelForce(2000f);
		d.createGameViewPort(renderManager, cam).attachScene(scene);
		d.createGuiViewPort(renderManager, guiViewPort.getCamera());
		d.setViewSize(getViewSize(0, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(), Functions.ARROW_KEYS);
		return d;
	}
	private Driver createP2(Player player, int n) {
		Driver d = new Driver(player);
		d.createVehicle(assetManager).getSpatial().setMaterial(assetManager.loadMaterial("Materials/blue_car_material.j3m"));
		d.createGameViewPort(renderManager, cam).attachScene(scene);
		d.createGuiViewPort(renderManager, guiViewPort.getCamera());
		d.setViewSize(getViewSize(1, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(), Functions.WASD);
		return d;
	}
	private Driver createP3(Player player, int n) {
		Driver d = new Driver(player);
		d.createVehicle(assetManager).getSpatial().setMaterial(assetManager.loadMaterial("Materials/green_car_material.j3m"));
		d.createGameViewPort(renderManager, cam).attachScene(scene);
		d.createGuiViewPort(renderManager, guiViewPort.getCamera());
		d.setViewSize(getViewSize(2, n));
		d.initializeInputs(GuiGlobals.getInstance().getInputMapper(), Functions.IJKL);
		return d;
	}
	private Driver createP4(Player player, int n) {
		Driver d = new Driver(player);
		d.createVehicle(assetManager).getSpatial().setMaterial(assetManager.loadMaterial("Materials/yellow_car_material.j3m"));
		d.createGameViewPort(renderManager, cam).attachScene(scene);
		d.createGuiViewPort(renderManager, guiViewPort.getCamera());
		d.setViewSize(getViewSize(3, n));
		//d.initializeInputs(GuiGlobals.getInstance().getInputMapper(),
		//		KeyInput.KEY_NUMPAD5, KeyInput.KEY_NUMPAD1, KeyInput.KEY_NUMPAD7, KeyInput.KEY_NUMPAD4, KeyInput.KEY_NUMPADENTER, KeyInput.KEY_NUMPAD2, KeyInput.KEY_NUMPAD3);
		inputManager.addRawInputListener(d);
		return d;
	}
	
	private static Vector4f getViewSize(int i, int n) {
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
	private static String getNumberSuffix(int n) {
		switch (n) {
			case 1: return "st";
			case 2: return "nd";
			case 3: return "rd";
			default: return "th";
		}
	}	
	private static int wrap(int n, int min, int max) {
		if (n > max) return min+(n-max-1);
		else if (n < min) return max-(min-n-1);
		else return n;
	}
	
}
