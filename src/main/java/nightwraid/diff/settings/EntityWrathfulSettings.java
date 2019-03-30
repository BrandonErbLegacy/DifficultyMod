package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import nightwraid.diff.general.DifficultyModConstants;

@Config(modid = DifficultyModConstants.MODID, name="DifficultySettingsMods", category="Wrathful")
public class EntityWrathfulSettings {
	@Config.Comment("Wrathful mobs can begin spawning at difficulty: 25")
	public static int wrathfulLevelEnabled = 25;
	
	
}
