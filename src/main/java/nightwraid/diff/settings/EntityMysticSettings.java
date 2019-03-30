package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;

public class EntityMysticSettings {
	@Config.Comment("The mystic modifier becomes enabled at difficulty 40")
	public static int levelMysticEnabled = 40;
	
	@Config.Comment("The mob will attempt to retreat and regen health until it's at 70% hp")
	public static double retreatRegenUntilPercent = .70;
	
	@Config.Comment("The mob will attempt to retreat at this percent hp")
	public static double retreatAtPercent = .30;
	
	@Config.Comment("How long the mystic will wait to siege you, if unable to reach and not being hit")
	public static double mysticSiegeDelay = 600;
	
	@Config.Comment("When the mystic will warn you")
	public static double mysticSiegeWarn = 300;
	
	@Config.Comment("For every diff tick, the mystic can recruit x mobs. Value rounded to 1 at lowest")
	public static double mysticMobsRecruitedPerDiff = .04;
	
	@Config.Comment("Mystic mobs will attempt to recruit other mobs within this range")
	public static int mysticMobsRecruitRange = 15;
	
	
}
