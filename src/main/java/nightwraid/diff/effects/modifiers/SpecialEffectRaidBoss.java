package nightwraid.diff.effects.modifiers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.EntityRaidBossSettings;
import nightwraid.diff.utils.RGBA;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpecialEffectRaidBoss implements ISpecialEffect {
	public static String NAME = "RaidBoss";
	public static int UNLOCKED_AT = EntityRaidBossSettings.levelRaidBossEnabled;
	public static String SPAWNLING_VALUE = "SPAWNLING";
	
	@Override
	public String GetName() {
		return NAME;
	}
	@Override
	public int GetLevel() {
		return UNLOCKED_AT;
	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {		
		//Spawns a new mob of the same type.
		EntityLivingBase entity = event.getEntityLiving();
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
		
		newEnt.addTag(SPAWNLING_VALUE);
		
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
	
	public static boolean SpawnedByRaidBoss(EntityLiving entity) {
		return entity.getTags().contains(SPAWNLING_VALUE);
	}
	
	@Override
	public void AbilityUnlockHandler(EntityPlayer player, int difficulty) {
		if (difficulty == EntityRaidBossSettings.levelRaidBossEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "can now be spawned! These mobs will clone themselves when they take damage. Be wary! Their clones have a chance at being modded too!");
		}
	}
	
	@Override
	public Color GetHealthbarColor() {
		return Color.BLUE;
	}
	
	@Override
	public RGBA GetShaderColor() {
		return new RGBA(93f, 93f, 212f);
	}
}
