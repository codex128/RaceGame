/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.jmeutil.audio.SFXSpeaker;
import com.jme3.app.Application;
import java.util.HashMap;

/**
 *
 * @author gary
 */
public class MultiplayerSoundState extends GameAppState {
	
    HashMap<String, MultiSound> sounds = new HashMap<>();
    
	@Override
	protected void init(Application app) {}
	@Override
	protected void cleanup(Application app) {
        clearAllSounds();
    }
	@Override
	protected void onEnable() {}
	@Override
	protected void onDisable() {}
    
    public void addSound(String sound, SFXSpeaker speaker) {
        sounds.putIfAbsent(sound, new MultiSound(speaker));
    }
    public void startEmittingSound(String sound, Object emitter) {
        sounds.get(sound).addEmitter(emitter);
    }
    public void stopEmittingSound(String sound, Object emitter) {
        sounds.get(sound).removeEmitter(emitter);
    }
    public SFXSpeaker getSound(String sound) {
        MultiSound s = sounds.get(sound);
        if (s != null) return s.getSound();
        return null;
    }
    public SFXSpeaker removeSound(String sound) {
        return sounds.remove(sound).getSound();
    }
    public void removeSounds(String... s) {
        for (String str : s) {
            removeSound(str);
        }
    }
    public void clearAllSounds() {
        sounds.clear();
    }
	
}
