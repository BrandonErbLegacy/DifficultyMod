package nightwraid.diff.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import nightwraid.diff.general.DifficultyConfigGeneral;
import nightwraid.diff.general.DifficultyMod;

public class GetLocalDifficulty extends CommandBase {
	private final String COMMAND_NAME = "nwlocaldiff";
	private List<String> aliases;

	public GetLocalDifficulty() {
		aliases = new ArrayList();
		aliases.add("nwgld");
		aliases.add("getlocaldiff");
		aliases.add("ld");
	}

	public String getName() {
		return COMMAND_NAME;
	}

	public String getUsage(ICommandSender sender) {
		return "/nwlocaldiff";
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			String username = sender.getName();
			World world = sender.getEntityWorld();
			EntityPlayer player = world.getPlayerEntityByName(username);
			Integer maxDiff = DifficultyMod.pdm.GetPlayerDifficulty(player);
			if (player != null) {
				for (EntityPlayer worldPlayer:world.playerEntities) {
					if (worldPlayer.getDistance(player) < DifficultyConfigGeneral.distanceDiffAffects) {
						maxDiff = DifficultyMod.pdm.GetPlayerDifficulty(worldPlayer);
					}
				}
			}
			sender.sendMessage(new TextComponentString("The highest difficulty near you is: "+maxDiff));
		} catch (Exception ex) {
			throw new CommandException("An error was encountered while handling this");
		}
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
