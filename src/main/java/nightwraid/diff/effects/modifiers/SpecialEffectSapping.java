package nightwraid.diff.effects.modifiers;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import nightwraid.diff.effects.ISpecialAttackMob;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.EntitySappingSettings;
import nightwraid.diff.utils.LogHelper;
import nightwraid.diff.utils.PositionHelper;
import nightwraid.diff.utils.RGBA;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpecialEffectSapping implements ISpecialEffect, ISpecialAttackMob {
	public static String NAME = "Sapping";
	public static int UNLOCKED_AT = EntitySappingSettings.levelSappingEnabled;
	
	@Override
	public void OnEntityAttackEvent(LivingAttackEvent event, Integer diff) {
		float damageAmount = event.getAmount();
		Entity e = event.getSource().getTrueSource();
		if (e instanceof EntityLiving) {
			EntityLiving entity = (EntityLiving) e;
			float missingHP = entity.getMaxHealth() - entity.getHealth();
			if (missingHP != 0) {
				float amountToHeal = missingHP;
				if (missingHP > (damageAmount * EntitySappingSettings.normalAttackConversionRatio)) {
					amountToHeal = (float) (damageAmount * EntitySappingSettings.normalAttackConversionRatio);
				}
				entity.setHealth(entity.getHealth()+amountToHeal);
				LogHelper.LogInfo("Sapping mob healing: "+amountToHeal);
			}
		}
	}
	
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		Entity e = event.getEntity();
		if (e instanceof EntityLiving) {
			EntityLiving entity = (EntityLiving) e;
			float hpPercent = entity.getHealth()/entity.getMaxHealth();
			if (hpPercent <= EntitySappingSettings.drainWellTrigger) {
				Count(entity, EntitySappingSettings.drainWellWarnTime, EntitySappingSettings.drainWellActiveTime);
			}
		}
	}
	
	@Override
	public void OnEntityLivingDeath(LivingDeathEvent event, Integer diff) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityLiving) {
			EntityLiving e = (EntityLiving) entity;
			ClearCountersOfEntity(e);
		}
	}

	@Override
	public String GetName() {
		return NAME;
	}

	@Override
	public int GetLevel() {
		return UNLOCKED_AT;
	}

	@Override
	public void AbilityUnlockHandler(EntityPlayer player, int difficulty) {
		if (difficulty == EntitySappingSettings.levelSappingEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "can now be spawned! "+NAME+" mobs heal a percentage of the damage they do to you. Be careful to keep your distance!");
		} else if (difficulty == EntitySappingSettings.levelDrainWellEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "mobs have gained a new attack [Drain Well]. When a sapping mob is low on HP it will drain HP from nearby mobs, or players.");
		}
	}

	@Override
	public void ExecuteWarning(EntityLiving entity) {
		World world = entity.getEntityWorld();
		for (EntityPlayer player:world.playerEntities) {
			if (player.getDistance(entity) <= EntitySappingSettings.drainWellWarnDistance) {
				UnlockMessageHelper.SendGenericMessage(player, "A sapping mob is half way toward having drain well charged. Watch out!");
			}
		}
	}

	@Override
	public void ExecuteAttack(EntityLiving entity) {
		World world = entity.getEntityWorld();
		List<Entity> entitiesNear = PositionHelper.getEntitiesInBoundingBoxByEntity(entity, EntitySappingSettings.drainWellWarnDistance);
		double damageToDeal = entity.getMaxHealth() * EntitySappingSettings.drainWellMaxHPPercent;
		for (Entity ent:entitiesNear) {
			if (ent instanceof EntityLivingBase) {
				EntityLivingBase target = (EntityLivingBase) ent;
				if (target.getEntityWorld().isRemote){
					target.hurtTime = 50;
					target.performHurtAnimation();
				}

				if (target.getHealth() <= damageToDeal) {
					target.setHealth(0);
					target.setDead();
				} else {
					target.setHealth((float) (target.getHealth()-damageToDeal));
				}
				entity.setHealth((float) (entity.getHealth() + damageToDeal));
				
				if (target instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) target;
					UnlockMessageHelper.SendGenericMessage(player, "A sapping "+entity.getName()+" has drained health from you!");
				}
			}
		}
	}
	
	@Override
	public Color GetHealthbarColor() {
		return Color.YELLOW;
	}
	
	@Override
	public RGBA GetShaderColor() {
		return new RGBA(212f, 212f, 93f);
	}
}
