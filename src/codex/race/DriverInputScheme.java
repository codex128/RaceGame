/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 *
 * @author gary
 */
public class DriverInputScheme {
	
    public static final int
            DRIVE = 0, STEER = 1, FLIP = 2;
    
	private final String id;
	private final FunctionId[] functions = new FunctionId[3];
	
	public DriverInputScheme(String id) {
		this.id = id;
		functions[0] = new FunctionId(id, "drive");
		functions[1] = new FunctionId(id, "steer");
		functions[2] = new FunctionId(id, "flip");
	}
	
	public void initialize(InputMapper im,
			int forwardKey, int reverseKey, int rightKey, int leftKey, int flipKey) {
		im.map(functions[DRIVE], InputState.Positive, forwardKey);
		im.map(functions[DRIVE], InputState.Negative, reverseKey);
		im.map(functions[STEER], InputState.Positive, rightKey);
		im.map(functions[STEER], InputState.Negative, leftKey);
		im.map(functions[FLIP], flipKey);
	}
    
	public void activateGroup(InputMapper im, boolean activate) {
		if (activate) im.activateGroup(id);
		else im.deactivateGroup(id);
	}
    public void addListeners(InputMapper im, AnalogFunctionListener analog, StateFunctionListener state) {
        if (analog != null) im.addAnalogListener(analog, functions);
        if (state != null) im.addStateListener(state, functions);
    }
    public void removeListeners(InputMapper im, AnalogFunctionListener analog, StateFunctionListener state) {
        if (analog != null) im.removeAnalogListener(analog, functions);
        if (state != null) im.removeStateListener(state, functions);
    }
	
	public String getId() {
		return id;
	}
	public FunctionId getDrive() {
		return functions[DRIVE];
	}
	public FunctionId getSteer() {
		return functions[STEER];
	}
	public FunctionId getFlip() {
		return functions[FLIP];
	}
	public FunctionId[] getFunctions() {
		return functions;
	}
	
    public boolean contains(FunctionId func) {
        for (FunctionId f : functions) {
            if (f == func) return true;
        }
        return false;
    }
    
}
