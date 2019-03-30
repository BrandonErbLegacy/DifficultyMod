package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import nightwraid.diff.general.DifficultyModConstants;

/* This config is for settings that affect all entities */
@Config(modid = DifficultyModConstants.MODID, name="DifficultySettings", category="Entities")
public class EntitySettings {
	@Config.Comment("At what level can mobs start gaining hp")
	public static int mobsGainHPUnlocked = 5;
	
	@Config.Comment("At what level can mobs start gaining strength")
	public static int mobsGainStrengthUnlocked = 10;
	
	@Config.Comment("At what level can mobs start gaining speed")
	public static int mobsGainSpeedUnlocked = 10;
	
	@Config.Comment("At what level can mobs start gaining knockback resistance")
	public static int mobsGainKnockbackResistUnlocked = 20;
	
	@Config.Comment("Speed has to be tightly controlled because of how fast it gets out of hand")
	public static double mobsSpeedIncreaseFactor = 1.015;
	
	@Config.Comment("Speed has to be tightly controlled because of how fast it gets out of hand")
	public static boolean canModPassiveMobs = false;
	
	@Config.Comment("The chance percent that a mob can spawn with one or more modifiers, per level of difficulty (.0025, or .25%)")
	public static double mobChanceOfBeingModded = .0025;
	
	@Config.Comment("The capped chance that a mob will spawn being modded (default: .3, or 30%)")
	public static double mobMaxChanceBeingModded = .3;
	
	@Config.Comment("How many difficulty ticks per additional modifier")
	public static int unlockAdditionalModsPerDiff = 75;
	
	/* Experimental. Might add this to all stats. Or remove it altogether */
	@Config.Comment("This is the max hp mobs can gain. Defaults to 4, for 400% of their norm")
	public static int mobsMaxHPGain = 4;
	
	@Config.Comment("This is the max hp mobs can gain. Defaults to 200, for 200 additional str")
	public static int mobsMaxSTRGain = 200;
}
