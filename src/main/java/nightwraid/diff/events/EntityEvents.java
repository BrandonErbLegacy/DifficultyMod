package nightwraid.diff.events;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nightwraid.diff.effects.EffectManager;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.EntitySettings;
import nightwraid.diff.settings.GeneralSettings;
import nightwraid.diff.utils.LogHelper;
import nightwraid.diff.utils.TagHelper;
import nightwraid.diff.utils.UnlockMessageHelper;

public class EntityEvents {
	@SubscribeEvent
	public static void EntityJoinWorld(EntityJoinWorldEvent event) {
		try {
			Entity entity = event.getEntity();
			if (!(entity instanceof EntityLiving)) {
				// Nonliving entities don't need this
				return;
			}
			if (entity instanceof EntityPlayer) {
				// Players don't get buffs :)
				return;
			}

			Integer HighestDifficultyTick = 0;
			World world = event.getWorld();

			for (EntityPlayer player : world.playerEntities) {
				float distanceFromPlayer = entity.getDistance(player);
				// Determine if close enough to matter
				if (distanceFromPlayer <= GeneralSettings.distanceDiffAffects) {
					int playerDiff = DifficultyMod.pdh.GetPlayerDifficulty(player);
					if (playerDiff > HighestDifficultyTick) {
						HighestDifficultyTick = playerDiff;
					}
				}
			}

			//Mod mobs if anyone in the area has difficulty
			if (HighestDifficultyTick > 0) {
				// Only do this for mobs that haven't been touched already
				// without this check, our handler would be run any time the mob is reloaded into the world
				if (!TagHelper.MobHasBeenModded(entity)) {
					//Config setting for whether or not passive mobs can be modded. (Mobs, Animals, NPCs)
					if (EntitySettings.canModPassiveMobs) {
						if (entity instanceof IMob || entity instanceof IAnimals || entity instanceof INpc) {
							EffectManager.AmpUpMob((EntityLiving) entity, HighestDifficultyTick);
						}
					} else {
						if (entity instanceof IMob) {
							EffectManager.AmpUpMob((EntityLiving) entity, HighestDifficultyTick);
						}
					}
					List<ISpecialEffect> effects = TagHelper.GetSpecialEffectsFromEntity(entity);
					if (effects.size() > 0) {
						String name = "";
						for (ISpecialEffect effect:effects) {
							name = name + effect.GetName() + " ";
						}
						name = name + entity.getName();
						entity.setCustomNameTag(name);
					}
				}
			}

			//Special effects have been applied via tags for new mobs, and existing ones.
			//Retroactively apply the effects they need when spawned in
			if (TagHelper.MobHasBeenModded(entity)) {
				int diffSpawnedWith = TagHelper.GetDifficultyFromTags(entity.getTags());
				for (ISpecialEffect effect:TagHelper.GetSpecialEffectsFromEntity(entity)) {
					effect.OnEntityJoinedWorld(event, diffSpawnedWith);
				}
			}
		}
		catch (Exception ex) {
			if (GeneralSettings.debugModeEnabled) {
				DifficultyMod.logger.error("Failed to run onJoinWorldEvent on: " + event.getEntity().getName());
				DifficultyMod.logger.error("Reason: " + ex.getMessage());
				DifficultyMod.logger.error("Cause: " + ex.getCause());
				DifficultyMod.logger.debug("Failed to run onJoinWorldEvent for: " + event.getEntity().getName());
				ex.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent
	public static void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			// Suck it up weenie
			return;
		}
		if (!(event.getEntity() instanceof EntityLiving)) {
			// Had to add this because for some reason an entity armor stand throws this,
			// but is not a living entity??
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		
		if (TagHelper.MobHasBeenModded(entity)) {
			int diffSpawnedWith = TagHelper.GetDifficultyFromTags(entity.getTags());
			for (ISpecialEffect effect:TagHelper.GetSpecialEffectsFromEntity(entity)) {
				try {
					effect.OnEntityLivingUpdate(event, diffSpawnedWith);
				} catch (Exception ex) {
					LogHelper.LogError("LivingUpdateEvent error encountered for: "+entity.getName()+" with modifier "+effect.GetName(), ex);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void LivingEntityHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			// Suck it up weenie
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		DamageSource ds = event.getSource();
		Entity truesrc = ds.getTrueSource();
		if (truesrc != null) {
			if (truesrc instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) truesrc;
				DifficultyMod.pdh.SetPlayerDamagedMob(entity, player);
			}
		}
				
		if (TagHelper.MobHasBeenModded(entity)) {
			int diffSpawnedWith = TagHelper.GetDifficultyFromTags(entity.getTags());
			for (ISpecialEffect effect:TagHelper.GetSpecialEffectsFromEntity(entity)) {
				try {
					effect.OnEntityLivingHurt(event, diffSpawnedWith);
				} catch (Exception ex) {
					LogHelper.LogError("LivingHurtEvent Error encountered for: "+entity.getName()+" with modifier "+effect.GetName(), ex);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void LivingEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			// Suck it up weenie
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();

		if (TagHelper.MobHasBeenModded(entity)) {
			int diffSpawnedWith = TagHelper.GetDifficultyFromTags(entity.getTags());
			for (ISpecialEffect effect:TagHelper.GetSpecialEffectsFromEntity(entity)) {
				try {
					effect.OnEntityLivingDeath(event, diffSpawnedWith);
				} catch (Exception ex) {
					LogHelper.LogError("LivingEntityDeath error encountered for: "+entity.getName()+" with modifier "+effect.GetName(), ex);
				}
			}
		}

		// Do announcements if any
		if (TagHelper.MobHasBeenModded(entity)) {
			if (DifficultyMod.pdh.DamagedEntitiesToPlayers.containsKey(entity) && event.isCanceled() == false) {
				World world = entity.getEntityWorld();
				String stringVersionTags = "";
				
				List<ISpecialEffect> effects = TagHelper.GetSpecialEffectsFromEntity(entity);
				if (effects.size() > 0) {
					for (int i=0;i<effects.size();i++) {
						if (i == effects.size()-1) {
							stringVersionTags = stringVersionTags + effects.get(i).GetName();
						} else {
							stringVersionTags = stringVersionTags + effects.get(i).GetName() + ", ";
						}
					}
					
					
					String players = "";
					int playerCount = DifficultyMod.pdh.DamagedEntitiesToPlayers.get(entity).size();
					if (playerCount == 1) {
						players = DifficultyMod.pdh.DamagedEntitiesToPlayers.get(entity).get(0).getName();
					} else {
						int count = 0;
						for (EntityPlayer player : DifficultyMod.pdh.DamagedEntitiesToPlayers.get(entity)) {
							count++;
							if (count == playerCount) {
								players = players + "and " + player.getName();
							} else {
								players = players + player.getName() + ", ";
							}
						}
					}
	
					if (playerCount > 1) {
						UnlockMessageHelper.WorldAnnouncement(world,
								(players + " have slain a " + entity.getName() + " (" + stringVersionTags
										+ ") at difficulty " + TagHelper.GetDifficultyFromTags(entity.getTags())));
					} else {
						UnlockMessageHelper.WorldAnnouncement(world,
								(players + " has slain a " + entity.getName() + " (" + stringVersionTags
										+ ") at difficulty " + TagHelper.GetDifficultyFromTags(entity.getTags())));
					}
					}

				// Spawn xp for killing the mob
				EntityXPOrb xp = new EntityXPOrb(entity.world);
				xp.xpValue = TagHelper.GetDifficultyFromTags(entity.getTags())
						* TagHelper.GetDifficultyFromTags(entity.getTags()) + 50;
				xp.setPosition(entity.posX, entity.posY, entity.posZ);
				entity.world.spawnEntity(xp);
			}
		}
		if (event.isCanceled() == false) {
			if (DifficultyMod.pdh.EntityHasDifficultyChange(entity)) {
				try {
					DifficultyMod.pdh.EntityDied(entity);
				} catch(Exception ex) {
					//Prevent memory leak by always cleaning this out and ensuring relevancy
					if (DifficultyMod.pdh.DamagedEntitiesToPlayers.containsKey(entity)) {
						DifficultyMod.pdh.DamagedEntitiesToPlayers.remove(entity);
					}
					LogHelper.LogError("LivingEntityDeath error encountered for: "+entity.getName(), ex);
				}
			}
		}
	}

	@SubscribeEvent
	public static void LivingEntityAttackEvent(LivingAttackEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			DamageSource ds = event.getSource();
			// If the target is a player, then apply any harmful effects that a mob with it's modifiers has (Like setting you on fire (Hellspawn))
			if (ds != null) {
				if (ds.getTrueSource() != null && ds.getTrueSource() instanceof EntityLivingBase) {
					if (ds.getTrueSource() instanceof EntityPlayer) {
						//Player attacking player.
						return;
					}
					EntityLivingBase originator = (EntityLivingBase) ds.getTrueSource();
					if (TagHelper.MobHasBeenModded(originator)) {
						int diffSpawnedWith = TagHelper.GetDifficultyFromTags(originator.getTags());
						for (ISpecialEffect effect:TagHelper.GetSpecialEffectsFromEntity(originator)) {
							try {
								effect.OnPlayerAttackedBy(event, originator, diffSpawnedWith);
							} catch (Exception ex) {
								LogHelper.LogError("OnPlayerAttackedBy error encountered for: "+originator.getName()+" with modifier "+effect.GetName(), ex);
							}
						}
					}
				}
			}
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		if (TagHelper.MobHasBeenModded(entity)) {
			int diffSpawnedWith = TagHelper.GetDifficultyFromTags(entity.getTags());
			for (ISpecialEffect effect:TagHelper.GetSpecialEffectsFromEntity(entity)) {
				try {
					effect.OnEntityAttackEvent(event, diffSpawnedWith);
				} catch (Exception ex) {
					LogHelper.LogError("LivingEntityAttackEvent error encountered for: "+entity.getName()+" with modifier "+effect.GetName(), ex);
				}
			}
		}
	}
}
