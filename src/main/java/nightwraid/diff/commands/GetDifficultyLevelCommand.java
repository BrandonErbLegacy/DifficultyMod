package nightwraid.diff.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.utils.TagHelper;

public class GetDifficultyLevelCommand extends CommandBase {
	private final String CMD_NAME = "getdifficulty";
	private String[] aliases = new String [] {
		"nwgetdiff",
		"nwgetdifficulty",
		"gd",
		"nwgd",
	};
	
	
	public String getUsage(ICommandSender sender) {
		return "/getdifficulty <PlayerName (optional>";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Integer targetDifficulty = 0;
		
		String playerName = null;
		try {
			playerName = args[0];
		} catch (Exception ex) {
			playerName = sender.getName();
		}
		
		PlayerList players = server.getPlayerList();
		EntityPlayerMP player = players.getPlayerByUsername(playerName);
		Integer difficulty = DifficultyMod.pdh.GetPlayerDifficulty(player);
		
		sender.sendMessage(new TextComponentString(player.getName()+"'s difficulty is "+difficulty));
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
