/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import codex.jmeutil.listen.Listenable;
import codex.jmeutil.scene.SceneGraphIterator;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class Driver implements StateFunctionListener, Listenable<DriverListener> {
	
	private static final Plane VIEW_PLANE = new Plane(Vector3f.UNIT_Y, Vector3f.ZERO);
	
	String id;
	VehicleControl car;
	Camera cam;
	Node gui;
	ViewPort guiView;
	Vector4f viewSize;
	FunctionId gas, flip, steer;
	float accelForce = 10000f;
	float steerAngle = .5f;
	int nextTrigger = 0;
	int lap = 0;
	boolean finished = false;
	float camHeight = 10f;
	LinkedList<DriverListener> listeners = new LinkedList<>();
	
	public Driver(String id) {
		this.id = id;
	}
	
	public VehicleControl createVehicle(AssetManager assets, J3map source) {
		return createVehicle(assets.loadModel(source.getString("model")), source);
	}
	public VehicleControl createVehicle(Spatial model, J3map source) {
		J3map suspension = source.getJ3map("suspension");
		float stiffness = suspension.getFloat("stiffness", 120f);
        float compValue = suspension.getFloat("compression", .2f);
        float dampValue = suspension.getFloat("damping", .3f);
        final float mass = source.getFloat("mass", 200f);

        //Load model and get chassis Geometry
        Geometry chasis = getChildGeometry(model, "Car");
        BoundingBox box = (BoundingBox)chasis.getModelBound();

        //Create a hull collision shape for the chassis
        CollisionShape carHull = CollisionShapeFactory.createDynamicMeshShape(chasis);

        //Create a vehicle control
		car = new VehicleControl(carHull, mass);
        model.addControl(car);

        //Setting default values for wheels
        car.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        car.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        car.setSuspensionStiffness(stiffness);
        car.setMaxSuspensionForce(10000);

        //Create four wheels and add them at their locations
        //note that our fancy car actually goes backwards..
        Vector3f wheelDirection = new Vector3f(0, -1, 0);
        Vector3f wheelAxle = new Vector3f(-1, 0, 0);
		J3map wheels = source.getJ3map("wheels");
		
        Geometry wheel_fr = getChildGeometry(model, wheels.getString("fr"));
        wheel_fr.center();
        box = (BoundingBox) wheel_fr.getModelBound();
        float wheelRadius = box.getYExtent();
        float back_wheel_h = (wheelRadius * 1.7f) - 1f;
        float front_wheel_h = (wheelRadius * 1.9f) - 1f;
        car.addWheel(wheel_fr.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        Geometry wheel_fl = getChildGeometry(model, wheels.getString("fl"));
        wheel_fl.center();
        box = (BoundingBox) wheel_fl.getModelBound();
        car.addWheel(wheel_fl.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        Geometry wheel_br = getChildGeometry(model, wheels.getString("br"));
        wheel_br.center();
        box = (BoundingBox) wheel_br.getModelBound();
        car.addWheel(wheel_br.getParent(), box.getCenter().add(0, -back_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, false);

        Geometry wheel_bl = getChildGeometry(model, wheels.getString("bl"));
        wheel_bl.center();
        box = (BoundingBox) wheel_bl.getModelBound();
        car.addWheel(wheel_bl.getParent(), box.getCenter().add(0, -back_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, false);
		
		//vehicle.getWheel(0).setFrictionSlip(4);
		//vehicle.getWheel(1).setFrictionSlip(4);
        //car.getWheel(2).setFrictionSlip(10);
        //car.getWheel(3).setFrictionSlip(10);
		
		return car;
	}
	public ViewPort createPersonalViewPort(RenderManager rm, Camera base) {
		cam = base.clone();
		ViewPort vp = rm.createMainView(id+"-view", cam);
		vp.setClearFlags(true, true, true);
		return vp;
	}
	public void setViewSize(Vector4f size) {
		viewSize = size;
		cam.setViewPort(size.x, size.y, size.z, size.w);
	}
	public void setCar(VehicleControl car) {
		this.car = car;
	}
	public void setCamera(Camera cam) {
		this.cam = cam;
	}
	public void setAccelForce(float accel) {
		accelForce = accel;
	}
	
	public void update(float tpf) {
		if (car == null || cam == null) return;
		Vector3f planar = VIEW_PLANE.getClosestPoint(car.getPhysicsRotation().getRotationColumn(2));
		Vector3f pos = car.getPhysicsLocation().add(planar.multLocal(30f)).addLocal(0f, camHeight, 0f);
		cam.setLocation(cam.getLocation().add(pos.subtractLocal(cam.getLocation()).divideLocal(10f)));
		cam.lookAt(car.getPhysicsLocation(), Vector3f.UNIT_Y);
		gui.updateLogicalState(tpf);
	}
	public void render(RenderManager rm) {
		gui.updateGeometricState();
	}
	public boolean detectLapTriggers(ArrayList<LapTrigger> triggers, int laps) {
		if (finished) return false;
		Spatial spatial = triggers.get(nextTrigger).getSpatial();
		Ray ray = new Ray(car.getPhysicsLocation(), VIEW_PLANE.getClosestPoint(car.getPhysicsRotation().getRotationColumn(2)).negateLocal());
		CollisionResults res = new CollisionResults();
		spatial.collideWith(ray, res);
		if (res.size() > 0 && res.getClosestCollision().getDistance() < 1f) {
			if (nextTrigger == 0 && ++lap > laps) {
				car.accelerate(0f);
				car.steer(0f);
				lap = laps;
				finished = true;
				notifyListeners(l -> l.onDriverFinish(this));
			}
			if (++nextTrigger >= triggers.size()) {
				nextTrigger = 0;
			}
			return true;
		}
		return false;
	}
	public void minimizeCarOcclusion(Spatial track) {
		Vector3f focus = car.getPhysicsLocation().add(0f, .5f, 0f);
		Ray r = new Ray(cam.getLocation(), focus.subtract(cam.getLocation()).normalizeLocal());
		CollisionResults res = new CollisionResults();
		track.collideWith(r, res);
		if (res.size() > 0) {
			CollisionResult closest = res.getClosestCollision();
			if (closest.getDistance() < cam.getLocation().distance(focus)) {
				camHeight = Math.min(camHeight+1f, 30f);
			}
			else {
				camHeight = Math.max(camHeight-1f, 10f);
			}
		}
	}
	
	public VehicleControl getVehicle() {
		return car;
	}
	public Camera getCamera() {
		return cam;
	}
	public Node getGui() {
		return gui;
	}
	public Vector4f getViewSize() {
		return viewSize;
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
	@Override
	public Collection<DriverListener> getListeners() {
		return listeners;
	}
	
	public void initializeInputs(InputMapper im, int drive, int reverse, int flip, int right, int left) {
		gas = new FunctionId(id+"-input", "gas");
		this.flip = new FunctionId(id+"-input", "flip");
		steer = new FunctionId(id+"-input", "steer");
		im.map(gas, InputState.Positive, drive);
		im.map(gas, InputState.Negative, reverse);
		im.map(this.flip, flip);
		im.map(steer, InputState.Positive, right);
		im.map(steer, InputState.Negative, left);
		listenToInputs(im);
	}
	public void listenToInputs(InputMapper im) {		
		im.addStateListener(this, gas, this.flip, steer);
		im.activateGroup(id+"-input");
	}
	public void cleanupInputs(InputMapper im) {
		if (gas == null || steer == null) return;
		im.removeStateListener(this, gas, flip, steer);
		im.deactivateGroup(id+"-input");
	}
	@Override
	public void valueChanged(FunctionId func, InputState value, double tpf) {
		if (finished) return;
		if (func == gas) {
			if (value != InputState.Off) {
				car.accelerate(-accelForce*value.asNumber());
			}
			else {
				car.accelerate(0f);
			}
		}
		else if (func == steer) {
			if (value != InputState.Off) {
				car.steer(-steerAngle*value.asNumber());
			}
			else {
				car.steer(0f);
			}
		}
		else if (func == flip && value != InputState.Off) {
			car.setAngularVelocity(car.getPhysicsRotation().getRotationColumn(2).negateLocal().multLocal(5f));
		}
	}
	
	/**
	 * Is a duplicate of Main.getChildGeometry(...).
	 * @param spatial
	 * @param name
	 * @return 
	 */
	private static Geometry getChildGeometry(Spatial spatial, String name) {
		for (Spatial s : new SceneGraphIterator(spatial)) {
			if (s instanceof Geometry && s.getName().startsWith(name)) {
				return (Geometry)s;
			}
		}
		return null;
	}	
	
}
