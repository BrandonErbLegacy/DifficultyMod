package nightwraid.diff.general;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import nightwraid.diff.commands.ChangePlayerDifficultyCommand;
import nightwraid.diff.commands.GetDifficultyLevelCommand;
import nightwraid.diff.commands.GetLocalDifficulty;
import nightwraid.diff.commands.GetMobValues;
import nightwraid.diff.commands.SpawnSkeletonWithModCommand;
import nightwraid.diff.events.EntityHandlers;
import nightwraid.diff.events.PlayerHandlers;

@Mod(modid=Settings.MODID, name=Settings.MODNAME, version=Settings.VERSION, acceptedMinecraftVersions=Settings.ACCEPTED_MINECRAFT_VERSIONS)
public class DifficultyMod {
	@Instance
	public static DifficultyMod instance;
	
	public static PlayerDifficultyManager pdm = new PlayerDifficultyManager();
	public static org.apache.logging.log4j.Logger logger = null;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(EntityHandlers.class);
		MinecraftForge.EVENT_BUS.register(PlayerHandlers.class);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		//System.out.println(Settings.MODID + ":init");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		//System.out.println(Settings.MODID + ":postInit");
		//System.out.println(Difficulty.playerDifficulty);
	}
	
	@EventHandler
	public void ServerStartingEvent(FMLServerStartingEvent event) {
		//System.out.println("Server starting");
		event.registerServerCommand(new ChangePlayerDifficultyCommand());
		event.registerServerCommand(new SpawnSkeletonWithModCommand());
		event.registerServerCommand(new GetDifficultyLevelCommand());
		event.registerServerCommand(new GetLocalDifficulty());
		event.registerServerCommand(new GetMobValues());
		//event.
	}
	
	@EventHandler
	public void ServerStoppingEvent(FMLServerStoppingEvent event) {
		//Ensure we save our configs
		ConfigManager.sync(Settings.MODID, Config.Type.INSTANCE);
	}
}
