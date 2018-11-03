package nightwraid.diff.effects;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import nightwraid.diff.general.DifficultyConfigEntities;
import nightwraid.diff.general.DifficultyConfigGeneral;
import nightwraid.diff.general.Utilities;

public class ApplyStats {
	EntityLiving entity;
	int diff;
	public ApplyStats(EntityLiving entity, int diff) {
		this.entity = entity;
		this.diff = diff;
		
		if (entity.getTags().contains("Modded")) {
			return;
		}
		
		BuffHealth();
		BuffSpeed();
		BuffDamage();
		BuffKnockbackResist();
		//BuffJump();
	}
	protected void BuffKnockbackResist() {
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		if (attribute != null) {
			double baseResist = attribute.getBaseValue();
			
			if (diff < DifficultyConfigGeneral.mobKnockbackTickThreshhold) {
				return;
			}
			
			double newResist = Utilities.ComputeModifierDiffFormula(diff, baseResist);
			double newIncrease = newResist - baseResist;
			
			if (newIncrease < DifficultyConfigGeneral.mobKnockbackMinimum) {
				newIncrease = DifficultyConfigGeneral.mobKnockbackMinimum;
			}
			
			AttributeModifier mod = new AttributeModifier("Diff_KnockbackResist", newIncrease, 0);
			
			attribute.applyModifier(mod);
			//System.out.println("Buffing "+entity.getName()+" to Knockback resist of "+(baseResist+newIncrease)+" ("+newIncrease+")");
		}
	}
	private void BuffSpeed() {
		double moveSpeed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();

		if (diff < DifficultyConfigGeneral.mobSpeedIncreaseTickThreshhold) {
			return;
		}
		
		double newSpeed = Utilities.ComputeModifierDiffFormula(diff, moveSpeed);
		
		entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);		
	}
	private void BuffHealth() {
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

		float maxHp = (int) entity.getMaxHealth();
		float currentHp = entity.getHealth();

		if (diff < DifficultyConfigGeneral.mobHealthIncreaseTickThreshhold) {
			return;
		}
		
		int newMaxHp = (int) Utilities.ComputeBaseDiffFormula(diff, maxHp);
		int actualIncreaseAmount = (int) (newMaxHp - maxHp);
		
		AttributeModifier mod = new AttributeModifier("Diff_MaxHP", actualIncreaseAmount, 0);
		
		attribute.applyModifier(mod);
		
		//Heal mob
		
		entity.setHealth(entity.getHealth()+actualIncreaseAmount);
		
		//System.out.println("Buffing "+entity.getName()+" to hp of "+entity.getMaxHealth()+" ("+actualIncreaseAmount+")");
		
	}
	private void BuffJump() {
		//entity.
	}
	private void BuffDamage() {
		//Turns out some mobs have damage. Some don't?
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		if (attribute != null) {
			double baseDmg = attribute.getBaseValue();
			
			double newDmg = Utilities.ComputeModifierDiffFormula(diff, baseDmg);
			double dmgIncrease = newDmg - baseDmg;
			
			AttributeModifier mod = new AttributeModifier("Diff_Strength", dmgIncrease, 0);
			
			attribute.applyModifier(mod);
			//System.out.println("Buffing "+entity.getName()+" to strength of "+(baseDmg+dmgIncrease)+" ("+dmgIncrease+")");
		}
	}
}
