/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.light.PointLight;
import com.jme3.scene.Node;

/**
 *
 * @author gary
 */
public class PointLightNode extends Node {
	
	PointLight light;
	
	public PointLightNode(PointLight light) {
		this.light = light;
	}
	
	public PointLight getLight() {
		return light;
	}
	@Override
	public void updateLogicalState(float tpf) {
		super.updateLogicalState(tpf);
		light.setPosition(getWorldTranslation());
	}
	
}
