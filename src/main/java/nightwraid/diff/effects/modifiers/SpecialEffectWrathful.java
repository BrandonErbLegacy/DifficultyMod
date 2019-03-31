package nightwraid.diff.effects.modifiers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.EntityRaidBossSettings;
import nightwraid.diff.settings.EntityWrathfulSettings;
import nightwraid.diff.utils.LogHelper;
import nightwraid.diff.utils.ModifierNames;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpecialEffectWrathful implements ISpecialEffect {
	public static String NAME = "Wrathful";
	public static int UNLOCKED_AT = EntityWrathfulSettings.wrathfulLevelEnabled;
	
	@Override
	public void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		//Init entity
		if (event.getEntity() instanceof EntityMob) {
			EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			IAttributeInstance strength = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			AttributeModifier newMod = new AttributeModifier(ModifierNames.WRATHFUL_MOD_DENOTATION, 0, 0);
			strength.applyModifier(newMod);
			
			IAttributeInstance speed = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			AttributeModifier newSpd = new AttributeModifier(ModifierNames.WRATHFUL_MOD_DENOTATION, 0, 0);
			strength.applyModifier(newSpd);
		}
	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityMob) {
			ChangeStrength(entity);
			ChangeSpeed(entity);
		}
	}
	
	public void ChangeStrength(EntityLivingBase entity) {
		IAttributeInstance strength = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		for(AttributeModifier mod:strength.getModifiers()) {
			if (mod.getName() == ModifierNames.WRATHFUL_MOD_DENOTATION) {
				double hpPercent = 1 - entity.getHealth()/entity.getMaxHealth();
				double strBoost = strength.getBaseValue() * hpPercent;
				if (strBoost != mod.getAmount()) {
					LogHelper.LogInfo("Increased strength by ("+hpPercent+"%) to "+strength.getAttributeValue()+" from "+strength.getBaseValue());
					strength.removeModifier(mod.getID());
					AttributeModifier newMod = new AttributeModifier(ModifierNames.WRATHFUL_MOD_DENOTATION, strBoost, 0);
					strength.applyModifier(newMod);
				}
			}
		}
	}
	
	public void ChangeSpeed(EntityLivingBase entity) {
		IAttributeInstance speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		for(AttributeModifier mod:speed.getModifiers()) {
			if (mod.getName() == ModifierNames.WRATHFUL_MOD_DENOTATION) {
				double hpPercent = 1 - entity.getHealth()/entity.getMaxHealth();
				double strBoost = speed.getBaseValue() * hpPercent;
				if (strBoost != mod.getAmount()) {
					LogHelper.LogInfo("Increased speed by ("+hpPercent+"%) to "+speed.getAttributeValue()+" from "+speed.getBaseValue());
					speed.removeModifier(mod.getID());
					AttributeModifier newMod = new AttributeModifier(ModifierNames.WRATHFUL_MOD_DENOTATION, strBoost, 0);
					speed.applyModifier(newMod);
				}
			}
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
		if (difficulty == EntityWrathfulSettings.wrathfulLevelEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "can now be spawned! "+NAME+" mobs gain increasing speed and strength the lower on HP they get.");
		}
	}
}
