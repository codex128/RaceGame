/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.jmeutil.listen.Listenable;
import codex.jmeutil.scene.SceneGraphIterator;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.collision.CollisionResults;
import com.jme3.light.Light;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gary
 */
public class Driver implements
        AnalogFunctionListener, StateFunctionListener, Listenable<DriverListener> {
	
	private static final Plane VIEW_PLANE = new Plane(Vector3f.UNIT_Y, Vector3f.ZERO);
	
	Player player;
	String id;
	VehicleControl car;
	Camera gameCam;
	Camera guiCam;
	Node gui;
	ViewWindow window;
    Vector3f camLocation;
	float baseAccelForce = 12000f;
	float steerAngle = .5f;
	int accelDirection = 0;
	int nextTrigger = 0;
	int lap = 0;
	boolean finished = false;
	LinkedList<DriverListener> listeners = new LinkedList<>();
	
	public Driver(Player player) {
		this.player = player;
		id = "p"+player.getPlayerNumber();
		baseAccelForce = player.getCarData().getFloat("accelleration", 12000f);
		steerAngle = player.getCarData().getFloat("steeringAngle", .5f);
        camLocation = player.getCarData().getProperty(Vector3f.class, "camera_location");
	}
	
	public VehicleControl createVehicle(GameFactory factory) {        
		car = factory.createVehicle(factory.createCarModel(player.getCarData(), player.getCarColor()), player.getCarData());
        return car;
    }
	public ViewPort createGameViewPort(RenderManager rm, Camera base) {
		gameCam = base.clone();
		ViewPort vp = rm.createMainView(getGameViewPortId(), gameCam);
		vp.setClearFlags(true, true, true);
		vp.setBackgroundColor(Main.SKY_COLOR);
		return vp;
	}
	public ViewPort createGuiViewPort(RenderManager rm, Camera base) {
		guiCam = new Camera(base.getWidth(), base.getHeight());
		guiCam.setParallelProjection(true);
		guiCam.setLocation(new Vector3f(guiCam.getWidth()/2, guiCam.getHeight()/2, 100f));
		gui = new Node(id+"-gui");
		gui.setQueueBucket(RenderQueue.Bucket.Gui);
		gui.setCullHint(Spatial.CullHint.Never);
		ViewPort vp = rm.createPostView(getGuiViewPortId(), guiCam);
		vp.setBackgroundColor(ColorRGBA.randomColor());
//		vp.setClearColor(true);
		vp.attachScene(gui);
//		Label l = new Label("Hello World");
//		gui.attachChild(l);
//		l.setLocalTranslation(300, 300, 0);
//		l.setFontSize(50f);
		return vp;
	}
	public void configureGui(AssetManager assets, Vector2f windowSize) {
//		gearshift = (Node)assets.loadModel("Models/gear_panel.j3o");
//		gearshift.setMaterial(assets.loadMaterial("Materials/gear_panel_material.j3m"));
//		gearshift.setLocalTranslation(windowSize.x, 0f, 0f);
//		float scale = 1f;
//		gearshift.setLocalScale(scale, scale, -scale);
//		gui.attachChild(gearshift);	
		Label lapLabel = new Label("Lap 1/3");
		lapLabel.setName("lap-counter");
		lapLabel.setFontSize(30f);
		lapLabel.setLocalTranslation(5f, windowSize.y-5f, 0f);
		gui.attachChild(lapLabel);
	}
    public void configureInputs(InputMapper im) {
        player.getInputScheme().addListeners(im, this, this);
		player.getInputScheme().activateGroup(im, true);
	}
	public Light[] createHeadlights() {
		if (car == null) return null;
		SpotLight light = new SpotLight();
		SpotLightNode n = new SpotLightNode(light);
		n.setLocalTranslation(0f, 2f, -3f);
		light.setColor(ColorRGBA.White);
		light.setSpotRange(100f);
		light.setSpotOuterAngle(FastMath.PI/2.1f);
		light.setSpotInnerAngle(FastMath.PI/2.2f);
		((Node)car.getSpatial()).attachChild(n);
//		PointLight light = new PointLight();
//		PointLightNode n = new PointLightNode(light);
//		n.setLocalTranslation(0f, 2f, 0f);
//		light.setRadius(1000f);
//		light.setColor(ColorRGBA.White);
//		((Node)car.getSpatial()).attachChild(n);
		return new Light[] {light};
	}
	public void setViewWindow(ViewWindow window) {
		window.applyToCamera(gameCam);
		window.applyToCamera(guiCam);
		this.window = window;
	}
	public void setCar(VehicleControl car) {
		this.car = car;
	}
	public void setCamera(Camera cam) {
		this.gameCam = cam;
	}
	public void setBaseAccelForce(float accel) {
		baseAccelForce = accel;
	}
	
	public void update(float tpf) {		
		if (car == null || gameCam == null) return;
		gameCam.setLocation(car.getPhysicsLocation().add(car.getPhysicsRotation().mult(camLocation)));
        //camControl.setOffset(car.getPhysicsRotation().mult(new Vector3f(0f, 2f, -.5f)));
		Quaternion tilted = new Quaternion().lookAt(car.getPhysicsRotation().mult(new Vector3f(0f, 0f, -1f)), FastMath.interpolateLinear(.5f, Vector3f.UNIT_Y, car.getPhysicsRotation().mult(Vector3f.UNIT_Y)));
		gameCam.setRotation(tilted);
        //gameCam.setLocation(car.getPhysicsLocation().add(3f, 3f, 3f));
        //gameCam.lookAt(car.getPhysicsLocation(), Vector3f.UNIT_Y);
	}
	public void updateNodeStates(float tpf) {
		gui.updateLogicalState(tpf);
		gui.updateGeometricState();
	}	
	public boolean detectLapTriggers(List<LapTrigger> triggers, int laps) {
		if (finished) return false;
		Spatial spatial = triggers.get(nextTrigger).getSpatial();
		Ray ray = new Ray(car.getPhysicsLocation(), VIEW_PLANE.getClosestPoint(car.getPhysicsRotation().getRotationColumn(2)).negateLocal());
		CollisionResults res = new CollisionResults();
		spatial.collideWith(ray, res);
		if (res.size() > 0 && res.getClosestCollision().getDistance() < 2f) {
			if (nextTrigger == 0) {
				lap++;
				((Label)gui.getChild("lap-counter")).setText("Lap "+Math.min(Math.max(lap, 1), laps)+"/"+laps);
				if (lap > laps) {
					car.accelerate(0f);
					car.steer(0f);
					lap = laps;
					finished = true;
					notifyListeners(l -> l.onDriverFinish(this));
				}
                else {
                    notifyListeners(l -> l.onLapFinish(this));                    
                }
			}
			if (++nextTrigger >= triggers.size()) {
				nextTrigger = 0;
			}
			return true;
		}
		return false;
	}
	public void warp(Vector3f translation, Quaternion rotation) {
		car.setPhysicsLocation(translation);
		car.setPhysicsRotation(rotation);
		car.setLinearVelocity(Vector3f.ZERO);
		car.setAngularVelocity(Vector3f.ZERO);
        //camControl.resetCameraLocation();
	}    
	private void applyAcceleration() {
		//car.accelerate(-(baseAccelForce+gearFactor*viewNum)*accelDirection);
		car.accelerate(-baseAccelForce*accelDirection);
	}
	
	public void cleanupViewPorts(RenderManager rm) {
		rm.removeMainView(id+"-view");
		rm.removePostView(id+"-gui-view");
	}
	public void cleanupInputs(InputMapper im) {
		player.getInputScheme().removeListeners(im, this, this);
        player.getInputScheme().activateGroup(im, false);
	}
	
	public Player getControllingPlayer() {
		return player;
	}
	public VehicleControl getVehicle() {
		return car;
	}
	public Camera getCamera() {
		return gameCam;
	}
	public Node getGui() {
		return gui;
	}
	public ViewWindow getViewWindow() {
		return window;
	}
	public int getNextTriggerIndex() {
		return nextTrigger;
	}
	public int getLapNumber() {
		return lap;
	}
	public boolean isFinished() {
		return finished;
	}
    public String getGameViewPortId() {
        return id+"-view";
    }
    public String getGuiViewPortId() {
        return id+"-gui-view";
    }
	@Override
	public Collection<DriverListener> getListeners() {
		return listeners;
	}
	
	@Override
	public void valueChanged(FunctionId func, InputState value, double tpf) {
		if (finished) return;
		if (func == player.getInputScheme().getDrive()) {
			accelDirection = value.asNumber();
			applyAcceleration();
		}
		else if (func == player.getInputScheme().getSteer()) {
			if (value != InputState.Off) {
				car.steer(-steerAngle*value.asNumber());
			}
			else {
				car.steer(0f);
			}
		}
		else if (func == player.getInputScheme().getFlip() && value != InputState.Off) {
			car.setAngularVelocity(car.getPhysicsRotation().getRotationColumn(2).negateLocal().multLocal(5f));
		}
	}
    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        if (finished) return;
        if (func == player.getInputScheme().getSteer()) {
            car.steer(-steerAngle*(float)value);
        }
    }
	
    /**
     * Fetches a child named.
     * @param spatial
     * @param name
     * @return 
     */
	private static Geometry getChildGeometry(Spatial spatial, String name) {
		for (Spatial s : new SceneGraphIterator(spatial)) {
			if (s.getName().equals(name)) {
				if (s instanceof Geometry) {
                    return (Geometry)s;
                }
                else {
                    throw new IllegalStateException("Spatial \""+name+"\" is not a Geometry!");
                }
			}
		}
		return null;
	}
	
	
}
