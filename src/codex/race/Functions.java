/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.InputMapper;

/**
 *
 * @author gary
 */
public class Functions {
	
	public static final DriverInputScheme
			ARROW_KEYS = new DriverInputScheme("Arrow Keys"),
			WASD = new DriverInputScheme("WASD"),
			IJKL = new DriverInputScheme("IJKL"),
            JOYSTICK = new DriverInputScheme("Joystick");
    public static final DriverInputScheme[]
            SCHEMES = {ARROW_KEYS, WASD, IJKL, JOYSTICK};
	
	public static void initialize(InputMapper im) {
		ARROW_KEYS.initialize(im, KeyInput.KEY_UP, KeyInput.KEY_DOWN,
				KeyInput.KEY_RIGHT, KeyInput.KEY_LEFT, KeyInput.KEY_RCONTROL);
        
		WASD.initialize(im, KeyInput.KEY_W, KeyInput.KEY_S,
				KeyInput.KEY_D, KeyInput.KEY_A, KeyInput.KEY_F);
        
		IJKL.initialize(im, KeyInput.KEY_I, KeyInput.KEY_K,
				KeyInput.KEY_L, KeyInput.KEY_J, KeyInput.KEY_SEMICOLON);
        
        JOYSTICK.initialize(im, Button.JOYSTICK_BUTTON1, Button.JOYSTICK_BUTTON2,
                Axis.JOYSTICK_LEFT_X, Button.JOYSTICK_BUTTON11);
	}
	
}
