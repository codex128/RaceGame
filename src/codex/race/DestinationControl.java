/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author gary
 */
public class DestinationControl extends AbstractControl {

	private Vector3f dest;
	private float scalar = 10f;
	
	public DestinationControl(Vector3f destination) {
		this.dest = destination;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		if (dest == null) return;
		spatial.setLocalTranslation(dest.subtract(spatial.getLocalTranslation()).divide(scalar));
	}
	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {}
	
	public void setDestination(Vector3f dest) {
		this.dest = dest;
	}
	
}
