package org.laserforce.scaling.common;

/**
 * Represents a position in a SpaceMarines game.
 * 
 * @author williamf
 */
public enum GamePosition {

	COMMANDER("Commander"),
	HEAVY("Heavy"),
	SCOUT("Scout"),
	AMMO("Ammo"),
	MEDIC("Medic");
	
	private final String name;
	
	private GamePosition(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
