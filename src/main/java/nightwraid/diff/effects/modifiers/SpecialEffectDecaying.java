package nightwraid.diff.effects.modifiers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.EntityBulwarkSettings;
import nightwraid.diff.settings.EntityDecayingSettings;
import nightwraid.diff.settings.EntityHellspawnSettings;
import nightwraid.diff.utils.RGBA;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpecialEffectDecaying implements ISpecialEffect {
	public static String NAME = "Decaying";
	public static int UNLOCKED_AT = EntityDecayingSettings.levelDecayingEnabled;
	
	
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
					if (diff >= EntityDecayingSettings.levelThreeDecayEquipmentEnabled) {
						item.damageItem(EntityDecayingSettings.levelOneDecayEquipmentTick, attackingEntity);
					} else if (diff >= EntityDecayingSettings.levelTwoDecayEquipmentEnabled) {
						item.damageItem(EntityDecayingSettings.levelTwoDecayEquipmentEnabled, attackingEntity);
					} else if (diff >= EntityDecayingSettings.levelOneDecayEquipmentEnabled) {
						item.damageItem(EntityDecayingSettings.levelOneDecayEquipmentTick, attackingEntity);
					} 
				}
			}
		}
	}
	
	@Override
	public void OnPlayerAttackedBy(LivingAttackEvent event, EntityLivingBase attacker, Integer diff) {
		if (EntityDecayingSettings.witherDebuffOnHitEnabled <= diff) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			PotionEffect wither = new PotionEffect(Potion.getPotionById(20), (5 * EntityDecayingSettings.witherDebuffDuration));
			player.addPotionEffect(wither);
		}
	}
	

	@Override
	public String GetName() {
		return NAME;
	}

	@Override
	public int GetLevel() {
		return UNLOCKED_AT;
	}

	@Override
	public void AbilityUnlockHandler(EntityPlayer player, int difficulty) {
		if (difficulty == UNLOCKED_AT) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "can now be spawned! These types of mobs have an innate ability to degrade your equipment when attacked.");
		} else if (difficulty == EntityDecayingSettings.levelTwoDecayEquipmentEnabled || difficulty == EntityDecayingSettings.levelThreeDecayEquipmentEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "have gained enhanced decaying properties. Hitting a "+NAME+" will now degrade your equipment more than before!");
		} else if (difficulty == EntityDecayingSettings.witherDebuffOnHitEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "have gained the ability to wither their targets when they attack!");
		} 
	}
	
	@Override
	public Color GetHealthbarColor() {
		return Color.WHITE;
	}
	
	@Override
	public RGBA GetShaderColor() {
		return new RGBA(64f, 64f, 64f);
	}
}
