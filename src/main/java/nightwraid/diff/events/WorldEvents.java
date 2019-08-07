package nightwraid.diff.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.utils.LogHelper;

public class WorldEvents {
	public static int counter = 0;
	@SubscribeEvent
	public static void OnWorldTick(TickEvent.WorldTickEvent event) {
		//LogHelper.LogInfo("Ticked on world");
	}
	
	//Used to persist a players difficulty level across deaths
	@SubscribeEvent
	public static void OnPlayerCloned(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			EntityPlayer oldPlayer = event.getOriginal();
			EntityPlayer newPlayer = event.getEntityPlayer();
			
			IDifficulty oldDiff = oldPlayer.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			IDifficulty newDiff = newPlayer.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			
			if (oldPlayer != null && newPlayer != null) {
				newDiff.setDifficulty(oldDiff.getDifficulty());
			}
		}
	}
}
