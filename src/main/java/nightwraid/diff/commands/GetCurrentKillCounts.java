package nightwraid.diff.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import nightwraid.diff.general.DifficultyMod;

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
		for (EntityPlayer player:DifficultyMod.pdh.NormalEntitiesKilled.keySet()) {
			sender.sendMessage(new TextComponentString("§6["+player.getName()+"]§f has "+DifficultyMod.pdh.NormalEntitiesKilled.get(player)+" kills"));
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
