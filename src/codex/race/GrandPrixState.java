/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import com.jme3.app.Application;
import java.util.List;
import codex.race.config.ConfigClient;
import codex.race.config.ConfigManager;
import codex.race.config.ConfigState;
import codex.race.config.PlayerConfigState;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class GrandPrixState extends GameAppState implements RaceListener, ConfigClient {
	
    private static final int NUM_PLAYERS = 2;
    
    ConfigManager manager;
	J3map data;
	int index;
	List<Player> players;
	int forceLaps;
	
	public GrandPrixState() {}
    public GrandPrixState(J3map data) {
        this.data = data;
        getInfo();
    }
	
	@Override
	protected void init(Application app) {
        
        manager = new ConfigManager(this, new PlayerConfigState());
        getStateManager().attach(manager);
        
        //manualConfig();
        
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
    @Override
    public void recieveConfiguredPlayers(List<Player> players) {
        this.players = players;
    }
    @Override
    public void recieveSelectedRaceData(J3map raceData) {		
        data = raceData;
		getInfo();
    }
    @Override
    public void configFinished(ConfigState config) {
        manager.advance();
    }    
    @Override
    public void allConfigsFinished() {
        getStateManager().detach(manager);
        getStateManager().attach(createRace());
    }
	
    private void manualConfig() {
        players = new LinkedList<>();
        J3map commonCar = (J3map)assetManager.loadAsset("Properties/cars/MyCar.j3map");
        for (int i = 0; i < NUM_PLAYERS; i++) {
            Player p = new Player(i);
            p.setViewPortNumber(i);
            p.setCarData(commonCar);
            p.setInputScheme(Functions.SCHEMES[i]);
            p.setCarColor(GameFactory.COLORS[i]);
            players.add(p);
        }       
        getStateManager().attach(createRace());
    }
    private void getInfo() {        
		index = data.getInteger("startIndex", 0);
		forceLaps = data.getInteger("forceLaps", -1); 
    }
    
	private RaceState createRace() {
		String r = data.getString("race"+(index++));
		if (r == null) return null;
		RaceState race = new RaceState(constructRaceModel(r));
		race.addListener(this);
		return race;
	}
    private RaceModel constructRaceModel(String source) {
        RaceModel m = new RaceModel();
        m.setTrackData((J3map)assetManager.loadAsset(source));
        System.out.println("there are "+players.size()+" players");
        m.setPlayers(players);
        m.setForcedLaps(forceLaps);
        return m;
    }
	
}
