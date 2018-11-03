package nightwraid.diff.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnSkeletonWithModCommand extends CommandBase {
	private final String COMMAND_NAME = "nwskele";
	private List<String> aliases;
		
	public SpawnSkeletonWithModCommand() {
		aliases = new ArrayList();
		aliases.add("nwsk");
	}

	public String getName() {
		return COMMAND_NAME;
	}

	public String getUsage(ICommandSender sender) {
		return "/nwskele";
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			String effectName = args[0];
			World world = sender.getEntityWorld();
			EntityZombie skele = new EntityZombie(world);
			skele.addTag("Modded");
			skele.addTag("ModCap:"+effectName);
			
			EntityPlayer player = world.getPlayerEntityByName(sender.getName());
			BlockPos pos = player.getPosition();
			skele.setPosition(pos.getX(), pos.getY(), pos.getZ());
			
			world.spawnEntity(skele);
			System.out.println("Spawned for type: "+effectName);
			
		} catch (Exception ex){
			throw new CommandException("Failed to execute command. Needs a class type");
		}
	}

}
