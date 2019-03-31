package nightwraid.diff.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import nightwraid.diff.effects.EffectManager;
import nightwraid.diff.effects.ISpecialEffect;

public class TagHelper {

	public static Integer GetDifficultyFromTags(Set<String> set) {
		int returnVal = -1;
		for (String tag : set) {
			if (tag.contains(ModifierNames.MOB_LEVEL_DENOTATION)) {
				returnVal = Integer.parseInt(tag.replace(ModifierNames.MOB_LEVEL_DENOTATION, ""));
			}
		}

		return returnVal;
	}
	public static void SetDifficultyFromEntity(Entity entity, int difficulty) {
		//Remove any other difficulty tags if they exist
		Set<String> tags = entity.getTags();
		String tagToRemove = null;
		if (GetDifficultyFromTags(tags) != -1) {
			for (String tag: tags) {
				if (tag.contains(ModifierNames.MOB_LEVEL_DENOTATION)) {
					tagToRemove = tag;
				}
			}
		}
		if (tagToRemove != null) {
			entity.removeTag(tagToRemove);
		}
		entity.addTag(ModifierNames.MOB_LEVEL_DENOTATION+(String.valueOf(difficulty)));
	}
	
	public static List<ISpecialEffect> GetSpecialEffectsFromEntity(Entity entity){
		List<ISpecialEffect> values = new ArrayList<ISpecialEffect>();
		for (String tag:entity.getTags()) {
			if (tag.contains(ModifierNames.MOB_CAPABILITY_DENOTATION)) {
				String name = tag.replace(ModifierNames.MOB_CAPABILITY_DENOTATION, "");
				ISpecialEffect effect = EffectManager.GetSpecialEffectByName(name);
				if (effect != null) {
					values.add(effect);
				}
			}
		}
		return values;
	}

	public static boolean MobHasBeenModded(Set<String> set) {
		if (set.contains(ModifierNames.MOB_MODDED_DENOTATION)) {
			return true;
		}
		return false;
	}
	
	public static void SetMobHasBeenModded(EntityLiving entity) {
		entity.addTag(ModifierNames.MOB_MODDED_DENOTATION);
	}

	public static boolean MobHasBeenModded(EntityLiving entity) {
		Set<String> set = entity.getTags();
		return MobHasBeenModded(set);
	}

	public static boolean MobHasBeenModded(Entity entity) {
		return MobHasBeenModded((EntityLiving) entity);
	}
	
	public static boolean MobHasBeenModded(EntityPlayer player) {
		return MobHasBeenModded(player.getTags());
	}
}
