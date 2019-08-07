package nightwraid.diff.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.effects.EffectManager;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.GeneralSettings;

public class DifficultyCapabilityHelper {
	public static int GetEntityDifficulty(Entity entity) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			return diff.getDifficulty();
		} else {
			return GeneralSettings.playerDefaultDifficultyTicks;
		}
	}
	
	public static void SetEntityDifficulty(Entity entity, int newDiff) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			diff.setDifficulty(newDiff);
		}
	}
	
	public static void AddModifier(Entity entity, String modifier) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			diff.addModifier(modifier);
		}
	}
	
	public static List<String> GetModifiers(Entity entity) {
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			return diff.getModifiers();
		} else {
			return new ArrayList<String>();
		}
	}
	
	public static boolean HasModifier(Entity entity, String modifier) {
		List<String> modifiers = GetModifiers(entity);
		return modifiers.contains(modifier);
	}
	
	public static List<ISpecialEffect> GetSpecialEffectsFromEntity(Entity entity){
		List<ISpecialEffect> values = new ArrayList<ISpecialEffect>();
		for (String modifier:GetModifiers(entity)) {
			ISpecialEffect effect = EffectManager.GetSpecialEffectByName(modifier);
			if (effect != null) {
				values.add(effect);
			}
		}
		return values;
	}
}
