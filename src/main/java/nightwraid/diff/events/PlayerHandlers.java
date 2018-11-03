package nightwraid.diff.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import nightwraid.diff.general.DifficultyConfigGeneral;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.general.Settings;

public class PlayerHandlers {

	@SubscribeEvent
	public static void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!DifficultyMod.pdm.NormalEntitiesKilled.containsKey(event.player)) {
			DifficultyMod.pdm.NormalEntitiesKilled.put(event.player, 0);
		} 
		if (!DifficultyMod.pdm.BossEntitiesKilled.containsKey(event.player)) {
			DifficultyMod.pdm.BossEntitiesKilled.put(event.player, 0);
		}
	}
	
	@SubscribeEvent
	public static void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
		//ConfigManager.sync(Settings.MODID, Config.Type.INSTANCE);
	}
	
	//@SubscribeEvent
	//public static void PlayerDealtDamage(PlayerEvent.)
}
