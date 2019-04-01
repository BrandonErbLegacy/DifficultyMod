package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import nightwraid.diff.general.DifficultyModConstants;

@Config(modid = DifficultyModConstants.MODID, name="DifficultySettingsMods", category="Sapping")
public class EntitySappingSettings {
	@Config.Comment("The sapping modifier becomes enabled at difficulty 50")
	public static int levelSappingEnabled = 15;
	
	@Config.Comment("The level at which the drain well special becomes available")
	public static int levelDrainWellEnabled = 15;
	
	@Config.Comment("The range targets need to be in, in order to be warned")
	public static int drainWellWarnDistance = 30;
	
	@Config.Comment("When during it's charge will it warn the target(s)")
	public static int drainWellWarnTime = 300;
	
	@Config.Comment("At which point will the drain well activate")
	public static int drainWellActiveTime = 600;
	
	@Config.Comment("The percent hp we must reach before attempting to drain well")
	public static double drainWellTrigger = .50;
	
	@Config.Comment("The conversion ration between damage done to nearby targets and damage healed")
	public static double drainWellConversion = 1;
	
	@Config.Comment("The damage drain well will do to each nearby mob. Based on the sapping mob's max hp")
	public static double drainWellMaxHPPercent = .05; //10 mobs = 50% of it's hp healed basically.
	
	@Config.Comment("The conversion rate for health gained based on damage dealt")
	public static double normalAttackConversionRatio = .75;
}