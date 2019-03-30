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

public class SetDifficultyLevelCommand extends CommandBase {
	private final String CMD_NAME = "setdifficulty";
	private String[] aliases = new String [] {
		"nwsetdiff",
		"nwsetdifficulty",
		"sd",
		"nwsd",
	};
	
	
	public String getUsage(ICommandSender sender) {
		return "/setdifficulty <NewDifficulty> <PlayerName (optional>";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Integer targetDifficulty = 0;
		try {
			targetDifficulty = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			throw new CommandException(CMD_NAME, "Requires a difficulty at a minimum.");
		}
		
		String playerName = null;
		try {
			playerName = args[1];
		} catch (Exception ex) {
			playerName = sender.getName();
		}
		
		PlayerList players = server.getPlayerList();
		EntityPlayerMP player = players.getPlayerByUsername(playerName);
		DifficultyMod.pdh.SetPlayerDifficulty(player, targetDifficulty);
		sender.sendMessage(new TextComponentString(player.getName()+"'s new difficulty is "+targetDifficulty));
	}
	
	public String getName() {
		return CMD_NAME;
	}
	
	public List<String> getAliases(){
		return Arrays.asList(aliases);
	}
}
