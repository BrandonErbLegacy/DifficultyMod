package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import nightwraid.diff.general.DifficultyModConstants;

@Config(modid = DifficultyModConstants.MODID, name="DifficultySettingsMods", category="Decaying")
public class EntityDecayingSettings {
	@Config.Comment("The decaying modifier becomes enabled at difficulty 10")
	public static int levelDecayingEnabled = 10;
	
	@Config.Comment("Equipment decay begins at difficulty 10")
	public static int levelOneDecayEquipmentEnabled = 10;
	
	@Config.Comment("Stage one of decay is 1 tick per hit")
	public static int levelOneDecayEquipmentTick = 1;
	
	@Config.Comment("Stage two decay begins at difficulty 25")
	public static int levelTwoDecayEquipmentEnabled = 25;
	
	@Config.Comment("Stage two of decay is 3 tick per hit")
	public static int levelTwoDecayEquipmentTick = 3;
	
	@Config.Comment("Stage three decay begins at difficulty 50")
	public static int levelThreeDecayEquipmentEnabled = 50;
	
	@Config.Comment("Stage three of decay is 5 tick per hit")
	public static int levelThreeDecayEquipmentTick = 5;
	
	@Config.Comment("Decaying mobs can apply a wither debuff if they hit you after this level")
	public static int witherDebuffOnHitEnabled = 30;
	
	@Config.Comment("Duration of the wither debuff")
	public static int witherDebuffDuration = 30;
}
