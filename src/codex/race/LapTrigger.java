/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.scene.Spatial;

/**
 *
 * @author gary
 */
public class LapTrigger {
	
	private final Spatial spatial;
	private final int index;
	
	public LapTrigger(Spatial spatial, int index) {
		this.spatial = spatial;
		this.index = index;
	}
	
	public Spatial getSpatial() {
		return spatial;
	}
	public int getIndex() {
		return index;
	}
	
}
