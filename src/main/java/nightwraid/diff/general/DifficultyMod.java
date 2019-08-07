package nightwraid.diff.general;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import nightwraid.diff.capabilities.DifficultyCapabilityFactory;
import nightwraid.diff.capabilities.DifficultyCapabilityHandler;
import nightwraid.diff.capabilities.DifficultyStorage;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.commands.GetCurrentKillCounts;
import nightwraid.diff.commands.GetCurrentKillsToLevel;
import nightwraid.diff.commands.GetDifficultyLevelCommand;
import nightwraid.diff.commands.GetLocalDifficultyLevelCommand;
import nightwraid.diff.commands.SetDebugModeCommand;
import nightwraid.diff.commands.SetDifficultyLevelCommand;
import nightwraid.diff.commands.SpawnMobWithModifierCommand;
import nightwraid.diff.events.EntityEvents;
import nightwraid.diff.events.PlayerEvents;
import nightwraid.diff.events.WorldEvents;
import nightwraid.diff.network.DifficultySyncPacket;
import nightwraid.diff.utils.PlayerDifficultyHelper;

@Mod(modid=DifficultyModConstants.MODID, name=DifficultyModConstants.MODNAME, version=DifficultyModConstants.VERSION, acceptedMinecraftVersions=DifficultyModConstants.ACCEPTED_MINECRAFT_VERSIONS)
public class DifficultyMod {
	@Instance
	public static DifficultyMod instance;
	
	public static org.apache.logging.log4j.Logger logger = null;
	public static PlayerDifficultyHelper pdh = new PlayerDifficultyHelper();	
	
	public static SimpleNetworkWrapper network;
	
	@SidedProxy(clientSide="nightwraid.diff.general.ClientProxy", serverSide="nightwraid.diff.general.ServerProxy", modId=DifficultyModConstants.MODID)
	public static IProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(DifficultyModConstants.NETWORK_CHANNEL);
		network.registerMessage(DifficultySyncPacket.DifficultySyncPacketHandler.class, DifficultySyncPacket.class, 9001, Side.SERVER);
		network.registerMessage(DifficultySyncPacket.DifficultySyncPacketHandler.class, DifficultySyncPacket.class, 9001, Side.CLIENT);
		
		CapabilityManager.INSTANCE.register(IDifficulty.class, new DifficultyStorage(), new DifficultyCapabilityFactory());
		
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(EntityEvents.class);
		MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
		MinecraftForge.EVENT_BUS.register(WorldEvents.class);
		
		MinecraftForge.EVENT_BUS.register(new DifficultyCapabilityHandler());

		proxy.preInit(event);
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
