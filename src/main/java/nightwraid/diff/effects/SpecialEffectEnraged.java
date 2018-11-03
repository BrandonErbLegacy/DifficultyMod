package nightwraid.diff.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SpecialEffectEnraged implements ISpecialEffect {
	//Level 1 - Double Strength buff
	//Level 2 - Charge attack where it breaks through blocks?
	//Level 3 - Undying buff, where he must be killed twice
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();
		//EntityDragon d = new EntityDragon(entity.world);
	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();

	}
}
