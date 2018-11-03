package nightwraid.diff.effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import nightwraid.diff.general.DifficultyConfigEntities;

public class SpecialEffectMystic implements ISpecialEffect {
	//Capable of teleporting you -:)
	//Capable of teleporting mobs to you -:)
	//Capable of teleporting away to regen -:)
	//Capable of blinding you
	
	protected static Random rand = new Random();
	protected static List<EntityLivingBase> regeningEntities = new ArrayList<EntityLivingBase>();
	protected static List<EntityLivingBase> hasUsedRetreat = new ArrayList<EntityLivingBase>();
	protected static List<EntityLivingBase> hasUsedSiege = new ArrayList<EntityLivingBase>();
	protected static Map<EntityLivingBase, Integer> siegeTimers = new HashMap<EntityLivingBase, Integer>();
	private static int maxNumberOfTPTries = 10; //Try to tp at most 10 times before failing
	
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();
		if (regeningEntities.contains(entity)) {
			if ((entity.getHealth()/entity.getMaxHealth()) >= DifficultyConfigEntities.mysticMobsReengagePercent) {
				if (entity.getAttackingEntity() != null) {
					teleportToEntity(entity, entity.getAttackingEntity());
				}
			}
		} else {
			if (entity instanceof EntityMob) {
				EntityMob mob = (EntityMob) entity;
			
				if (mob.getAttackTarget() != null) {
					if (!hasUsedSiege.contains(entity)) {
						if (siegeTimers.containsKey(entity)) {
							//Siege timer already ticking
							int currentTimer = siegeTimers.get(entity);
							if (currentTimer%10 == 0) {
								//System.out.println("Siege timer tick: "+currentTimer);
							}
							if (currentTimer++ >= DifficultyConfigEntities.mysticSiegeDelay) {
								//Perform a siege action
								RecruitHelp(entity, true, diff);
								siegeTimers.remove(entity);
								hasUsedSiege.add(entity);
							} else {
								if (currentTimer == DifficultyConfigEntities.mysticSiegeWarn) {
									if (mob.getAttackTarget() instanceof EntityPlayer) {
										EntityPlayer player = (EntityPlayer) mob.getAttackTarget();
										player.sendMessage(new TextComponentString("A mystic mob is targeting you and will attack soon if you don't attack it first"));
									}
								}
								siegeTimers.replace(entity, currentTimer);
							}
						} else {
							//Start siege timer
							siegeTimers.put(entity, 0);
						}
					}
				}
			} else {
				//Passive mobs can be reactive
				if (entity.getAttackingEntity() != null) {
					if (!hasUsedSiege.contains(entity)) {
						if (siegeTimers.containsKey(entity)) {
							//Siege timer already ticking
							int currentTimer = siegeTimers.get(entity);
							if (currentTimer%10 == 0) {
								//System.out.println("Siege timer tick: "+currentTimer);
							}
							if (currentTimer++ >= DifficultyConfigEntities.mysticSiegeDelay) {
								//Perform a siege action
								RecruitHelp(entity, true, diff);
								siegeTimers.remove(entity);
								hasUsedSiege.add(entity);
							} else {
								if (currentTimer == DifficultyConfigEntities.mysticSiegeWarn) {
									if (entity.getAttackingEntity() instanceof EntityPlayer) {
										EntityPlayer player = (EntityPlayer) entity.getAttackingEntity();
										player.sendMessage(new TextComponentString("A mystic mob is targeting you and will attack soon if you don't attack it first"));
									}
								}
								siegeTimers.replace(entity, currentTimer);
							}
						} else {
							//Start siege timer
							siegeTimers.put(entity, 0);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		//Apply regeneration effect for 5s (if not undead)
		EntityLivingBase entity = event.getEntityLiving();
		if ((entity.getHealth()/entity.getMaxHealth()) <= DifficultyConfigEntities.mysticMobsRetreatPercent) {
			AttemptToRunAndRegen(entity);
			RecruitHelp(entity, false, diff);
			hasUsedRetreat.add(entity);
		}
		//If we have siege timer, stop it.
		if (siegeTimers.containsKey(entity)) {
			siegeTimers.remove(entity);
		}
	}
	
	@Override
	public void OnEntityLivingDeath(LivingDeathEvent event, Integer diff) {
		if (regeningEntities.contains(event.getEntityLiving())) {
			regeningEntities.remove(event.getEntityLiving());
		}
		if (hasUsedRetreat.contains(event.getEntityLiving())){
			hasUsedRetreat.remove(event.getEntityLiving());
		}
		if (hasUsedSiege.contains(event.getEntityLiving())) {
			hasUsedSiege.remove(event.getEntityLiving());
		}
		if (siegeTimers.containsKey(event.getEntityLiving())) {
			siegeTimers.remove(event.getEntityLiving());
		}
	}
	
	protected void RecruitHelp(EntityLivingBase entity, boolean teleportToTarget, Integer diff) {
		if (!hasUsedRetreat.contains(entity)) {
			System.out.println("Recruiting help");
			World world = entity.getEntityWorld();
			Integer al = DifficultyConfigEntities.mysticMobsRecruitRange;
			AxisAlignedBB bb = new AxisAlignedBB(entity.posX-al, entity.posY-al, entity.posZ-al,
					entity.posX+al, entity.posY+al, entity.posZ+al);
			List<Entity> ents = world.getEntitiesInAABBexcluding(entity, bb, null);
			List<EntityLivingBase> recruitList = new ArrayList<EntityLivingBase>();
			int allowedToRecruit = (int) Math.round(diff/DifficultyConfigEntities.mysticMobsRecruitedPerDiff);
			if (allowedToRecruit < 1) {
				allowedToRecruit = 1;
			}
			for (Entity ent:ents) {
				if ((ent instanceof EntityLivingBase) && !(ent instanceof EntityPlayer)) {
					if (recruitList.size() < allowedToRecruit) {
						recruitList.add((EntityLivingBase) ent);
						System.out.println("Adding "+ent.getName()+" to recruits");
					} else {
						break;
					}
				}
			}
			if (entity instanceof EntityMob) {
				EntityMob mob = (EntityMob) entity;
				EntityLivingBase target = mob.getAttackTarget();
				if (target != null) {
					for (EntityLivingBase ent:recruitList) {
						if (teleportToEntity(ent, target) == false) {
							//Teleport thing on to player
							teleportTo(ent, target.posX, target.posY, target.posZ);
						}
						ent.attackEntityAsMob(target);
					}
					if (teleportToTarget) {
						teleportToEntity(entity, target);
					}
				}
			} else {
				EntityLivingBase target = entity.getAttackingEntity();
				if (target != null) {
					for (EntityLivingBase ent:recruitList) {
						if (teleportToEntity(ent, target) == false) {
							//Teleport thing on to player
							teleportTo(ent, target.posX, target.posY, target.posZ);
						}
						ent.attackEntityAsMob(target);
					}
					if (teleportToTarget) {
						teleportToEntity(entity, target);
					}
				}
			}
		}
	}
	
	protected void AttemptToRunAndRegen(EntityLivingBase entity) {
		if (!hasUsedRetreat.contains(entity)) {
			System.out.println("Retreat!");
			//Flee at around 30% hp
			teleportRandomly(entity);
			if (entity.isEntityUndead()) {
				return;
			}
			//Apply 5 seconds of regen
			PotionEffect regen = new PotionEffect(Potion.getPotionById(10), 100); //Value may be ticks
			entity.addPotionEffect(regen);
		}
	}
	
	
    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly(EntityLivingBase entity)
    {
    	int attempts = 0;
    	while (attempts < maxNumberOfTPTries) {
	        double d0 = entity.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
	        double d1 = entity.posY + (double)(this.rand.nextInt(64) - 32);
	        double d2 = entity.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
	        boolean result = this.teleportTo(entity, d0, d1, d2);
	        if (result) {
	        	return result;
	        }
	        attempts++;
    	}
    	return false;
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(EntityLivingBase entity, Entity p_70816_1_)
    {
    	int attempts = 0;
    	while (attempts < maxNumberOfTPTries) {
	        Vec3d vec3d = new Vec3d(entity.posX - p_70816_1_.posX, entity.getEntityBoundingBox().minY + (double)(entity.height / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), entity.posZ - p_70816_1_.posZ);
	        vec3d = vec3d.normalize();
	        double d0 = 16.0D;
	        double d1 = entity.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
	        double d2 = entity.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
	        double d3 = entity.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
	        boolean result =  this.teleportTo(entity, d1, d2, d3);
	        if (result) {
	        	return result;
	        }
	        attempts++;
    	}
    	return false;
    }

    /**
     * Teleport the enderman
     */
    private boolean teleportTo(EntityLivingBase entity, double x, double y, double z)
    {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entity, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = entity.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag)
        {
            entity.world.playSound((EntityPlayer)null, entity.prevPosX, entity.prevPosY, entity.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
            entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }
}
