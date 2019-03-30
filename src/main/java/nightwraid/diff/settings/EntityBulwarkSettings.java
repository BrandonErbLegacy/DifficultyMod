package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nightwraid.diff.general.DifficultyModConstants;

@Config(modid = DifficultyModConstants.MODID, name="DifficultySettingsMods", category="Bulwark")
public class EntityBulwarkSettings {
	//Player Settings
	@Config.Comment("The bulwark modifier becomes enabled at difficulty 5")
	public static int levelBulwarkEnabled = 5;
	
	@Config.Comment("Bulwark creatures can have at most x% of their hp as absorption when they spawn (default: 50% (.5))")
	public static double bulwarkAbsorptionCapped = .5;
	
	@Config.Comment("Bulwark creatures gain x% of their hp as absorption per difficulty level (default: 1% (.01))")
	public static double bulwarkAdditionalAbsorbPerDiff = .01;
	
	@Config.Comment("Bulwark creatures take x% reduced damage from all sources (default: 25% (.25))")
	public static double bulwarkDamageReduction = .25;
	
	@Config.Comment("Bulwark creatures regen for x seconds after taking damage (default: 5)")
	public static int bulwarkRegenDuration = 5;
	
	@Config.Comment("Chance a bulwark resists being one-shot")
	public static double bulwarkResistOneshot = .3;
	
	@Config.Comment("Difficulty that a bulwark unlocks the resist one-shotting ability")
	public static double bulwarkResistOneshotUnlocked = 30;

}
