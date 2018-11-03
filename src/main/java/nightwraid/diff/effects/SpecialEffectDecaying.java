package nightwraid.diff.effects;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import nightwraid.diff.general.DifficultyConfigEntities;

public class SpecialEffectDecaying implements ISpecialEffect {
	//Decay armor/weapons when attacking or taking damage
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource ds = event.getSource();
		if (ds.getTrueSource() != null && ds.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attackingEntity = (EntityLivingBase) ds.getTrueSource();
			Iterable<ItemStack> armorAndEquip = attackingEntity.getEquipmentAndArmor();
			for (ItemStack item:armorAndEquip) {
				if (item.isItemStackDamageable()) {
					item.damageItem(DifficultyConfigEntities.decayingEquipmentDamage, attackingEntity);
				}
			}
		}
	}
}
