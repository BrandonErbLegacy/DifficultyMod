package nightwraid.diff.general;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import nightwraid.diff.commands.GetCurrentKillCounts;
import nightwraid.diff.commands.GetCurrentKillsToLevel;
import nightwraid.diff.commands.GetDifficultyLevelCommand;
import nightwraid.diff.commands.GetLocalDifficultyLevelCommand;
import nightwraid.diff.commands.SetDebugModeCommand;
import nightwraid.diff.commands.SetDifficultyLevelCommand;
import nightwraid.diff.commands.SpawnMobWithModifierCommand;
import nightwraid.diff.events.EntityEvents;
import nightwraid.diff.events.PlayerEvents;
import nightwraid.diff.utils.PlayerDifficultyHelper;

@Mod(modid=DifficultyModConstants.MODID, name=DifficultyModConstants.MODNAME, version=DifficultyModConstants.VERSION, acceptedMinecraftVersions=DifficultyModConstants.ACCEPTED_MINECRAFT_VERSIONS)
public class DifficultyMod {
	@Instance
	public static DifficultyMod instance;
	
	public static org.apache.logging.log4j.Logger logger = null;
	public static PlayerDifficultyHelper pdh = new PlayerDifficultyHelper();	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(EntityEvents.class);
		MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	@EventHandler
	public void ServerStartingEvent(FMLServerStartingEvent event) {
		event.registerServerCommand(new GetDifficultyLevelCommand());
		event.registerServerCommand(new GetLocalDifficultyLevelCommand());
		event.registerServerCommand(new GetCurrentKillsToLevel());
		event.registerServerCommand(new GetCurrentKillCounts());
		event.registerServerCommand(new SetDifficultyLevelCommand());
		event.registerServerCommand(new SetDebugModeCommand());
		event.registerServerCommand(new SpawnMobWithModifierCommand());
	}
	
	@EventHandler
	public void ServerStoppingEvent(FMLServerStoppingEvent event) {

	}
}
