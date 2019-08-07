package nightwraid.diff.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import nightwraid.diff.general.DifficultyMod;

public class PlayerEvents {
	@SubscribeEvent
	public static void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!DifficultyMod.pdh.NormalEntitiesKilled.containsKey(event.player)) {
			DifficultyMod.pdh.NormalEntitiesKilled.put(event.player, 0);
		} 
		if (!DifficultyMod.pdh.BossEntitiesKilled.containsKey(event.player)) {
			DifficultyMod.pdh.BossEntitiesKilled.put(event.player, 0);
		}
	}
	
	@SubscribeEvent
	public static void PlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
		if (!DifficultyMod.pdh.NormalEntitiesKilled.containsKey(event.player)) {
			DifficultyMod.pdh.NormalEntitiesKilled.remove(event.player);
		} 
		if (!DifficultyMod.pdh.BossEntitiesKilled.containsKey(event.player)) {
			DifficultyMod.pdh.BossEntitiesKilled.remove(event.player);
		}
	}
}
