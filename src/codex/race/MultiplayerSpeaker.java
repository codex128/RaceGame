/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

import codex.jmeutil.audio.AudioModel;
import codex.jmeutil.audio.SFXSpeaker;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioParam;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class MultiplayerSpeaker extends SFXSpeaker {
	
	LinkedList<Voter> voters = new LinkedList<>();

	public MultiplayerSpeaker(AudioData data, AudioModel model) {
		super(data, model);
	}
	public MultiplayerSpeaker(AssetManager assets, AudioModel model) {
		super(assets, model);
	}
	public MultiplayerSpeaker(AssetManager assets, String name, AudioModel model) {
		super(assets, name, model);
	}
	public MultiplayerSpeaker(AssetManager assets, AudioModel model, boolean cache) {
		super(assets, model, cache);
	}
	public MultiplayerSpeaker(AssetManager assets, String name, AudioModel model, boolean cache) {
		super(assets, name, model, cache);
	}
	
	public boolean registerVoter(Object owner) {
		if (!voters.stream().noneMatch(v -> v.owner == owner)) return false;
		voters.add(new Voter(owner));
		refresh();
		return true;
	}
	public boolean removeVoter(Object owner) {
		Voter voter = voters.stream().filter(v -> v.owner == owner).findAny().orElse(null);
		if (voter == null) return false;
		return voters.remove(voter);
	}
	public void registerVoters(Object... owners) {
		for (Object a : owners) {
			registerVoter(a);
		}
	}
	public void castVote(Object owner, boolean favor) {
		voters.stream().filter(v -> v.owner == owner).findAny().orElse(null).favor = favor;
		refresh();
	}	
	public void clearRegistry() {
		voters.clear();
		refresh();
	}
	@Override
	public float getVolumeFactor() {
		float votes = voters.stream().filter(v -> v.favor).count();
		return super.getVolumeFactor()*(votes/voters.size());
	}
	
	private void refresh() {
		if (voters.isEmpty() || voters.stream().noneMatch(v -> v.favor)) {
			if (isPlaying()) stop();
		}
		else if (!isPlaying()) {
			play();
		}
		updateSourceParam(AudioParam.Volume);
	}
	
	
	private static final class Voter {
		boolean favor = false;
		Object owner;
		private Voter(Object owner) {
			this.owner = owner;
		}
	}
	
}
