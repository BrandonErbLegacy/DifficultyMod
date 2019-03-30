package nightwraid.diff.effects.stats;

import javax.swing.text.Utilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import nightwraid.diff.effects.IStatModifier;
import nightwraid.diff.settings.EntitySettings;

public class StatGainSPD implements IStatModifier {

	@Override
	public float CalculateStat(float initialValue, int difficulty) {
		double factor = difficulty/5;
		return (float) (initialValue * Math.pow(EntitySettings.mobsSpeedIncreaseFactor, factor));
	}

	@Override
	public void ApplyStat(EntityLivingBase entity, int difficulty) {
		if (difficulty < EntitySettings.mobsGainSpeedUnlocked) {
			return;
		} else {
			double moveSpeed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();

			double newSpeed = CalculateStat((float) moveSpeed, difficulty);
			
			entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
		}
	}

}
