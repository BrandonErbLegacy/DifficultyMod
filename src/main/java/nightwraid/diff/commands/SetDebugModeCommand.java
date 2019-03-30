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


//Defaults to OP only
public class SetDebugModeCommand extends CommandBase {
	private final String CMD_NAME = "setdebugmode";
	private String[] aliases = new String [] {
		"nwsetdebugmode",
		"nwsdm",
		"dm",
		"nwdm",
	};

	public String getName() {
		return CMD_NAME;
	}

	public String getUsage(ICommandSender sender) {
		return "/setdebugmode <true or false>";
	}
	
	public List<String> getAliases(){
		return Arrays.asList(aliases);
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1) {
			return;
		} else {
			String bool = args[0];
			if (bool == "true") {
				GeneralSettings.debugModeEnabled = true;
			} else {
				GeneralSettings.debugModeEnabled = false;
			}
		}
	}
}
