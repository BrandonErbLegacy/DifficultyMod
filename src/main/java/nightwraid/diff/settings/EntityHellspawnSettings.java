package nightwraid.diff.settings;

import net.minecraftforge.common.config.Config;
import nightwraid.diff.general.DifficultyModConstants;

@Config(modid = DifficultyModConstants.MODID, name="DifficultySettingsMods", category="Hellspawn")
public class EntityHellspawnSettings {
	@Config.Comment("The hellspawn modifier becomes enabled at difficulty 0")
	public static int levelHellspawnEnabled = 1;
	
	@Config.Comment("Hellspawn become immune to fire damage at level 0")
	public static int levelHellspawnImmuneToFire = 0;
	
	@Config.Comment("Hellspawn can set their target on fire at level 7")
	public static int levelHellspawnFireAttacks = 7;
	
	@Config.Comment("Duration of fire attacks")
	public static int durationHellspawnFireAttack = 5;	
	
	@Config.Comment("Hellspawn gain their set ablaze special at level 10")
	public static int levelHellspawnSetAblazeSpecial = 20;
	
	@Config.Comment("Range the set ablaze special affects (10 meters)")
	public static int hellspawnSetAblazeRange = 10;
	
	@Config.Comment("Cool down of the set ablaze attack (60 seconds?)")
	public static int hellspawnSetAblazeCooldown = 600;
	
	@Config.Comment("Cool down of the set ablaze attack (45 seconds?)")
	public static int hellspawnSetAblazeWarning = 450;
	
	@Config.Comment("Hellspawn explode on death unlocked at level 25")
	public static int levelHellspawnExplode = 25;
	
	@Config.Comment("The strength hellspawn will explode with when they die (default 3)")
	public static int hellspawnExplosionStrength = 3;
	
	@Config.Comment("Whether or not passive mobs can use the hellspawn set ablaze special")
	public static boolean hellspawnPassiveCanUseAblaze = false;
	
	@Config.Comment("Whether or not passive mobs can use the hellspawn explode special")
	public static boolean hellspawnPassiveCanExplode = false;
}
