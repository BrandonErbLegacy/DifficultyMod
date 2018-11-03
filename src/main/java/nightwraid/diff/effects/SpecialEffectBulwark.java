package nightwraid.diff.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SpecialEffectBulwark implements ISpecialEffect {
	//Takes reduced damage -:)
	//Regenerates % hp -:)
	//Has extra knock back resistance -Not implemented
	//Has default absorption level - 10% of hp, rounded to whole
	@Override
	public void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		//Increase values
		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		Integer amt = (int) Math.round(entity.getMaxHealth()*.1);
		entity.setAbsorptionAmount(amt);
	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		//Apply regeneration effect for 5s (if not undead)
		EntityLivingBase entity = event.getEntityLiving();
		if (entity.isEntityUndead()) {
			return;
		}
		//Apply 5 seconds of regen
		PotionEffect regen = new PotionEffect(Potion.getPotionById(10), 40); //Value may be ticks
		entity.addPotionEffect(regen);
		//Reduce Damage
		double damageAmt = event.getAmount();
		DamageSource damageSrc = event.getSource();
		
		/*float maxHp = entity.getMaxHealth();
		System.out.println(maxHp);*/
		
		event.setAmount((float) (damageAmt*.15)); //Reduce all damage by 15%;
		//50% Made a pig hard to kill with a diamond sword...
	}
}
