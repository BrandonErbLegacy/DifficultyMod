package nightwraid.diff.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import nightwraid.diff.effects.EffectManager;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.GeneralSettings;

public class PlayerDifficultyHelper {
	public Map<EntityLiving, List<EntityPlayer>> DamagedEntitiesToPlayers = new HashMap<EntityLiving, List<EntityPlayer>>();
	public Map<EntityPlayer, Integer> NormalEntitiesKilled = new HashMap<EntityPlayer, Integer>();
	public Map<EntityPlayer, Integer> BossEntitiesKilled = new HashMap<EntityPlayer, Integer>();
	
	//public Map<BossInfoServer, EntityPlayer> BossHealthbars = new HashMap<BossInfoServer, EntityPlayer>();
	
	public Map<EntityLiving, BossHealthbarTracker> BossHealthbars = new HashMap<EntityLiving, BossHealthbarTracker>();
	
	public PlayerDifficultyHelper() {
		
	}
	
	public void SetupMapsForPlayer(EntityPlayer player) {
		if (!NormalEntitiesKilled.containsKey(player)) {
			NormalEntitiesKilled.put(player, 0);
		}
		if (!BossEntitiesKilled.containsKey(player)) {
			BossEntitiesKilled.put(player, 0);
		}
	}
	
	
	public void IncrementPlayerDifficulty(EntityPlayer player, String DiffIncreaseReason) {
		int diff = GetPlayerDifficulty(player);
		diff++;
		SetPlayerDifficulty(player, diff);
		String message = "You have increased your difficulty level to: "+diff;
		if (DiffIncreaseReason.equals("normies")) {
			 message = "\u00A76[Level up!]\u00A7f You have been involved in \u00A7c"+GeneralSettings.playerNormalKillsDifficultyTick+"\u00A7f recent mob kills. Your difficulty has increased to: \u00A79"+GetPlayerDifficulty(player);
		} else if (DiffIncreaseReason.equals("bosses")) {
			 message = "\u00A76[Level up!]\u00A7f You have been involved in \u00A7c"+GeneralSettings.playerBossKillsDifficultyTick+"\u00A7f recent boss kills. Your difficulty has increased to: \u00A79"+GetPlayerDifficulty(player);
		}
		player.sendMessage(new TextComponentString(message));
		EffectManager.TriggerUnlockMessages(player, diff);
	}
	
	public void DecrementPlayerDifficulty(EntityPlayer player) {
		int diff = GetPlayerDifficulty(player);
		diff--;
		SetPlayerDifficulty(player, diff);
	}
	
	public void SetPlayerDifficulty(EntityPlayer player, Integer newDiff) {
		DifficultyCapabilityHelper.SetEntityDifficulty(player, newDiff);
	}
	
	public int GetPlayerDifficulty(EntityPlayer player) {
		return DifficultyCapabilityHelper.GetEntityDifficulty(player);
	}
	
	public void SetPlayerDamagedMob(EntityLiving entity, EntityPlayer player) {
		if (DamagedEntitiesToPlayers.containsKey(entity)) {
			List<EntityPlayer> list = DamagedEntitiesToPlayers.get(entity);
			if (!list.contains(player)) {
				list.add(player);
				DamagedEntitiesToPlayers.replace(entity, list);
			}
		} else {
			List<EntityPlayer> list = new ArrayList<>();
			list.add(player);
			DamagedEntitiesToPlayers.put(entity, list);
		}
	}
	public boolean EntityHasDifficultyChange(EntityLiving entity) {
		if (DamagedEntitiesToPlayers.containsKey(entity)) {
			return true;
		} else {
			return false;
		}
	}
	public void EntityDied(EntityLiving entity) {
		if (entity == null || DamagedEntitiesToPlayers.containsKey(entity) == false) {
			return;
		}
		List<EntityPlayer> list = DamagedEntitiesToPlayers.get(entity);
		for (EntityPlayer player:list) {
			//For Fake players and such?
			if (!(player instanceof EntityPlayer)) {
				continue;
			}
			if (entity.isNonBoss()) {
				if (GeneralSettings.allowDifficultyTickByNormal) {
					Integer killedCount = NormalEntitiesKilled.get(player);
					killedCount++;
					if (killedCount >= GetRequiredKillsPerLevel(player)) {
						IncrementPlayerDifficulty(player, "normies");
						killedCount = 0;
					}
					NormalEntitiesKilled.replace(player, killedCount);
				}
			} else {
				Integer killedCount = BossEntitiesKilled.get(player);
				killedCount++;
				if (killedCount >= GeneralSettings.playerBossKillsDifficultyTick) {
					IncrementPlayerDifficulty(player, "bosses");
					killedCount = 0;
				}
				BossEntitiesKilled.replace(player, killedCount);
			}
		}
		RemoveEntity(entity);
	}
	public void RemoveEntity(EntityLiving entity) {
		DamagedEntitiesToPlayers.remove(entity);
	}
	
	public static void WorldAnnouncement(World world, String message) {
		for (EntityPlayer player:world.playerEntities) {
			player.sendMessage(new TextComponentString(message));
		}
	}
	
	public static int GetRequiredKillsPerLevel(int currentDiff) {
		return (int) (GeneralSettings.playerNormalKillsDifficultyTick + Math.round(GeneralSettings.playerNormalKillIncreasePerTick * currentDiff));
	}
	
	public static int GetRequiredKillsPerLevel(EntityPlayer player) {
		return GetRequiredKillsPerLevel(DifficultyCapabilityHelper.GetEntityDifficulty(player));
	}

	public void RemoveHealthbarForEntity(EntityLiving entity) {
		if (BossHealthbars.containsKey(entity)) {
			BossHealthbars.remove(entity);
		}
	}
	
	public BossInfoServer GetHealthbarForEntity(EntityLiving entity) {
		BossHealthbarTracker tracker = BossHealthbars.get(entity);
		if (tracker == null) {
			return null;
		} else {
			return tracker.GetServer();
		}
	}
	
	public void AddPlayerToHealthbar(EntityLiving entity, EntityPlayer player) {
		BossHealthbarTracker tracker;
		if (BossHealthbars.containsKey(entity) == false) {
			tracker = new BossHealthbarTracker(entity, player);
			BossHealthbars.put(entity, tracker);
		} else {
			tracker = BossHealthbars.get(entity);
			tracker.AddTrackingPlayer(player);
		}
		if (player instanceof EntityPlayerMP) {
			tracker.GetServer().addPlayer((EntityPlayerMP) player);
		}
	}
	
	public void RemovePlayerFromHealthbar(EntityLiving entity, EntityPlayer player) {
		BossHealthbarTracker tracker;
		if (BossHealthbars.containsKey(entity) == false) {
			return;
		} else {
			tracker = BossHealthbars.get(entity);
			tracker.RemoveTrackingPlayer(player);
		}
		if (player instanceof EntityPlayerMP) {
			tracker.GetServer().removePlayer((EntityPlayerMP) player);
		}
		if (tracker.PlayerTrackingCount() == 0) {
			BossHealthbars.remove(entity);
		}
	}
}
