package nightwraid.diff.capabilities;

import java.util.ArrayList;
import java.util.List;

public class DifficultyCapability implements IDifficulty {
	int difficultyLevel = 0;
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
}
