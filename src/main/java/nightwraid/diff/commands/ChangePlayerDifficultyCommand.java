package nightwraid.diff.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import nightwraid.diff.general.DifficultyMod;

public class ChangePlayerDifficultyCommand extends CommandBase {

	private final String COMMAND_NAME = "nwsetplayerdiff";
	private List<String> aliases;
		
	public ChangePlayerDifficultyCommand() {
		aliases = new ArrayList();
		aliases.add("nwspd");
	}

	public String getName() {
		return COMMAND_NAME;
	}

	public String getUsage(ICommandSender sender) {
		return "/nwsetplayerdiff <NewDifficulty> <PlayerName(Optional)>";
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Integer targetDifficulty = 0;
		try {
			targetDifficulty = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			throw new CommandException(COMMAND_NAME, "Requires a difficulty at a minimum.");
		}
		
		String playerName = null;
		try {
			playerName = args[1];
		} catch (Exception ex) {
			playerName = sender.getName();
		}
		
		PlayerList players = server.getPlayerList();
		EntityPlayerMP player = players.getPlayerByUsername(playerName);
		DifficultyMod.pdm.SetPlayerDifficulty(player, targetDifficulty);

	}

}
