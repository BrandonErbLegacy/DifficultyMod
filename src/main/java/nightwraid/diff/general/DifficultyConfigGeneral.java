package nightwraid.diff.general;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Settings.MODID, name="DifficultySettings", category="General")
public class DifficultyConfigGeneral {
	//Player Settings
	@Config.Comment("This is the default player difficulty")
	public static int playerDefaultDifficultyTicks = 0;
	@Config.Comment("This number of normal kills to increase difficulty")
	public static int playerNormalKillsDifficultyTick = 30;
	@Config.Comment("This number of boss kills to increase difficulty")
	public static int playerBossKillsDifficultyTick = 1;
	
	@Config.Comment("The number of days a player can be alive for before a difficulty increase")
	public static int playerPassiveDaysBeforeIncrease = 5;
	
	@Config.Comment("The distance from spawn for the difficulty modifier to take place (WIP)")
	public static double diffModifierPerKM = .01;
	
	@Config.Comment("Does difficulty increase by killing normal entities?")
	public static boolean allowDifficultyTickByNormal = true;
	
	//World Settings
	@Config.Comment("Distance used to check for highest difficulty")
	public static int distanceDiffAffects = 256; 
	
	//See compute formula 1
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double exponentIncreaseBase = 1.03;
	//See compute formula 1
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double flatModifier = 1.5;
	
	//Mob Settings
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double tinyIncreaseFactor = 1.02;
	@Config.Comment("This is controls the number of ticks required to increase this trait")
	public static int mobSpeedIncreaseTickThreshhold = 10; //300 kills, or 30 bossess
	@Config.Comment("This changes how many ticks we calculate for (e.g, calculate every 5)")
	public static int mobSpeedIncreaseTicksToSkip = 5; 
	
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double mobHealthIncrease = 1.03;
	@Config.Comment("This is controls the number of ticks required to increase this trait")
	public static int mobHealthIncreaseTickThreshhold = 0;
	@Config.Comment("This changes how many ticks we calculate for (e.g, calculate every 5)")
	public static int mobHealthIncreaseTicksToSkip = 2; 
	
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double mobDmgIncreaseAmt = 1.07;
	@Config.Comment("This is controls the number of ticks required to increase this trait")
	public static int mobDmgIncreaseTickThreshhold = 1;
	@Config.Comment("This changes how many ticks we calculate for (e.g, calculate every 5)")
	public static int mobDmgIncreaseTicksToSkip = 5; 
	
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double mobArmorIncreaseAmt = 1.05;
	@Config.Comment("This is controls the number of ticks required to increase this trait")
	public static int mobArmorIncreaseTickThreshhold = 1;
	@Config.Comment("This changes how many ticks we calculate for (e.g, calculate every 5)")
	public static int mobArmorIncreaseTicksToSkip = 5; 
	
	@Config.Comment("This is the amount this trait will be raised per increase")
	public static double mobKnockbackResistAmt = 1.05;
	@Config.Comment("This is controls the number of ticks required to increase this trait")
	public static int mobKnockbackTickThreshhold = 1;
	@Config.Comment("This controls the minimum the value can be, after a set threshhold")
	public static double mobKnockbackMinimum = 0.05; //5%
	@Config.Comment("This changes how many ticks we calculate for (e.g, calculate every 5)")
	public static int mobKnockbackIncreaseTicksToSkip = 5; 

	//@Config.Comment("This is the amount this trait will be raised per increase")
	//public static double mobJumpIncreaseAmt = 1.50;
	//@Config.Comment("This is controls the number of ticks required to increase this trait")
	//public static int mobJumpIncreaseTickThreshhold = 10;
	
	//Factors
	
	
	//Config Handler
	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Settings.MODID)) {
			ConfigManager.sync(Settings.MODID, Config.Type.INSTANCE);
		}
	}
}
