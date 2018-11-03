package nightwraid.diff.general;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Utilities {
	public static void WorldAnnouncement(World world, String message) {
		for (EntityPlayer player:world.playerEntities) {
			player.sendMessage(new TextComponentString(message));
		}
	}
	
	public static Integer GetDifficultyFromTags(Set<String> set) {
		int returnVal = -1;
		for (String tag:set) {
			if (tag.contains("Challenge:")) {
				returnVal = Integer.parseInt(tag.replace("Challenge:", ""));
			} 
		}
		return returnVal;
	}
	
	public static double ComputeBaseDiffFormula(Integer diff, int baseVal) {
		double exponent = diff/2;
		double asquaredFactor = Math.pow(DifficultyConfigGeneral.exponentIncreaseBase, exponent);
		double valueOne = asquaredFactor*diff;
		double valueTwo = diff * DifficultyConfigGeneral.flatModifier;
		return (baseVal + (valueOne+valueTwo));
	}
	public static double ComputeBaseDiffFormula(Integer diff, double baseVal) {
		double exponent = diff/2;
		double asquaredFactor = Math.pow(DifficultyConfigGeneral.exponentIncreaseBase, exponent);
		double valueOne = asquaredFactor*diff;
		double valueTwo = diff * DifficultyConfigGeneral.flatModifier;
		return (baseVal + (valueOne+valueTwo));
	}
	
	public static double ComputeModifierDiffFormula(Integer diff, int baseVal) {
		double factor = diff/5;
		return (baseVal * Math.pow(DifficultyConfigGeneral.tinyIncreaseFactor, factor));
	}
	public static double ComputeModifierDiffFormula(Integer diff, double baseVal) {
		double factor = diff/5;
		return (baseVal * Math.pow(DifficultyConfigGeneral.tinyIncreaseFactor, factor));
	}
}
