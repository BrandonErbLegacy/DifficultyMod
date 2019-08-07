package nightwraid.diff.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.settings.GeneralSettings;

public class DifficultyCapabilityHelper {
	public int GetEntityDifficulty(Entity entity) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			return diff.getDifficulty();
		} else {
			return GeneralSettings.playerDefaultDifficultyTicks;
		}
	}
	
	public void SetEntityDifficulty(Entity entity, int newDiff) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			diff.setDifficulty(newDiff);
		}
	}
	
	public void AddModifier(Entity entity, String modifier) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			diff.addModifier(modifier);
		}
	}
	
	public List<String> GetModifiers(Entity entity) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			return diff.getModifiers();
		} else {
			return new ArrayList<String>();
		}
	}
	
	public boolean HasModifier(Entity entity, String modifier) {
		List<String> modifiers = GetModifiers(entity);
		return modifiers.contains(modifier);
	}
}
