package nightwraid.diff.effects;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLiving;
import nightwraid.diff.utils.LogHelper;

public interface ISpecialAttackMob {
	public Map<EntityLiving, Integer> specialAttackCountdown = new HashMap<EntityLiving, Integer>();
	public Map<EntityLiving, Integer> specialAttackCooldown = new HashMap<EntityLiving, Integer>();
	
	public default void Count(EntityLiving entity, int warnTimer, int executeTimer, int cooldownTimer) {
		if (specialAttackCooldown.containsKey(entity)) {
			//Already on cool down
			int countdown = specialAttackCooldown.get(entity);
			countdown++;
			if (countdown == cooldownTimer) {
				specialAttackCooldown.remove(entity);
			} else {
				specialAttackCooldown.replace(entity, countdown);
			}
		} else {
			if (specialAttackCountdown.containsKey(entity)) {
				int countDown = specialAttackCountdown.get(entity);
				countDown++;
				if (countDown == warnTimer) {
					ExecuteWarning(entity);
				} else if (countDown == executeTimer) {
					ExecuteAttack(entity);
					countDown = 0;
					specialAttackCooldown.put(entity, 0);
				}
				specialAttackCountdown.replace(entity, countDown);
			} else {
				specialAttackCountdown.put(entity, 1);
			}
		}
	}
	
	public default void Count(EntityLiving entity, int warnTimer, int executeTimer) {
		if (specialAttackCountdown.containsKey(entity)) {
			int countDown = specialAttackCountdown.get(entity);
			countDown++;
			if (countDown == warnTimer) {
				ExecuteWarning(entity);
			} else if (countDown == executeTimer) {
				ExecuteAttack(entity);
				countDown = 0;
			}
			specialAttackCountdown.replace(entity, countDown);
		} else {
			specialAttackCountdown.put(entity, 1);
		}
	}
	
	public default void ResetCount(EntityLiving entity) {
		if (specialAttackCountdown.containsKey(entity)) {
			specialAttackCountdown.replace(entity, 0);
		}
	}
	
	public default void ResetCooldown(EntityLiving entity) {
		if (specialAttackCooldown.containsKey(entity)) {
			specialAttackCooldown.replace(entity, 0);
		}
	}
	
	public default void ClearCountersOfEntity(EntityLiving entity) {
		if (specialAttackCountdown.containsKey(entity)) {
			specialAttackCountdown.remove(entity);
		}
		if (specialAttackCooldown.containsKey(entity)) {
			specialAttackCooldown.remove(entity);
		}
	}
	
	public void ExecuteWarning(EntityLiving entity);
	public void ExecuteAttack(EntityLiving entity);
}
