/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.race;

/**
 *
 * @author gary
 */
public class PersistantString <T> {
	
	Object[] string;
	
	public PersistantString(Object... string) {
		this.string = string;
	}
	
	public String refresh() {
		String out = "";
		for (Object s : string) out += s.toString();
		return out;
	}
	@Override
	public String toString() {
		return refresh();
	}
	
}
