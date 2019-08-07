package nightwraid.diff.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nightwraid.diff.utils.LogHelper;

public class WorldEvents {
	public static int counter = 0;
	@SubscribeEvent
	public void OnWorldTick(TickEvent.WorldTickEvent event) {
		LogHelper.LogInfo("Ticked on world");
	}
}
