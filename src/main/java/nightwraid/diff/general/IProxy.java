package nightwraid.diff.general;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {
	@EventHandler
	public void preInit(FMLPreInitializationEvent event);
	
	public EntityPlayer GetPlayerByContext(MessageContext context);
}
