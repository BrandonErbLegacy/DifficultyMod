package nightwraid.diff.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import nightwraid.diff.general.DifficultyConfigEntities;
import nightwraid.diff.general.DifficultyConfigGeneral;
import nightwraid.diff.general.DifficultyMod;

public class ApplyMods {
	public ApplyMods(EntityLiving entity, Integer diff) {
		//Chance of being modded
		int numberOfIncreases = (int)(diff/(DifficultyConfigEntities.increaseRateEveryXTicks));
		double spawnModifier = Math.pow(DifficultyConfigEntities.increaseRateByX, numberOfIncreases);
		Integer floorValue = (int) (((DifficultyConfigEntities.baseModdedSpawnRate * spawnModifier)-1) * 100);
		int numberOfModsAllowed = (int) Math.round(diff/DifficultyConfigEntities.additionalModifierAvailableAt)+1;
		String modsAllowedString = numberOfModsAllowed+" are allowed, set by difficulty of: "+diff;
		//DifficultyMod.logger.info(modsAllowedString);
		List<String> modsToAdd = new ArrayList<String>();
		if (RollForMod(floorValue)) {
			if (!entity.isNonBoss())
				modsToAdd.add("ModCap:RaidBoss");
			for (int i=0; i<numberOfModsAllowed; i++) {
				if (i != 0) {
					//Always allow one if we rolled lucky to get here.
					if (!RollForMod(floorValue)) {
						//We didn't roll well enough to get another mod. Try again for however many we can have
						continue;
					}
				}
				List<String> mods = possibleMods(diff, modsToAdd);
				Random rand = new Random();
				Integer randInt = rand.nextInt(mods.size());
				DifficultyMod.logger.debug(randInt);
				String randomElement = mods.get(randInt);
				modsToAdd.add(randomElement);
			}
			if (!entity.isNonBoss())
				modsToAdd.remove("ModCap:RaidBoss");
				//Can't let bosses also be raid bosses. That would be hell, unless modified
			if (modsToAdd.size() > 0) {
				entity.addTag("Modded");
				String toSay = "Spawning "+entity.getName()+" with mods ";
				String nameString = "";
				for (String mod:modsToAdd) {
					toSay = toSay+mod+", ";
					DifficultyMod.logger.info(toSay);
					entity.addTag(mod);
					nameString = mod.replace("ModCap:", "") + " ";
				}
				//System.out.println(entity.getCustomNameTag());
				if (entity.getCustomNameTag() == null || entity.getCustomNameTag() == "") {
					entity.setCustomNameTag(nameString+entity.getName());
				}
			}
		}
	}
	
	
	public List<String> possibleMods(Integer diff, List<String> exclude){
		List<String> mods = new ArrayList<String>();
		if (diff >= DifficultyConfigEntities.threshholdToSpawnRaidBoss && !exclude.contains("ModCap:RaidBoss")) {
			mods.add("ModCap:RaidBoss");
		}
		if (diff >= DifficultyConfigEntities.threshholdToSpawnBulwark && !exclude.contains("ModCap:Bulwark")) {
			mods.add("ModCap:Bulwark");
		}
		if (diff >= DifficultyConfigEntities.threshholdToSpawnHellspawn && !exclude.contains("ModCap:Hellspawn")) {
			mods.add("ModCap:Hellspawn");
		}
		if (diff >= DifficultyConfigEntities.threshholdToSpawnMystic && !exclude.contains("ModCap:Mystic")) {
			mods.add("ModCap:Mystic");
		}
		if (diff >= DifficultyConfigEntities.threshholdToSpawnDecaying && !exclude.contains("ModCap:Decaying")) {
			mods.add("ModCap:Decaying");
		}
		return mods;
	}
	
	public boolean RollForMod(Integer floor) {
		int roll = (int )(Math.random() * 100 + 1); //Max 100, min of 1
		if (roll <= floor) {
			return true;
		} else {
			return false;
		}
	}

}
