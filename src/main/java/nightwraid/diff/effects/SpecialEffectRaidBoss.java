package nightwraid.diff.effects;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SpecialEffectRaidBoss implements ISpecialEffect {
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {		
		//Spawns a new mob of the same type.
		EntityLivingBase entity = event.getEntityLiving();
		
		if (entity.getTags().contains("Spawnling")) {
			return;
		}
		
		if (entity.isDead == true) {
			return;
		}
		
		Class<? extends EntityLivingBase> entityClass = entity.getClass();
		
		World world = entity.getEntityWorld();

		Constructor<? extends EntityLivingBase> constructor = null;
		try {
			constructor = entityClass.getConstructor(World.class);
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Entity newEnt = null;
		try {
			newEnt = constructor.newInstance(world);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		newEnt.addTag("Spawnling");
		
		BlockPos position1 = entity.getPosition();
		newEnt.setPosition(position1.getX(), position1.getY(), position1.getZ());
		
		world.spawnEntity(newEnt);
		
		if (entity instanceof EntityMob) {
			EntityMob original = (EntityMob) entity;
			EntityMob baseForTarget = (EntityMob) newEnt;
			if (original.getAttackTarget() != null) {
				baseForTarget.attackEntityAsMob(original.getAttackTarget());
			}
		} else {
			EntityLivingBase baseForTarget = (EntityLivingBase) newEnt;
			if (entity.getAttackingEntity() != null) {
				baseForTarget.attackEntityAsMob(entity.getAttackingEntity());
			}
		}
	}
}
