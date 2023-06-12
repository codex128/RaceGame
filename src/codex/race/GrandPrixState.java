/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import com.jme3.app.Application;

/**
 *
 * @author gary
 */
public class GrandPrixState extends GameAppState implements RaceListener {
	
	J3map data;
	int index;
	Player[] players;
	
	public GrandPrixState(J3map data) {
		this.data = data;
	}
	
	@Override
	protected void init(Application app) {
		
		J3map commonCar = (J3map)assetManager.loadAsset("Properties/MyCar.j3map");
		players = new Player[]{new Player(0), new Player(1), /*new Player(2), /*new Player(3)*/};
		for (Player p : players) {
			p.setCarData(commonCar);
		}
		
		//grandprix = (J3map)assetManager.loadAsset("Properties/GrandPrix1.j3map");
		index = data.getInteger("startIndex", 0);
		getStateManager().attach(createRace());
		
	}
	@Override
	protected void cleanup(Application app) {}
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}	
	@Override
	public void onRaceComplete(RaceState race) {
		if (index == data.getInteger("length")) return;
		getStateManager().detach(race);
		getApplication().enqueue(() -> {
			getStateManager().attach(createRace());
		});
	}
	
	private RaceState createRace() {
		String r = data.getString("race"+(index++));
		if (r == null) return null;
		RaceState race = new RaceState((J3map)assetManager.loadAsset(r), players);
		race.addListener(this);
		return race;
	}
	
}
