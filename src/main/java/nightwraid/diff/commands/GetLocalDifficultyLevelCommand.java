package nightwraid.diff.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.GeneralSettings;

public class GetLocalDifficultyLevelCommand extends CommandBase {
	private final String CMD_NAME = "getlocaldifficulty";
	private String[] aliases = new String [] {
		"nwlocaldiff",
		"nwgetlocaldiff",
		"ld",
		"nwld",
	};

	public String getName() {
		return CMD_NAME;
	}

	public String getUsage(ICommandSender sender) {
		return "/getlocaldifficulty";
	}
	
	public List<String> getAliases(){
		return Arrays.asList(aliases);
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			String username = sender.getName();
			World world = sender.getEntityWorld();
			EntityPlayer player = world.getPlayerEntityByName(username);
			String name = "";
			Integer maxDiff = DifficultyMod.pdh.GetPlayerDifficulty(player);
			if (player != null) {
				for (EntityPlayer worldPlayer:world.playerEntities) {
					if (worldPlayer.getDistance(player) < GeneralSettings.distanceDiffAffects) {
						maxDiff = DifficultyMod.pdh.GetPlayerDifficulty(worldPlayer);
						name = player.getName();
					}
				}
			}
			if (maxDiff < DifficultyMod.pdh.GetPlayerDifficulty(player)) {
				maxDiff = DifficultyMod.pdh.GetPlayerDifficulty(player);
				sender.sendMessage(new TextComponentString("The highest difficulty near you is you, at level "+maxDiff));
			} else {
				sender.sendMessage(new TextComponentString("The highest difficulty near you is "+name+", at level "+maxDiff));
			}
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
