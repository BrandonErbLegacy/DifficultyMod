package nightwraid.diff.capabilities;

import java.util.ArrayList;
import java.util.List;

import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.GeneralSettings;

public class DifficultyCapability implements IDifficulty {
	int difficultyLevel = GeneralSettings.playerDefaultDifficultyTicks;
	int mobsKilled = 0;
	int bossesKilled = 0;
	List<String> modifiers = new ArrayList<String>();
	
	public void setDifficulty(int diff) {
		this.difficultyLevel = diff;
	}
	public int getDifficulty() {
		return this.difficultyLevel;
	}
	public void addModifier(String modifierName) {
		this.modifiers.add(modifierName);
	}
	public List<String> getModifiers(){
		return this.modifiers;
	}
	public boolean hasModifier(String modifierName) {
		return (this.modifiers.contains(modifierName));
	}
	public boolean hasEffect() {
		return (this.modifiers.size() > 0);
	};
	public void setMobsKilled(int val) {
		this.mobsKilled = val;
	}
	public int getMobsKilled() {
		return this.mobsKilled;
	}
	public void incrementMobsKilled() {
		this.mobsKilled++;
	}
	public void setBossesKilled(int val) {
		this.bossesKilled = val;
	}
	public int getBossesKilled() {
		return this.bossesKilled;
	}
	public void incrementBossesKilled() {
		this.bossesKilled++;
	}
}
