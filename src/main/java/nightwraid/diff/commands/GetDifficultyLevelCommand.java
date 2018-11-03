package nightwraid.diff.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import nightwraid.diff.general.Utilities;

public class GetDifficultyLevelCommand extends CommandBase {
	private final String COMMAND_NAME = "nwgetdiff";
	private List<String> aliases;
		
	public GetDifficultyLevelCommand() {
		aliases = new ArrayList();
		aliases.add("nwgd");
	}

	public String getName() {
		return COMMAND_NAME;
	}

	public String getUsage(ICommandSender sender) {
		return "/nwgetdiff <PlayerName(Optional)>";
	}

	public List<String> getAliases() {
		return aliases;
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
		Integer difficulty = Utilities.GetDifficultyFromTags(player.getTags());
		sender.sendMessage(new TextComponentString(player.getName()+"'s difficulty is "+difficulty));
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
