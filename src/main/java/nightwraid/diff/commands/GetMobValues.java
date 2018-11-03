package nightwraid.diff.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import nightwraid.diff.effects.ApplyStats;
import nightwraid.diff.general.Utilities;

public class GetMobValues extends CommandBase {
	private final String COMMAND_NAME = "nwgetstats";
	private List<String> aliases;
	
	public GetMobValues() {
		aliases = new ArrayList();
		aliases.add("nwgs");
		aliases.add("gs");
	}

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/nwgs <Difficulty (optional)>";
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		try {
			int difficulty = 0;
			if (args.length == 1) {
				difficulty = Integer.parseInt(args[0]);
			} else {
				if (!(sender.getCommandSenderEntity() instanceof EntityPlayer)) {
					return;
				}
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
				difficulty = Utilities.GetDifficultyFromTags(player.getTags());
			}
			//Compute stats
			EntityZombie sampleZombie = new EntityZombie(sender.getEntityWorld());
			ApplyStats as = new ApplyStats((EntityLiving)sampleZombie, difficulty);
			
			double health = sampleZombie.getMaxHealth();
			double strength = sampleZombie.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
			double speed = sampleZombie.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
			sender.sendMessage(new TextComponentString("Difficulty: "+difficulty));
			sender.sendMessage(new TextComponentString("Health: "+health));
			sender.sendMessage(new TextComponentString("Strength: "+strength));
			sender.sendMessage(new TextComponentString("Speed: "+speed));
		} catch (Exception ex) {
			throw new CommandException("Failed to execute command. Something went wrong on the server");
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

}
