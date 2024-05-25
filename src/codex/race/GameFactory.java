/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import codex.jmeutil.scene.SceneGraphIterator;
import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author gary
 */
public class GameFactory extends GameAppState {

    public static final String[] COLORS = "red/blue/green/yellow".split("/");
    
    @Override
    protected void init(Application app) {}
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
    /**
     * Creates a car model based on input data.
     * @param carData
     * @param color
     * @return 
     */
    public Spatial createCarModel(J3map carData, String color) {
        Spatial model = assetManager.loadModel(carData.getString("model"));
        if (carData.propertyExists("material")) {
            model.setMaterial(createCarMaterial(carData, color));
        }
        return model;
    }
    
    /**
     * Creates a car material based on input data.
     * @param carData
     * @param color
     * @return 
     */
    public Material createCarMaterial(J3map carData, String color) {
        if (!carData.propertyExists("material")) {
            return null;
        }
        Material mat = assetManager.loadMaterial(carData.getString("material"));
        TextureKey key = new TextureKey(carData.getString("texture").replace((CharSequence)"$", color), false);
        key.setFlipY(false);
        Texture tex = assetManager.loadTexture(key);
        mat.setTexture("DiffuseMap", tex);
        return mat;
    }
    
    /**
     * Creates a physical vehicle based on input data.Code copied from TestFancyCar.java.
     * @param model
     * @param carData
     * @return physical vehicle
     * @author JmeTests3, gary
     */
    public VehicleControl createVehicle(Spatial model, J3map carData) {
        J3map suspension = carData.getJ3map("suspension");
		float stiffness = suspension.getFloat("stiffness", 120f);
        float compValue = suspension.getFloat("compression", .2f);
        float dampValue = suspension.getFloat("damping", .3f);
        final float mass = carData.getFloat("mass", 200f);
        
        //Load model and get chassis Geometry
        //Spatial model = factory.createCarModel(carData, player.getCarColor());
        Geometry chasis = getChildGeometry(model, carData.getString("chassis"));

        //Create a hull collision shape for the chassis
        CollisionShape carHull = CollisionShapeFactory.createDynamicMeshShape(chasis);

        //Create a vehicle control
		VehicleControl car = new VehicleControl(carHull, mass);
        model.addControl(car);

        //Setting default values for wheels
        car.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        car.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        car.setSuspensionStiffness(stiffness);
        car.setMaxSuspensionForce(1);

        //Create four wheels and add them at their locations
        //note that our fancy car actually goes backwards..
        Vector3f wheelDirection = new Vector3f(0, -1, 0);
        Vector3f wheelAxle = new Vector3f(-1, 0, 0);
		J3map wheels = carData.getJ3map("wheels");
		
        car.setFrictionSlip(.9f);
        car.setRollingFriction(10000f);
        
        Geometry wheel_fr = getChildGeometry(model, wheels.getString("fr"));
        wheel_fr.center();
        BoundingBox box = (BoundingBox)wheel_fr.getModelBound();
        float wheelRadius = box.getYExtent();
        float back_wheel_h = (wheelRadius * 1.7f) - 1f;
        float front_wheel_h = (wheelRadius * 1.7f) - 1f;
        car.addWheel(wheel_fr.getParent(), box.getCenter().add(0, -front_wheel_h, 0),
                wheelDirection, wheelAxle, 0.2f, wheelRadius, true);

        Geometry wheel_fl = getChildGeometry(model, wheels.getString("fl"));
        wheel_fl.center();
        box = (BoundingBox)wheel_fl.getModelBound();
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
        
        ///for (int i = 0; i < car.getNumWheels(); i++) {
        //    car.setRollInfluence(i, 0f);
        //}
        
        J3map axles = carData.getJ3map("axles");
        if (axles != null) {
            Geometry axle_fr = getChildGeometry(model, axles.getString("fr"));
            axle_fr.addControl(new AxleControl(wheel_fr));
            Geometry axle_fl = getChildGeometry(model, axles.getString("fl"));
            axle_fl.addControl(new AxleControl(wheel_fl));
            Geometry axle_br = getChildGeometry(model, axles.getString("br"));
            axle_br.addControl(new AxleControl(wheel_br));
            Geometry axle_bl = getChildGeometry(model, axles.getString("bl"));
            axle_bl.addControl(new AxleControl(wheel_bl));
        }
		
		return car;
    }
    
    
    /**
     * Fetches a geometry from the given spatial.
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
