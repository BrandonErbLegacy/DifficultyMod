package nightwraid.diff.effects.stats;

import javax.swing.text.Utilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import nightwraid.diff.effects.IStatModifier;
import nightwraid.diff.settings.EntitySettings;
import nightwraid.diff.utils.ModifierNames;

public class StatGainSTR implements IStatModifier {

	@Override
	public float CalculateStat(float initialValue, int difficulty) {
		int cappedValue = (int) EntitySettings.mobsMaxSTRGain;
		
		float valOne = difficulty/5;
		double valTwo = Math.pow(1.005, difficulty) * initialValue;
		if (cappedValue < (valOne+valTwo)) {
			return initialValue + cappedValue;
		} else {
			return (int)(valOne+valTwo);
		}
	}

	@Override
	public void ApplyStat(EntityLivingBase entity, int difficulty) {
		if (difficulty >= EntitySettings.mobsGainStrengthUnlocked) {
			//Turns out some mobs have damage. Some don't?
			IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			if (attribute != null) {
				double baseDmg = attribute.getBaseValue();
				
				double newDmg = CalculateStat((float) baseDmg, difficulty);
				double dmgIncrease = newDmg - baseDmg;
				
				AttributeModifier mod = new AttributeModifier(ModifierNames.MAX_STR_MODIFIER, dmgIncrease, 0);
				attribute.applyModifier(mod);
			}
		}
	}
}
