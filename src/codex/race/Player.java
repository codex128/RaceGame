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
	
    final int id;
    int viewport;
	J3map carData;
    DriverInputScheme inputScheme;
    String carColor = "red";
    
    public Player(int id) {
        this.id = id;
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
	
    public int getId() {
        return id;
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
