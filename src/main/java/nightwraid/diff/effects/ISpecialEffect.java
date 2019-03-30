package nightwraid.diff.effects;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionAbsorption;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public interface ISpecialEffect {
	public static Random rand = new Random();
	
	public default void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		//System.out.println("A modded entity has joined the world - EffectBase");
	}

	public default void OnEntityLivingDeath(LivingDeathEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivingDeath - EffectBase");
	}

	public default void OnEntityLivingSetAttackTarget(LivingSetAttackTargetEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivingSetAttackTarget - EffectBase");		
	}

	public default void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivingHurt - EffectBase");
	}

	public default void OnEntityLivingFall(LivingFallEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivingFall - EffectBase");
	}

	public default void OnEntityLivingJump(LivingJumpEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivingJump - EffectBase");
	}

	public default void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivinUpdate - EffectBase");
	}

	public default void OnEntityAttackEvent(LivingAttackEvent event, Integer diff) {
		//System.out.println("Modded Entity: LivinAttack - EffectBase");
	}
	
	public default void OnPlayerAttackedBy(LivingAttackEvent event, EntityLivingBase attacker, Integer diff) {
		
	}
	
	public String GetName();
	public int GetLevel();
	public void AbilityUnlockHandler(EntityPlayer player, int difficulty);
}
