/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.Button;
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
	private final DriverFunctionId[] functions = new DriverFunctionId[3];
	
	public DriverInputScheme(String id) {
		this.id = id;
		functions[0] = new DriverFunctionId(id, "drive");
		functions[1] = new DriverFunctionId(id, "steer");
		functions[2] = new DriverFunctionId(id, "flip");
	}
	
	public void initialize(InputMapper im, int forwardKey, int reverseKey, int rightKey, int leftKey, int flipKey) {
		im.map(functions[DRIVE], InputState.Positive, forwardKey);
		im.map(functions[DRIVE], InputState.Negative, reverseKey);
		im.map(functions[STEER], InputState.Positive, rightKey);
		im.map(functions[STEER], InputState.Negative, leftKey);
		im.map(functions[FLIP], flipKey);
	}
    public void initialize(InputMapper im, Button forwardButton, Button reverseButton, Axis steerAxis, Button flipButton) {
        im.map(functions[DRIVE], InputState.Positive, forwardButton);
		im.map(functions[DRIVE], InputState.Negative, reverseButton);
		im.map(functions[STEER], steerAxis);
		im.map(functions[FLIP], flipButton);
        functions[STEER].setAsAnalog(true);
    }
    
	public void activateGroup(InputMapper im, boolean activate) {
		if (activate) im.activateGroup(id);
		else im.deactivateGroup(id);
	}
    public void addListeners(InputMapper im, AnalogFunctionListener analog, StateFunctionListener state) {
        for (DriverFunctionId func : functions) {
            if (func.isAnalog() && analog != null) {
                im.addAnalogListener(analog, func);
            }
            else if (!func.isAnalog() && state != null) {
                im.addStateListener(state, func);
            }
        }
    }
    public void removeListeners(InputMapper im, AnalogFunctionListener analog, StateFunctionListener state) {
        if (analog != null) im.removeAnalogListener(analog, functions);
        if (state != null) im.removeStateListener(state, functions);
    }
	
	public String getId() {
		return id;
	}
	public DriverFunctionId getDrive() {
		return functions[DRIVE];
	}
	public DriverFunctionId getSteer() {
		return functions[STEER];
	}
	public DriverFunctionId getFlip() {
		return functions[FLIP];
	}
	public DriverFunctionId[] getFunctions() {
		return functions;
	}
	
    public boolean contains(FunctionId func) {
        for (FunctionId f : functions) {
            if (f == func) return true;
        }
        return false;
    }
    
    public static final class DriverFunctionId extends FunctionId {
        
        private boolean analog = false;
        
        private DriverFunctionId(String group, String id) {
            super(group, id);
        }
        
        public boolean isAnalog() {
            return analog;
        }
        private void setAsAnalog(boolean analog) {
            this.analog = analog;
        }
        
    }
    
}
