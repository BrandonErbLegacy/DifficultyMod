package nightwraid.diff.general;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nightwraid.diff.events.RenderingEvents;

public class ClientProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(RenderingEvents.class);
	}

	@Override
	public EntityPlayer GetPlayerByContext(MessageContext context) {
		return Minecraft.getMinecraft().player;
	}
}
