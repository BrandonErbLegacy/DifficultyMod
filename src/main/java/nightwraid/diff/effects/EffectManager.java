package nightwraid.diff.effects;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import nightwraid.diff.general.Utilities;

public class EffectManager {
	public static void ApplyChallengeIfNone(Entity target, Integer diff) {
		int challengeDiff = GetDifficultyFromTags(target.getTags());
		if (challengeDiff == -1) {
			target.addTag("Challenge:"+diff);
		}
	}
	
	public static void ApplyEffects(Entity target, Integer diff) {
		EntityLiving entity = (EntityLiving) target;
		ApplyStats stats = new ApplyStats(entity, diff);
		ApplyChallengeIfNone(target, diff);
	}
	
	public static void ApplyGear(Entity target, Integer diff) {
		EntityLiving entity = (EntityLiving) target;
		ApplyGear gear = new ApplyGear(entity, diff);
		ApplyChallengeIfNone(target, diff);
	}
	
	public static void ApplyMods(Entity target, Integer diff) {
		EntityLiving entity = (EntityLiving) target;
		ApplyMods mods = new ApplyMods(entity, diff);
		ApplyChallengeIfNone(target, diff);
	}
	
	public static ISpecialEffect MatchTagToEffect(String tag) {
		if (tag.contains("RaidBoss")) {
			return new SpecialEffectRaidBoss();
		} else if (tag.contains("Bulwark")) {
			return new SpecialEffectBulwark();
		} else if (tag.contains("Hellspawn")) {
			return new SpecialEffectHellspawn();
		} else if (tag.contains("Mystic")) {
			return new SpecialEffectMystic();
		} else if (tag.contains("Decaying")) {
			return new SpecialEffectDecaying();
		}
		return null;
	}
	
	public static Integer GetDifficultyFromTags(Set<String> set) {
		return Utilities.GetDifficultyFromTags(set);
	}
	
	/*public static void MultBuffKnockbackResist(EntityLivingBase entity, Integer amt) {
		
	}*/
}
