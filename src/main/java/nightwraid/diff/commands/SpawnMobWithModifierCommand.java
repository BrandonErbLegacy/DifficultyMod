package nightwraid.diff.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nightwraid.diff.effects.EffectManager;
import nightwraid.diff.settings.GeneralSettings;
import nightwraid.diff.utils.DifficultyCapabilityHelper;
import nightwraid.diff.utils.ModifierNames;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpawnMobWithModifierCommand extends CommandBase {
	private final String CMD_NAME = "spawnmodmob";
	private String[] aliases = new String [] {
		"nwspawnmodmob",
		"nwsmm",
		"smm",
	};
	
	
	public String getUsage(ICommandSender sender) {
		return "/nwspawnmodmob <MobType>";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String effectName = args[0];
		
		if (EffectManager.ValidateModifierType(effectName) == false) {
			UnlockMessageHelper.SendGenericMessage(sender, effectName+" is not a valid type. Check both the case of the word, and the spelling");
			return;
		}
		
		int diff = 200;
		for (EntityPlayer player:sender.getEntityWorld().playerEntities) {
			if (player.getName() == sender.getName()) {
				diff = DifficultyCapabilityHelper.GetEntityDifficulty(player);
			}
		}
		
		World world = sender.getEntityWorld();
		EntityZombie zombie = new EntityZombie(world);
		zombie.addTag(ModifierNames.MOB_MODDED_DENOTATION);
		zombie.addTag(ModifierNames.MOB_CAPABILITY_DENOTATION+effectName);
		zombie.addTag(ModifierNames.MOB_LEVEL_DENOTATION+diff);

		EntityPlayer player = world.getPlayerEntityByName(sender.getName());
		BlockPos pos = player.getPosition();
		zombie.setPosition(pos.getX(), pos.getY(), pos.getZ());
		
		world.spawnEntity(zombie);
		
		if (GeneralSettings.debugModeEnabled) {
			UnlockMessageHelper.SendGenericMessage(sender, "Spawned Zombie with the capability: "+effectName);
		}
		
	}
	
	public String getName() {
		return CMD_NAME;
	}
	
	public List<String> getAliases(){
		return Arrays.asList(aliases);
	}
}
