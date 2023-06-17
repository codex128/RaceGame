/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

/**
 *
 * @author gary
 */
public class DriverInputScheme {
	
	private final String id;
	private final FunctionId drive, steer, flip;
	
	public DriverInputScheme(String id) {
		this.id = id;
		drive = new FunctionId(id, "drive");
		steer = new FunctionId(id, "steer");
		flip = new FunctionId(id, "flip");
	}
	
	public void initialize(InputMapper im,
			int forwardKey, int reverseKey, int rightKey, int leftKey, int flipKey) {
		im.map(drive, InputState.Positive, forwardKey);
		im.map(drive, InputState.Negative, reverseKey);
		im.map(steer, InputState.Positive, rightKey);
		im.map(steer, InputState.Negative, leftKey);
		im.map(flip, flipKey);
	}
	public void activateGroup(InputMapper im, boolean activate) {
		if (activate) im.activateGroup(id);
		else im.deactivateGroup(id);
	}
	
	public String getId() {
		return id;
	}
	public FunctionId getDrive() {
		return drive;
	}
	public FunctionId getSteer() {
		return steer;
	}
	public FunctionId getFlip() {
		return flip;
	}
	public FunctionId[] getFunctions() {
		return new FunctionId[] {drive, steer, flip};
	}
	
}
