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
	
    private int pnum, viewport;
	private J3map carData;
    private DriverInputScheme inputScheme;
    private String carColor = GameFactory.COLORS[0];
    
    public Player(int pnum) {
        this.pnum = pnum;
    }
	
    public void setPlayerNumber(int pnum) {
        this.pnum = pnum;
    }
	public void setViewPortNumber(int vpn) {
        viewport = vpn;
    }
	public void setCarData(J3map carData) {
		this.carData = carData;
	}
    public void setInputScheme(DriverInputScheme scheme) {
        inputScheme = scheme;
    }
    public void setCarColor(String color) {
        carColor = color;
    }
	
    public int getPlayerNumber() {
        return pnum;
    }
	public int getViewPortNumber() {
		return viewport;
	}
	public J3map getCarData() {
		return carData;
	}
    public DriverInputScheme getInputScheme() {
        return inputScheme;
    }
    public String getCarColor() {
        return carColor;
    }
	
}
