package nightwraid.diff.effects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SpecialEffectWrathful implements ISpecialEffect {
	//Capable of striking with lightning.
	//Capable of creating small explosions on the player
	//Capable of reflecting some damage
	//Capable of knocking player back
	//Capable of applying wither
	
	@Override
	public void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		//Init entity
	}
	
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();

	}
	
	@Override
	public void OnEntityLivingHurt(LivingHurtEvent event, Integer diff) {
		EntityLivingBase entity = event.getEntityLiving();
		
	}
	
	public void NotifyPlayerOfIncomingSpecial(String special, EntityPlayer player) {
		
	}
	
	public void ChooseSpecial() {
		List<String> specials = new ArrayList<String>();
		specials.add("wither");
		specials.add("explode");
		specials.add("smite");
		specials.add("reflect");
	}
	
	public void DoSpecialAttackWither() {
		
	}
	public void DoSpecialAttackExplode() {
		
	}
	public void DoSpecialAttackSmite() {
		
	}
	public void DoSpecialAttackReflect() {
		
	}
}
