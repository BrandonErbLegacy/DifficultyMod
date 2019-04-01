package nightwraid.diff.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;

public class UnlockMessageHelper {
	public static void SendGenericMessage(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(message));
	}
	public static void SendGenericMessage(ICommandSender player, String message) {
		player.sendMessage(new TextComponentString(message));
	}
	public static void WorldAnnouncement(World world, String message) {
		for (EntityPlayer player:world.playerEntities) {
			player.sendMessage(new TextComponentString(message));
		}
	}
	public static void SendAbilityUnlockMessage(EntityPlayer player, String mobName, String message) {
		player.sendMessage(new TextComponentString("\u00A76["+mobName+"]\u00A7f mobs "+message));
	}
}
