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

public class GetCurrentKillsToLevel extends CommandBase {
	private final String CMD_NAME = "getkillstolevel";
	private String[] aliases = new String [] {
		"nwgetkillstolevel",
		"nwgktl",
		"killstolevel",
		"kl",
	};
	
	
	public String getUsage(ICommandSender sender) {
		return "/getkillstolevel";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(sender.getName());
		sender.sendMessage(new TextComponentString("\u00A76["+player.getName()+"]\u00A7f will require "+DifficultyMod.pdh.GetRequiredKillsPerLevel(player)+" kills to level up"));
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
