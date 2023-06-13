/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.light.SpotLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author gary
 */
public class SpotLightNode extends Node {
	
	private static final Vector3f NEG_Z = new Vector3f(0f, 0f, -1f);
	
	SpotLight light;
	
	public SpotLightNode(SpotLight light) {
		this.light = light;
	}
	
	public SpotLight getLight() {
		return light;
	}
	@Override
	public void updateLogicalState(float tpf) {
		super.updateLogicalState(tpf);
		light.setPosition(getWorldTranslation());
		light.setDirection(getWorldRotation().mult(NEG_Z));
	}
	
}
