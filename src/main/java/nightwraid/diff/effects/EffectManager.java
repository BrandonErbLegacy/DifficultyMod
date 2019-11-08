package nightwraid.diff.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.effects.modifiers.SpecialEffectBulwark;
import nightwraid.diff.effects.modifiers.SpecialEffectDecaying;
import nightwraid.diff.effects.modifiers.SpecialEffectHellspawn;
import nightwraid.diff.effects.modifiers.SpecialEffectMystic;
import nightwraid.diff.effects.modifiers.SpecialEffectRaidBoss;
import nightwraid.diff.effects.modifiers.SpecialEffectSapping;
import nightwraid.diff.effects.modifiers.SpecialEffectWrathful;
import nightwraid.diff.effects.stats.StatGainHP;
import nightwraid.diff.effects.stats.StatGainKRESIST;
import nightwraid.diff.effects.stats.StatGainSPD;
import nightwraid.diff.effects.stats.StatGainSTR;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.EntitySettings;
import nightwraid.diff.settings.GeneralSettings;
import nightwraid.diff.utils.LogHelper;

public class EffectManager {
	/* Register our effects and etc for use */
	private static IStatModifier[] KnownStatChanges = new IStatModifier[] {
		new StatGainHP(),
		new StatGainSTR(),
		new StatGainSPD(),
		new StatGainKRESIST(),
	};
	private static ISpecialEffect[] KnownSpecialEffects = new ISpecialEffect[] {
		new SpecialEffectBulwark(),
		new SpecialEffectRaidBoss(),
		new SpecialEffectHellspawn(),
		new SpecialEffectDecaying(),
		new SpecialEffectMystic(), 
		new SpecialEffectWrathful(),
		new SpecialEffectSapping(),
	};
	private static ISpecialEquipment[] KnownEquipmentSets = new ISpecialEquipment[] {
			
	};
	
	/* This method kicks everything off */
	public static void AmpUpMob(EntityLiving entity, int difficulty) {
		
		//TODO: I'm 99% sure that these get reapplied now, with the change from the tag system to the capability system
		//Investigate and removed the duplicated mod/stat bug
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		LogHelper.LogInfo("Diff is null: "+(diff == null));
		if (diff != null) {
			diff.setDifficulty(difficulty);
		}
		ApplyStats(entity, difficulty);
		ApplyMods(entity, difficulty);
		ApplyEquipment(entity, difficulty);
	}
	
	private static void ApplyMods(EntityLiving entity, int difficulty) {
		int numberOfMods = 0;
		int numberOfRolls = 1 + ((int) difficulty/EntitySettings.unlockAdditionalModsPerDiff);
		int maxTries = 10;
		
		double chanceOfBeingModded = difficulty * EntitySettings.mobChanceOfBeingModded;
		if (chanceOfBeingModded > EntitySettings.mobMaxChanceBeingModded) {
			chanceOfBeingModded = EntitySettings.mobMaxChanceBeingModded;
		}
		
		int i = 0;
		while (i < numberOfRolls) {
			i++;
			double roll = Math.random();
			if (roll <= chanceOfBeingModded) {
				numberOfMods++;
			}
		}
		List<Integer> indexesToGrab = new ArrayList<Integer>();
		List<ISpecialEffect> unlockedMods = new ArrayList<ISpecialEffect>();
		Random rand = new Random();
		
		for (ISpecialEffect effect:KnownSpecialEffects) {
			if (difficulty >= effect.GetLevel()) {
				//Bosses cannot be raid bosses. That would be aids
				//Mobs spawned via a raid boss shouldn't be a raid boss either. That'd be shitty too
				if (effect.GetName().equals(SpecialEffectRaidBoss.NAME) && ((!entity.isNonBoss()) || SpecialEffectRaidBoss.SpawnedByRaidBoss(entity))) {
					continue;
				}
				unlockedMods.add(effect);
			}
		}
		
		int tryNum = 0;
		while (indexesToGrab.size() < numberOfMods) {
			tryNum++;
			int val = rand.nextInt(unlockedMods.size());
			if (indexesToGrab.contains(val)) {
				continue;
			} else {
				indexesToGrab.add(val);
			}
			if (tryNum > maxTries) {
				break;
			}
		}
		String modsAdded = "Spawned "+entity.getName()+" with ";
		for (int val: indexesToGrab) {
			ISpecialEffect effectToAdd = unlockedMods.get(val);
			//entity.addTag(ModifierNames.MOB_CAPABILITY_DENOTATION+effectToAdd.GetName());
			IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			if (diff != null) {
				diff.addModifier(effectToAdd.GetName());
			}
			
			modsAdded = modsAdded +effectToAdd.GetName()+ ", ";
		}
		if (GeneralSettings.debugModeEnabled && indexesToGrab.size() > 0) {
			DifficultyMod.logger.info(modsAdded);
		}
	}
	
	private static void ApplyStats(EntityLiving entity, int difficulty) {
		for (IStatModifier statChange:KnownStatChanges) {
			statChange.ApplyStat(entity, difficulty);
		}
		if (GeneralSettings.debugModeEnabled) {
			DifficultyMod.logger.info("Entity Max HP\t\t: "+entity.getMaxHealth());
			
			IAttributeInstance strengthAttr = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			if (strengthAttr != null) {
				DifficultyMod.logger.info("Entity Strength\t\t: "+strengthAttr.getAttributeValue());
			}
			
			IAttributeInstance speedAttr = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			DifficultyMod.logger.info("Entity Speed\t\t\t: "+speedAttr.getAttributeValue());
		}
	}
	
	private static void ApplyEquipment(EntityLiving entity, int difficulty) {
		
	}
	
	public static ISpecialEffect GetSpecialEffectByName(String name) {
		for (ISpecialEffect effect:KnownSpecialEffects) {
			if (effect.GetName().equals(name)) {
				return effect;
			}
		}
		return null;
	}
	
	public static boolean ValidateModifierType(String name) {
		if (GetSpecialEffectByName(name) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void TriggerUnlockMessages(EntityPlayer player, int difficulty) {
		for (ISpecialEffect effect:KnownSpecialEffects) {
			effect.AbilityUnlockHandler(player, difficulty);
		}
	}
}
