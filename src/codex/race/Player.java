/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;

/**
 *
 * @author gary
 */
public class Player {
	
	int pnum;
	J3map carData;
    DriverInputScheme inputScheme;
	
	public Player(int pnum) {
		this.pnum = pnum;
	}
	
	public void setCarData(J3map carData) {
		this.carData = carData;
	}
    public void setInputScheme(DriverInputScheme scheme) {
        inputScheme = scheme;
    }
	
	public int getPlayerNumber() {
		return pnum;
	}
	public J3map getCarData() {
		return carData;
	}
	
}
