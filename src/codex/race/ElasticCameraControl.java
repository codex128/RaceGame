/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author gary
 */
public class ElasticCameraControl extends AbstractControl {
    
    Camera camera;
    Vector3f offset = new Vector3f();
    Vector3f velocity = new Vector3f();
    Vector3f trueLocation = new Vector3f();
    float radius = .5f;
    float elasticity = .1f;
    float damping = .8f;
    float speed = 1f;
    
    public ElasticCameraControl(Camera camera) {
        this.camera = camera;
    }    
    
    @Override
    protected void controlUpdate(float tpf) {
        Vector3f location = spatial.getWorldTranslation().add(offset);
        velocity.addLocal(location.subtract(trueLocation).multLocal(elasticity));
        velocity.multLocal(damping);
        trueLocation.set(trueLocation.add(velocity.mult(speed)));
        if (trueLocation.distanceSquared(location) > radius*radius) {
            trueLocation.set(location.add(camera.getLocation().subtract(location).normalizeLocal().multLocal(radius)));
            //velocity.multLocal(damping);
        }
        camera.setLocation(trueLocation);
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    @Override
    public void setSpatial(Spatial spat) {
        super.setSpatial(spat);
        if (spatial != null) {
            resetCameraLocation();
        }
    }
    
    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public void setElasticity(float elasticity) {
        this.elasticity = elasticity;
    }
    public void resetCameraLocation() {
        camera.setLocation(spatial.getWorldTranslation().add(offset));
        trueLocation.set(camera.getLocation());
        velocity.set(0f, 0f, 0f);
    }

    public Vector3f getOffset() {
        return offset;
    }
    public float getRadius() {
        return radius;
    }
    public float getElasticity() {
        return elasticity;
    }
    public Vector3f getVelocity() {
        return velocity;
    }
    
}
