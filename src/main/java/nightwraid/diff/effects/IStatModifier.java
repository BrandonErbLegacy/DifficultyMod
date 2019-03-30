package nightwraid.diff.effects;

import net.minecraft.entity.EntityLivingBase;

public interface IStatModifier {
	public float CalculateStat(float initialValue, int difficulty);
	public void ApplyStat(EntityLivingBase entity, int difficulty);
}
