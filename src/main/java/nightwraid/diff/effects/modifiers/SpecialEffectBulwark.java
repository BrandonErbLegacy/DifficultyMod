package nightwraid.diff.effects.modifiers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import nightwraid.diff.effects.ISpecialEffect;
import nightwraid.diff.settings.EntityBulwarkSettings;
import nightwraid.diff.utils.LogHelper;
import nightwraid.diff.utils.RGBA;
import nightwraid.diff.utils.UnlockMessageHelper;

public class SpecialEffectBulwark implements ISpecialEffect {
	public static String NAME = "Bulwark";
	public static int UNLOCKED_AT = EntityBulwarkSettings.levelBulwarkEnabled;
	
	//Takes reduced damage -:)
	//Regenerates % hp -:)
	//Has extra knock back resistance -Not implemented
	//Has default absorption level - 10% of hp, rounded to whole
	@Override
	public void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		
		double absorbPercent = EntityBulwarkSettings.bulwarkAdditionalAbsorbPerDiff * (diff - EntityBulwarkSettings.levelBulwarkEnabled);
		if (absorbPercent > EntityBulwarkSettings.bulwarkAbsorptionCapped) {
			absorbPercent = EntityBulwarkSettings.bulwarkAbsorptionCapped;
		}
		int hpToAbsorb = (int) entity.getMaxHealth();
		entity.setAbsorptionAmount(hpToAbsorb);
	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();
		
		if (diff >= EntityBulwarkSettings.bulwarkResistOneshotUnlocked) {
			if (entity.getHealth() == entity.getMaxHealth()) {
				if (event.getAmount() >= entity.getHealth()) {
					if (Math.random() <= EntityBulwarkSettings.bulwarkResistOneshot) {
						DamageSource ds = event.getSource();
						Entity truesrc = ds.getTrueSource();
						if ((truesrc != null) && (truesrc instanceof EntityPlayer)) {
							EntityPlayer player = (EntityPlayer) truesrc;
							UnlockMessageHelper.SendGenericMessage(player, "The bulwark creature has resisted being one-shot");
						}
						event.setAmount(0);
						return;
					}
				}
			}
		}
		
		
		//Apply regeneration effect for 5s (if not undead)
		if (!entity.isEntityUndead()) {
			//Apply 5 seconds of regen
			PotionEffect regen = new PotionEffect(Potion.getPotionById(10), (5 * EntityBulwarkSettings.bulwarkRegenDuration));
			entity.addPotionEffect(regen);
		}

		//Reduce Damage
		double damageAmt = event.getAmount();
		DamageSource damageSrc = event.getSource();
		float newDamage = (float) (damageAmt* (1-EntityBulwarkSettings.bulwarkDamageReduction));
		LogHelper.LogInfo("Bulwark attacked, reducing damage from "+damageAmt+" to "+newDamage);
		event.setAmount(newDamage); //Reduce all damage by 15%;
		//50% Made a pig hard to kill with a diamond sword...
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
		if (difficulty == EntityBulwarkSettings.levelBulwarkEnabled) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "can now be spawned! These are difficult to kill creatures that take reduced damage, have basic regen abilities, and an amount of absorption based on their max HP");
		} else if (difficulty == EntityBulwarkSettings.bulwarkResistOneshotUnlocked) {
			UnlockMessageHelper.SendAbilityUnlockMessage(player, NAME, "have gained the ability to occasionally resist being one shot!");
		}
	}
	
	@Override
	public Color GetHealthbarColor() {
		return Color.PINK;
	}
	
	@Override
	public RGBA GetShaderColor() {
		return new RGBA(191f, 63f, 127f);
	}
}
