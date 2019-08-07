package nightwraid.diff.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nightwraid.diff.general.DifficultyModConstants;
import nightwraid.diff.utils.LogHelper;

public class DifficultyCapabilityHandler {
	public static final ResourceLocation DIFFICULTY_CAP = new ResourceLocation(DifficultyModConstants.MODID, "DIFFICULTY");
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	{
		event.addCapability(DIFFICULTY_CAP, new DifficultyProvider());
	}
}
