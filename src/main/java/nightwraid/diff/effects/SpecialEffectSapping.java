package nightwraid.diff.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import nightwraid.diff.general.DifficultyConfigEntities;

public class SpecialEffectSapping implements ISpecialEffect {
	//Level 1 - Special attack that adds slowing
	//Level 2 - Special attack that lowers attack speed
	//Level 3 - Special attack that freezes your movement (?)
	//Level 4 - Increased slowing
	//Level 5 - Special attack that lowers your strength
	//Level 6 - Special attack that saps your hp and heals some
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();

	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();

	}
}
