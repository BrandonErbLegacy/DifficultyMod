package nightwraid.diff.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nightwraid.diff.effects.EffectManager;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.general.DifficultyConfigGeneral;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.general.Utilities;

public class EntityHandlers {
	@SubscribeEvent
	public static void EntityJoinWorld(EntityJoinWorldEvent event) {
		try {
			Entity entity = event.getEntity();
			if (!(entity instanceof EntityLiving)) {
				//Nonliving entities don't need this
				return;
			}
			if (entity instanceof EntityPlayer) {
				//Players don't get buffs :)
				return;
			}
			
			Integer HighestDifficultyTick = 0;
			World world = event.getWorld();
			
			
			for (EntityPlayer player:world.playerEntities) {
				float distanceFromPlayer = entity.getDistance(player);
				//Determine if close enough to matter
				if (distanceFromPlayer <= DifficultyConfigGeneral.distanceDiffAffects) {
					int playerDiff = DifficultyMod.pdm.GetPlayerDifficulty(player);
					if (playerDiff > HighestDifficultyTick) {
						HighestDifficultyTick = playerDiff;
					}
				}
			}
			
			if (HighestDifficultyTick > 0) {
				//Only do this for mobs that haven't been touched already
				if (!entity.getTags().contains("Challenge")) {
					EffectManager.ApplyEffects(entity, HighestDifficultyTick);
					EffectManager.ApplyGear(entity, HighestDifficultyTick);
					EffectManager.ApplyMods(entity, HighestDifficultyTick);
				}
			}
			
			//Process special mob events
			if (entity.getTags().contains("Modded")) {
				Integer diff = EffectManager.GetDifficultyFromTags(entity.getTags());
				for (String tag:entity.getTags()) {
					if (tag.contains("ModCap")) {					
						ISpecialEffect effect = EffectManager.MatchTagToEffect(tag);
						if (effect != null) {
							effect.OnEntityJoinedWorld(event, diff);
						}
					}
				}
			}
		} catch (Exception ex) {
			DifficultyMod.logger.error("Failed to run onJoinWorldEvent on: "+event.getEntity().getName());
			DifficultyMod.logger.error("Reason: "+ex.getMessage());
		}
	}
	
	@SubscribeEvent
	public static void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			//Suck it up weenie
			return;
		}
		if (!(event.getEntity() instanceof EntityLiving)) {
			//Had to add this because for some reason an entity armor stand throws this, but is not a living entity??
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		//Process special mob events
		if (entity.getTags().contains("Modded")) {
			Integer diff = EffectManager.GetDifficultyFromTags(entity.getTags());
			for (String tag:entity.getTags()) {
				if (tag.contains("ModCap")) {
					ISpecialEffect effect = EffectManager.MatchTagToEffect(tag);
					if (effect != null) {
						try {
							effect.OnEntityLivingUpdate(event, diff);
						} catch (Exception ex) {
							DifficultyMod.logger.error("Failed to execute LivingUpdate for "+entity.getName()+": "+tag);
							DifficultyMod.logger.error("Reason: "+ex.getMessage());

						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void LivingEntityHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			//Suck it up weenie
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		DamageSource ds = event.getSource();
		Entity truesrc = ds.getTrueSource();
		if (truesrc != null) {
			if (truesrc instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) truesrc;
				DifficultyMod.pdm.SetPlayerDamagedMob(entity, player);
			}
		}
		
		//Process special mob events
		if (entity.getTags().contains("Modded")) {
			Integer diff = EffectManager.GetDifficultyFromTags(entity.getTags());
			for (String tag:entity.getTags()) {
				if (tag.contains("ModCap")) {
					ISpecialEffect effect = EffectManager.MatchTagToEffect(tag);
					if (effect != null) {
						try {
							effect.OnEntityLivingHurt(event, diff);
						} catch (Exception ex) {
							DifficultyMod.logger.error("Failed to execute EntityHurt for "+entity.getName()+": "+tag);
							DifficultyMod.logger.error("Reason: "+ex.getMessage());
						}
					}
				}
			}
		}
	}

	
	@SubscribeEvent
	public static void LivingEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			//Suck it up weenie
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();

		//Do onDeath events
		if (entity.getTags().contains("Modded")) {
			Integer diff = EffectManager.GetDifficultyFromTags(entity.getTags());
			for (String tag:entity.getTags()) {
				if (tag.contains("ModCap")) {
					ISpecialEffect effect = EffectManager.MatchTagToEffect(tag);
					if (effect != null) {
						try {
							effect.OnEntityLivingDeath(event, diff);
						} catch (Exception ex) {
							DifficultyMod.logger.error("Failed to execute EntityDeath for "+entity.getName()+": "+tag);
							DifficultyMod.logger.error("Reason: "+ex.getMessage());
						}
					}
				}
			}
		}
		
		//Do announcements if any
		if (entity.getTags().contains("Modded")) {
			if (DifficultyMod.pdm.DamagedEntitiesToPlayers.containsKey(entity) && event.isCanceled() == false) {
				World world = entity.getEntityWorld();
				String stringVersionTags = "";
				for (String tag:entity.getTags()) {
					if (tag.contains("ModCap:")) {
						String cut = tag.replace("ModCap:", "");
						if (stringVersionTags == "") {
							stringVersionTags = cut;
						} else {
							stringVersionTags = stringVersionTags + ", "+cut;
						}
					}
				}
				
				String players = "";
				int playerCount = DifficultyMod.pdm.DamagedEntitiesToPlayers.get(entity).size();
				if (playerCount == 1) {
					players = DifficultyMod.pdm.DamagedEntitiesToPlayers.get(entity).get(0).getName();
				} else {
					int count = 0;
					for (EntityPlayer player:DifficultyMod.pdm.DamagedEntitiesToPlayers.get(entity)) {
						count++;
						if (count == playerCount) {
							players = players+"and "+player.getName();
						} else {
							players = players+player.getName()+", ";
						}
					}
				}

				if (playerCount > 1) {
					Utilities.WorldAnnouncement(world, (players+" have slain a "+entity.getName()+" ("+stringVersionTags+") at difficulty "+EffectManager.GetDifficultyFromTags(entity.getTags())));
				} else {
					Utilities.WorldAnnouncement(world, (players+" has slain a "+entity.getName()+" ("+stringVersionTags+") at difficulty "+EffectManager.GetDifficultyFromTags(entity.getTags())));
				}
			}
		}
		if (event.isCanceled() == false) {
			if (DifficultyMod.pdm.EntityHasDifficultyChange(entity)) {
				DifficultyMod.pdm.EntityDied(entity);
			}
		}
	}
	
	@SubscribeEvent
	public static void LivingEntityAttackEvent(LivingAttackEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			DamageSource ds = event.getSource();
			//Tl;dr. Apply effects mobs would when the player is hit by one.
			if (ds != null) {
				if (ds.getTrueSource() != null && ds.getTrueSource() instanceof EntityLivingBase) {
					EntityLivingBase originator = (EntityLivingBase) ds.getTrueSource();
					if (originator.getTags().contains("Modded")) {
						Integer diff = EffectManager.GetDifficultyFromTags(originator.getTags());
						for (String tag:originator.getTags()) {
							if (tag.contains("ModCap")) {
								ISpecialEffect effect = EffectManager.MatchTagToEffect(tag);
								if (effect != null) {
									try {
										effect.OnPlayerAttackedBy(event, originator, diff);
									} catch (Exception ex) {
										DifficultyMod.logger.error("Failed to execute OnPlayerAttacked");
										DifficultyMod.logger.error("Reason: "+ex.getMessage());
									}
								}
							}
						}
					}
				}
			}
			return;
		}
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		//Process special mob events
		if (entity.getTags().contains("Modded")) {
			Integer diff = EffectManager.GetDifficultyFromTags(entity.getTags());
			for (String tag:entity.getTags()) {
				if (tag.contains("ModCap")) {
					ISpecialEffect effect = EffectManager.MatchTagToEffect(tag);
					if (effect != null) {
						try { 
							effect.OnEntityAttackEvent(event, diff);
						} catch (Exception ex) {
							DifficultyMod.logger.error("Failed to execute EntityAttacked for "+entity.getName()+": "+tag);
							DifficultyMod.logger.error("Reason: "+ex.getMessage());
						}
					}
				}
			}
		}
	}
	
	
	//@SubscribeEvent
	//public static void EntityAttacked(Living)
}
