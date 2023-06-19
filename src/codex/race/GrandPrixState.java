/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.j3map.J3map;
import codex.race.config.PlayerConfigState;
import com.jme3.app.Application;
import java.util.List;
import codex.race.config.ConfigClient;
import codex.race.config.ConfigClientState;
import codex.race.config.ConfigState;
import codex.race.config.RaceSelectionState;

/**
 *
 * @author gary
 */
public class GrandPrixState extends ConfigClientState implements RaceListener, ConfigClient {
	
	J3map data;
	int index;
	List<Player> players;
	int forceLaps;
	
	public GrandPrixState() {
		super(new PlayerConfigState(4), new RaceSelectionState(false));
	}
	
	@Override
	protected void init(Application app) {
		
        super.init(app);
		
		index = data.getInteger("startIndex", 0);
		forceLaps = data.getInteger("forceLaps", -1);
		
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
    }
    @Override
    public void allConfigsComplete() {
        getStateManager().attach(createRace());
    }
	
	private RaceState createRace() {
		String r = data.getString("race"+(index++));
		if (r == null) return null;
		RaceState race = new RaceState(forceLaps);
		race.addListener(this);
        getApplication().enqueue(() -> {
            race.load((J3map)assetManager.loadAsset(r), players);
        });
		return race;
	}
	
}
