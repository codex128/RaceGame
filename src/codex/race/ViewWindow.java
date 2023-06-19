/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import com.jme3.math.Vector4f;
import com.jme3.renderer.Camera;

/**
 *
 * @author gary
 */
public class ViewWindow {
    
    private Vector4f window = new Vector4f();
    
    public ViewWindow(int slot, int format) {
        window = readFormat(slot, format);
    }
    public ViewWindow(Vector4f window) {
        this.window.set(window);
    }
    public ViewWindow(float xmin, float xmax, float ymin, float ymax) {
        window.set(xmin, xmax, ymin, ymax);
    }
    
    private Vector4f readFormat(int i, int n) {
		switch (n) {
			case 1: return new Vector4f(0f, 1f, 0f, 1f);
			case 2: switch (i) {
				case 0:  return new Vector4f(0f, .5f, .25f, .75f);
				default: return new Vector4f(.5f, 1f, .25f, .75f);
			}
			default: switch (i) {
				case 0:  return new Vector4f(0f, .5f, .5f, 1f);
				case 1:  return new Vector4f(.5f, 1f, .5f, 1f);
				case 2:  return new Vector4f(0f, .5f, 0f, .5f);
				default: return new Vector4f(.5f, 1f, 0f, .5f);
			}
		}
	}   
    
    public Vector4f getWindow() {
        return window;
    }
    public void applyToCamera(Camera camera) {
        camera.setViewPort(window.x, window.y, window.z, window.w);
    }
    
}
