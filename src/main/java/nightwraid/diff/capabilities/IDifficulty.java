package nightwraid.diff.capabilities;

import java.util.List;

public interface IDifficulty {
	void setDifficulty(int diff);
	int getDifficulty();
	void addModifier(String modifierName);
	List<String> getModifiers();
	boolean hasModifier(String modifierName);
	boolean hasEffect();
}