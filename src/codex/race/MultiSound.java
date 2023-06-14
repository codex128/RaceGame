/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.jmeutil.audio.SFXSpeaker;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class MultiSound {
    
    SFXSpeaker sound;
    LinkedList<Object> emitters = new LinkedList<>();
    
    public MultiSound(SFXSpeaker sound) {
        this.sound = sound;
    }
    
    public SFXSpeaker getSound() {
        return sound;
    }
    public Collection<Object> getEmitters() {
        return emitters;
    }
    public String getName() {
        return sound.getModel().getSourceFile();
    }
    
    private void refresh() {
        if (!emitters.isEmpty()) {
            if (!sound.isPlaying()) sound.play();
        }
        else if (sound.isPlaying()) {
            sound.stop();
        }
    }
    
    public void addEmitter(Object emitter) {
        if (!emitters.contains(emitter)) {
            emitters.add(emitter);
            refresh();
        }
    }
    public void removeEmitter(Object emitter) {
        emitters.remove(emitter);
        refresh();
    }
    
}
