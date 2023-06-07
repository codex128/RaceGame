/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

/**
 *
 * @author gary
 */
public class Functions {
	
	public static final String
			DRIVER_GROUP = "driver-group";
	
	public static final FunctionId
			F_GAS = new FunctionId(DRIVER_GROUP, "gas"),
			F_STEER = new FunctionId(DRIVER_GROUP, "steer");
	
	public static void initialize(InputMapper im) {
		im.map(F_GAS, InputState.Positive, KeyInput.KEY_W);
		im.map(F_GAS, InputState.Negative, KeyInput.KEY_S);
		im.map(F_STEER, InputState.Positive, KeyInput.KEY_D);
		im.map(F_STEER, InputState.Negative, KeyInput.KEY_A);
	}
	
}
