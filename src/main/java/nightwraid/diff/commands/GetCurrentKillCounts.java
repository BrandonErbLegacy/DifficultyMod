package nightwraid.diff.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.utils.LogHelper;

public class GetCurrentKillCounts extends CommandBase  {
	private final String CMD_NAME = "getcurrentkillcounts";
	private String[] aliases = new String [] {
		"nwgetcurrentkillcounts",
		"nwgckc",
		"killcounts",
		"kc",
	};
	
	
	public String getUsage(ICommandSender sender) {
		return "/getcurrentkillcounts";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		for (EntityPlayer player:server.getPlayerList().getPlayers()) {
			IDifficulty diff = player.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			if (diff != null) {
				sender.sendMessage(new TextComponentString("\u00A76["+player.getName()+"]\u00A7f has "+diff.getMobsKilled()+" kills"));
			}
		}
	}	
	
	public String getName() {
		return CMD_NAME;
	}
	
	public List<String> getAliases(){
		return Arrays.asList(aliases);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
