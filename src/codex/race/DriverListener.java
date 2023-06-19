/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

/**
 *
 * @author gary
 */
public interface DriverListener {
	
	public void onDriverFinish(Driver driver);
    public void onLapFinish(Driver driver);
	
}
