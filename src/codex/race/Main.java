package codex.race;

import codex.j3map.J3map;
import codex.j3map.J3mapFactory;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.IntegerProcessor;
import codex.j3map.processors.StringProcessor;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements RaceListener {
	
	BulletAppState bulletapp;
	Player[] players;
	J3map grandprix;
	int prixIndex = 0;
	RaceState race;
	
	public Main() {
		super((AppState) null);
	}
	
    public static void main(String[] args) {
        Main app = new Main();
		AppSettings as = new AppSettings(true);
		as.setUseJoysticks(true);
		app.setSettings(as);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
		GuiGlobals.initialize(this);
		GuiGlobals.getInstance().setCursorEventsEnabled(false);
		Functions.initialize(GuiGlobals.getInstance().getInputMapper());
		
		assetManager.registerLoader(J3mapFactory.class, "j3map");
		J3mapFactory.registerAllProcessors(
				StringProcessor.class, FloatProcessor.class, IntegerProcessor.class);
		
		cam.setFrustumPerspective(120f, cam.getAspect(), .5f, 100f);
		
		bulletapp = new BulletAppState();
		//bulletapp.setDebugEnabled(true);
		stateManager.attach(bulletapp);
		
		J3map commonCar = (J3map)assetManager.loadAsset("Properties/MyCar.j3map");
		players = new Player[]{new Player(0), new Player(1), /*new Player(2), /*new Player(3)*/};
		for (Player p : players) {
			p.setCarData(commonCar);
		}
		
		grandprix = (J3map)assetManager.loadAsset("Properties/GrandPrix1.j3map");
		stateManager.attach(createRace());
		
    }
    @Override
    public void simpleUpdate(float tpf) {		
		
	}
    @Override
    public void simpleRender(RenderManager rm) {}
	@Override
	public void onRaceComplete(RaceState race) {
		if (prixIndex == grandprix.getInteger("length")) return;
		stateManager.detach(race);
		enqueue(() -> {
			stateManager.attach(createRace());
		});
	}
	
	public RaceState createRace() {
		String r = grandprix.getString("race"+(++prixIndex));
		if (r == null) return null;
		RaceState race = new RaceState((J3map)assetManager.loadAsset(r), players);
		race.addListener(this);
		return race;
	}
	
}
