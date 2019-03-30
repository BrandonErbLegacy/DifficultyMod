package nightwraid.diff.effects.stats;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import nightwraid.diff.effects.IStatModifier;
import nightwraid.diff.settings.EntitySettings;
import nightwraid.diff.utils.ModifierNames;

public class StatGainHP implements IStatModifier {

	@Override
	public float CalculateStat(float initialValue, int difficulty) {
		int workingDiff = difficulty - EntitySettings.mobsGainHPUnlocked;
		int cappedValue = (int) (initialValue * EntitySettings.mobsMaxHPGain);
		
		float valOne = workingDiff/2;
		double valTwo = Math.pow(1.008, workingDiff) * initialValue;
		if (cappedValue < (valOne+valTwo)) {
			return (cappedValue);
		} else {
			return ((int)(valOne+valTwo));
		}
	}

	@Override
	public void ApplyStat(EntityLivingBase entity, int difficulty) {
		//Do nothing if you haven't unlocked this modifier
		if (difficulty < EntitySettings.mobsGainHPUnlocked) {
			return;
		}
		IAttributeInstance maxHPAttribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		
		int maxHPCurrently = (int) entity.getMaxHealth();
		int currentHP = (int) entity.getHealth();
		
		int newMaxHP = (int) CalculateStat(maxHPCurrently, difficulty);
		int increaseAmt = (int) (newMaxHP - maxHPCurrently);
		
		AttributeModifier mod = new AttributeModifier(ModifierNames.MAX_HP_MODIFIER, increaseAmt, 0);
		maxHPAttribute.applyModifier(mod);
		
		//If we don't do this the mob only gains max hp, and remains at it's current health (e.g. 20/40 as an example)
		entity.setHealth(entity.getHealth() + increaseAmt);
	}
}
