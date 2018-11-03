package nightwraid.diff.effects;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import nightwraid.diff.general.DifficultyConfigEntities;

public class SpecialEffectHellspawn implements ISpecialEffect {
	//Level 1 - Sets targets on fire with hits -:)
	//Level 2 - Immune to fire damage -:)
	//Level 3 - Leaves fire trail
	//Level 4 - Throws fireballs
	//Level 5 - Periodically says something in chat, and sets all ablaze
	//Level 6 - Explodes on death - Leaves firey crater? -:/
	protected static Map<EntityLivingBase, Integer> setAllAblazeCooldown = new HashMap<EntityLivingBase, Integer>();
	protected static Map<EntityLivingBase, Integer> throwFireballCooldown = new HashMap<EntityLivingBase, Integer>();
	
	@Override
	public void OnEntityJoinedWorld(EntityJoinWorldEvent event, Integer diff) {
		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		if (!setAllAblazeCooldown.containsKey(entity)) {
			//Start the cooldown at 1 second so these don't fire as soon as they spawn
			setAllAblazeCooldown.put(entity, 1);
		}
	}
	
	@Override
	public void OnEntityAttackEvent(LivingAttackEvent event, Integer diff) {
		if (event.getSource() != null) {
			if (event.getSource().isFireDamage()) {
				event.setCanceled(true);
				//No fire damage for this bitch
			}
		}
	}
	
	@Override
	public void OnEntityLivingUpdate(LivingUpdateEvent event, Integer diff) {
		EntityLiving entity = (EntityLiving) event.getEntityLiving();
		if (setAllAblazeCooldown.containsKey(entity)) {
			if (DifficultyConfigEntities.hellspawnPassiveCanUseAblaze == false) {
				if (!(entity instanceof EntityMob)) {
					return;
				}
			}
			Integer abCd = setAllAblazeCooldown.get(entity);
			if (abCd++ <= DifficultyConfigEntities.hellspawnAblazeCooldown) {
				setAllAblazeCooldown.replace(entity, abCd);
				if (abCd == DifficultyConfigEntities.hellspawnWarnAblazeTime) {
					World world = entity.world;
					for (EntityPlayer player:world.playerEntities) {
						if (player.getDistance(entity) <= DifficultyConfigEntities.hellspawnSetAblazeRange) {
							player.sendMessage(new TextComponentString("Hellspawn "+entity.getName()+" will set the world ablaze soon (range of "+DifficultyConfigEntities.hellspawnSetAblazeRange+"m)"));
						}
					}
				}
			} else {
				setAllAblazeCooldown.remove(entity);
			}
		} else {
			if (DifficultyConfigEntities.hellspawnPassiveCanUseAblaze == false) {
				if (!(entity instanceof EntityMob)) {
					return;
				}
			}
			setAllAblazeCooldown.put(entity, 0);
			SetAblazeAttack(entity, diff);
		}
		//Eventually light the block on fire.
		//Currently this lasts forever though. That can't stay that way
		//So disabled until further notice.
		/*
		if (entity.onGround) {
			World world = entity.getEntityWorld();
			
			double posX = entity.posX;
			double posY = entity.posY;
			double posZ = entity.posZ;
			
			//Block we're standing on - 
            int i = MathHelper.floor(posX);
            int j = MathHelper.floor(posY);
            int k = MathHelper.floor(posZ);
            for (int l = 0; l < 4; ++l)
            {
            	//Comes from snowman code
                i = MathHelper.floor(posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
                j = MathHelper.floor(posY);
                k = MathHelper.floor(posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);

                if (world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(world, blockpos))
                {
                	//IBlockState fireState = Blocks.FIRE.getDefaultState();
                	//fireState.
                	//Blocks.FIRE.state
                	
                    //world.setBlockState(blockpos, Blocks.FIRE.getDefaultState().withProperty("AGE", 15));
                }
            }
		}*/
	}
	
	public void FireballAttack(EntityLivingBase entity, Integer diff) {
		
	}
	
	public void SetAblazeAttack(EntityLivingBase entity, Integer diff) {
		if (DifficultyConfigEntities.hellspawnPassiveCanUseAblaze == false) {
			if (!(entity instanceof EntityMob)) {
				return;
			}
		}
		World world = entity.world;
		for (EntityPlayer player:world.playerEntities) {
			if (player.getDistance(entity) <= DifficultyConfigEntities.hellspawnSetAblazeRange) {
				player.sendMessage(new TextComponentString("Hellspawn "+entity.getName()+" has set you ablaze (range of "+DifficultyConfigEntities.hellspawnSetAblazeRange+"m)"));
				player.setFire(DifficultyConfigEntities.hellspawnSetAblazeTime);
			}
		}
	}
	
	@Override
	public void OnEntityLivingDeath(LivingDeathEvent event, Integer diff) {
		Entity entity = event.getEntity();
		World world = event.getEntity().getEntityWorld();
		BlockPos pos = event.getEntity().getPosition();
		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 
				(float) DifficultyConfigEntities.hellspawnExplosionStr, true);
		
		if (setAllAblazeCooldown.containsKey(entity)) {
			setAllAblazeCooldown.remove(entity);
		}
		if (throwFireballCooldown.containsKey(entity)) {
			throwFireballCooldown.remove(entity);
		}
	}
	
	@Override
	public void OnPlayerAttackedBy(LivingAttackEvent event, EntityLivingBase attacker, Integer diff) {
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		player.setFire(1); //1 second of fire
	}
}
