package nightwraid.diff.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class PlayerDifficultyManager {
	public Map<EntityLiving, List<EntityPlayer>> DamagedEntitiesToPlayers = new HashMap<EntityLiving, List<EntityPlayer>>();
	public Map<EntityPlayer, Integer> NormalEntitiesKilled = new HashMap<EntityPlayer, Integer>();
	public Map<EntityPlayer, Integer> BossEntitiesKilled = new HashMap<EntityPlayer, Integer>();
	public PlayerDifficultyManager() {
		
	}
	
	public void AddIfNonePlayer(EntityPlayer player) {
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
		String message = "You have increased your difficulty level to: "+GetPlayerDifficulty(player);
		if (DiffIncreaseReason.equals("normies")) {
			 message = "You have been involved in "+DifficultyConfigGeneral.playerNormalKillsDifficultyTick+" recent mob kills. Your difficulty has increased to: "+GetPlayerDifficulty(player);
		} else if (DiffIncreaseReason.equals("bosses")) {
			message = "You have been involved in "+DifficultyConfigGeneral.playerBossKillsDifficultyTick+" recent boss kills. Your difficulty has increased to: "+GetPlayerDifficulty(player);
		}
		
		player.sendMessage(new TextComponentString(message));
		
		//System.out.println("Player "+player.getName()+" is receiving a new level");
	}
	
	public void DecrementPlayerDifficulty(EntityPlayer player) {
		int diff = GetPlayerDifficulty(player);
		diff--;
		SetPlayerDifficulty(player, diff);
	}
	
	public void SetPlayerDifficulty(EntityPlayer player, Integer newDiff) {
		Integer currentDiff = GetPlayerDifficulty(player);
		String tag = "Challenge:"+currentDiff;
		player.removeTag(tag);
		player.addTag("Challenge:"+newDiff);
	}
	
	public int GetPlayerDifficulty(EntityPlayer player) {
		Integer diff = Utilities.GetDifficultyFromTags(player.getTags());
		if (diff == null) {
			//Make a new 
			diff = DifficultyConfigGeneral.playerDefaultDifficultyTicks;
			player.addTag("Challenge:"+diff);
		}
		return diff;
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
		//System.out.print(entity.getName()+" has died"+"\n");
		List<EntityPlayer> list = DamagedEntitiesToPlayers.get(entity);
		for (EntityPlayer player:list) {
			if (entity.isNonBoss()) {
				if (DifficultyConfigGeneral.allowDifficultyTickByNormal) {
					Integer killedCount = NormalEntitiesKilled.get(player);
					killedCount++;
					if (killedCount >= DifficultyConfigGeneral.playerNormalKillsDifficultyTick) {
						IncrementPlayerDifficulty(player, "normies");
						killedCount = 0;
					}
					NormalEntitiesKilled.replace(player, killedCount);
				}
			} else {
				Integer killedCount = BossEntitiesKilled.get(player);
				killedCount++;
				if (killedCount >= DifficultyConfigGeneral.playerBossKillsDifficultyTick) {
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
}
