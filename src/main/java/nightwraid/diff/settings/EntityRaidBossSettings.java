package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import nightwraid.diff.general.DifficultyModConstants;

@Config(modid = DifficultyModConstants.MODID, name="DifficultySettingsMods", category="RaidBoss")
public class EntityRaidBossSettings {
	@Config.Comment("The raid boss modifier becomes enabled at difficulty 50")
	public static int levelRaidBossEnabled = 50;

}
