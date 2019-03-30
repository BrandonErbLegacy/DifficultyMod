package nightwraid.diff.effects.modifiers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.EntityHellspawnSettings;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpecialEffectHellspawn implements ISpecialEffect {
	public static String NAME = "Hellspawn";
	public static int UNLOCKED_AT = EntityHellspawnSettings.levelHellspawnEnabled;
	
	protected static Map<EntityLivingBase, Integer> setAllAblazeCooldown = new HashMap<EntityLivingBase, Integer>();
	protected static Map<EntityLivingBase, Integer> throwFireballCooldown = new HashMap<EntityLivingBase, Integer>();
	
	@Override
	public void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		if (!setAllAblazeCooldown.containsKey(entity)) {
			//Start the cooldown at 1 second so these don't fire as soon as they spawn
			setAllAblazeCooldown.put(entity, 1);
		}
	}
	
	@Override
	public void OnEntityAttackEvent(LivingAttackEvent event, Integer diff) {
		if (diff >= EntityHellspawnSettings.levelHellspawnImmuneToFire) {
			if (event.getSource() != null) {
				if (event.getSource().isFireDamage()) {
					event.setCanceled(true);
					//No fire damage for this bitch
				}
			}
		}
	}
	
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		if (diff <= EntityHellspawnSettings.levelHellspawnSetAblazeSpecial) {
			EntityLiving entity = (EntityLiving) event.getEntityLiving();
			if (setAllAblazeCooldown.containsKey(entity)) {
				if (EntityHellspawnSettings.hellspawnPassiveCanUseAblaze == false) {
					if (!(entity instanceof EntityMob)) {
						return;
					}
				}
				Integer abCd = setAllAblazeCooldown.get(entity);
				if (abCd++ <= EntityHellspawnSettings.hellspawnSetAblazeCooldown) {
					setAllAblazeCooldown.replace(entity, abCd);
					if (abCd == EntityHellspawnSettings.hellspawnSetAblazeWarning) {
						World world = entity.world;
						for (EntityPlayer player:world.playerEntities) {
							if (player.getDistance(entity) <= EntityHellspawnSettings.hellspawnSetAblazeRange) {
								player.sendMessage(new TextComponentString("Hellspawn "+entity.getName()+" will set the world ablaze soon (range of "+EntityHellspawnSettings.hellspawnSetAblazeRange+"m)"));
							}
						}
					}
				} else {
					setAllAblazeCooldown.remove(entity);
				}
			} else {
				if (EntityHellspawnSettings.hellspawnPassiveCanUseAblaze == false) {
					if (!(entity instanceof EntityMob)) {
						return;
					}
				}
				setAllAblazeCooldown.put(entity, 0);
				SetAblazeAttack(entity, diff);
			}
		}
	}
	
	@Override
	public void OnEntityLivingDeath(LivingDeathEvent event, Integer diff) {
		Entity entity = event.getEntity();
		
		if (EntityHellspawnSettings.levelHellspawnExplode <= diff) {
			if (EntityHellspawnSettings.hellspawnPassiveCanExplode) {
				if (!(event.getEntity() instanceof EntityMob)) {
					return;
				}
			}
			World world = event.getEntity().getEntityWorld();
			BlockPos pos = event.getEntity().getPosition();
			world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 
					(float) EntityHellspawnSettings.hellspawnExplosionStrength, true);
		}
		
		if (setAllAblazeCooldown.containsKey(entity)) {
			setAllAblazeCooldown.remove(entity);
		}
		if (throwFireballCooldown.containsKey(entity)) {
			throwFireballCooldown.remove(entity);
		}
	}
	
	@Override
	public void OnPlayerAttackedBy(LivingAttackEvent event, EntityLivingBase attacker, Integer diff) {
		if (EntityHellspawnSettings.levelHellspawnFireAttacks <= diff) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			player.setFire(EntityHellspawnSettings.durationHellspawnFireAttack);
		}
	}
	
	public void SetAblazeAttack(EntityLivingBase entity, Integer diff) {
		if (EntityHellspawnSettings.hellspawnPassiveCanUseAblaze == false) {
			if (!(entity instanceof EntityMob)) {
				return;
			}
		}
		World world = entity.world;
		for (EntityPlayer player:world.playerEntities) {
			if (player.getDistance(entity) <= EntityHellspawnSettings.hellspawnSetAblazeRange) {
				player.sendMessage(new TextComponentString("Hellspawn "+entity.getName()+" has set you ablaze (range of "+EntityHellspawnSettings.hellspawnSetAblazeRange+"m)"));
				player.setFire(EntityHellspawnSettings.durationHellspawnFireAttack);
			}
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
		if (difficulty == UNLOCKED_AT) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "can now be spawned! These types of mobs are immune to fire based damages.");
		} else if (difficulty == EntityHellspawnSettings.levelHellspawnFireAttacks) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "have gained firey attacks. Being hit by a "+NAME+" will set you on fire for "+EntityHellspawnSettings.durationHellspawnFireAttack+" seconds");
		} else if (difficulty == EntityHellspawnSettings.levelHellspawnSetAblazeSpecial) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "have gained the ability to set all entities within "+EntityHellspawnSettings.hellspawnSetAblazeRange+"m on fire!");
		} else if (difficulty == EntityHellspawnSettings.levelHellspawnExplode) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "have gained the ability to explode when they are slain.");
		}
	}

}
