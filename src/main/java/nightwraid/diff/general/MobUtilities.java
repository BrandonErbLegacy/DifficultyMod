package nightwraid.diff.general;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MobUtilities {
	public static List<EntityPlayer> GetNearbyPlayers(Entity entity, double range){
		World world = entity.world;
		List<EntityPlayer> nearbyPlayers = new ArrayList<EntityPlayer>();
		for (EntityPlayer player:world.playerEntities) {
			if (entity.getDistance(player) < range){
				nearbyPlayers.add(player);
			}
		}
		return nearbyPlayers;
	}
}
