/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author gary
 */
public class AxleControl extends AbstractControl {

    private final Spatial wheel;
    private Quaternion normal;
    
    public AxleControl(Spatial wheel) {
        this.wheel = wheel;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        Quaternion rotation = calculateRotation().subtractLocal(normal);
        spatial.setLocalRotation(rotation);
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    @Override
    public void setSpatial(Spatial spat) {
        super.setSpatial(spat);
        if (spatial != null) {
            normal = calculateRotation();
        }
    }
    
    private Quaternion calculateRotation() {
        return new Quaternion().lookAt(wheel.getLocalTranslation().subtract(spatial.getLocalTranslation()), Vector3f.UNIT_Y);
    }
    public Quaternion getNormalRotation() {
        return normal;
    }
    public Spatial getWheel() {
        return wheel;
    }
    
}
