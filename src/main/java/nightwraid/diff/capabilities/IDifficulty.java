package nightwraid.diff.capabilities;

import java.util.List;

import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.GeneralSettings;

public interface IDifficulty {
	void setDifficulty(int diff);
	int getDifficulty();
	void addModifier(String modifierName);
	List<String> getModifiers();
	boolean hasModifier(String modifierName);
	boolean hasEffect();
	public void setMobsKilled(int val);
	public int getMobsKilled();
	public void incrementMobsKilled();
	public void setBossesKilled(int val);
	public int getBossesKilled();
	public void incrementBossesKilled();
}