package nightwraid.diff.utils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class PositionHelper {
	public static List<Entity> getEntitiesInBoundingBoxByEntity(EntityLiving entity, int range){
		World world = entity.getEntityWorld();
		Integer al = range;
		AxisAlignedBB bb = new AxisAlignedBB(entity.posX-al, entity.posY-al, entity.posZ-al,
				entity.posX+al, entity.posY+al, entity.posZ+al);
		List<Entity> ents = world.getEntitiesInAABBexcluding(entity, bb, null);
		return ents;
	}
}
