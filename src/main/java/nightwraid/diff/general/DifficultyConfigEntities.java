package nightwraid.diff.general;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Settings.MODID, name="DifficultySettings", category="EntitySettings")
public class DifficultyConfigEntities {
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnBulwark = 5;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnDecaying = 0;
	
	@Config.Comment("How much damage the decaying effect does to equipment")
	public static int decayingEquipmentDamage = 5;

	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnEnraged = 1;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnFarsight = 1;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnHellspawn = 10;
	
	@Config.Comment("This is the explosion strength hellspawn explode with")
	public static double hellspawnExplosionStr = 2;
	
	@Config.Comment("This is the cool down for the set ablaze attack")
	public static int hellspawnAblazeCooldown = 600; //Seconds?
	
	@Config.Comment("This is range players have to be to be set ablaze")
	public static double hellspawnSetAblazeRange = 20;
	
	@Config.Comment("This is how long players will be on fire for after the attack")
	public static int hellspawnSetAblazeTime = 5;
	
	@Config.Comment("This when to warn players the attack is coming")
	public static int hellspawnWarnAblazeTime = 150;
	
	@Config.Comment("Can non-aggressive hellspawn use their special attack?")
	public static boolean hellspawnPassiveCanUseAblaze = false;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnRaidBoss = 10;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnMystic = 20;
	
	@Config.Comment("For every diff tick, the mystic can recruit x mobs. Value rounded to 1 at lowest")
	public static double mysticMobsRecruitedPerDiff = .04;
	
	@Config.Comment("For every diff tick, the mystic can recruit x mobs. Value rounded to 1 at lowest")
	public static int mysticMobsRecruitRange = 30;
	
	@Config.Comment("The percent of hp that triggers a mystic retreat")
	public static double mysticMobsRetreatPercent = .3;
	
	@Config.Comment("The percent of hp that triggers a mystic reengage")
	public static double mysticMobsReengagePercent = .6;
	
	@Config.Comment("How long the mystic will wait to siege you, if unable to reach and not being hit")
	public static double mysticSiegeDelay = 300;
	
	@Config.Comment("When the mystic will warn you")
	public static double mysticSiegeWarn = 150;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnSapping = 1;
	
	@Config.Comment("There is a 0 percent chance to spawn this type until difficulty of x")
	public static int threshholdToSpawnWeightless = 1;
	
	@Config.Comment("The spawn rate of these increases every x difficulty ticks")
	public static double increaseRateEveryXTicks = 5;
	
	@Config.Comment("This is the rate which the spawn rate increases by")
	public static double increaseRateByX = 1.015;
	
	@Config.Comment("Base chance to spawn a modded mob")
	public static double baseModdedSpawnRate = 1.00;

	@Config.Comment("Every x difficulty ticks allows tick/this modifiers")
	public static double additionalModifierAvailableAt = 100;
}
