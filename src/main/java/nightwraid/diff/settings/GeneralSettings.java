package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nightwraid.diff.general.DifficultyModConstants;

/* This config is for general things about gameplay */
@Config(modid = DifficultyModConstants.MODID, name="DifficultySettings", category="General")
public class GeneralSettings {
	//Player Settings
	@Config.Comment("This is the default player difficulty")
	public static int playerDefaultDifficultyTicks = 0;
	
	@Config.Comment("This number of normal kills to increase difficulty")
	public static int playerNormalKillsDifficultyTick = 2;
	
	@Config.Comment("This number of boss kills to increase difficulty")
	public static int playerBossKillsDifficultyTick = 2;
	
	@Config.Comment("The distance from spawn for the difficulty modifier to take place (WIP)")
	public static double diffModifierPerKM = .01;
	
	@Config.Comment("Does difficulty increase by killing normal entities?")
	public static boolean allowDifficultyTickByNormal = true;
	
	//World Settings
	@Config.Comment("Distance used to check for highest difficulty")
	public static int distanceDiffAffects = 256; 
	
	//Debug settings
	@Config.Comment("Is the debugger activated?")
	public static boolean debugModeEnabled = true;
}
