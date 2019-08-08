package nightwraid.diff.network;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.utils.LogHelper;

public class DifficultySyncPacket implements IMessage {
	public int entityID;
	public List<String> modifiers;
	public int difficultyLevel;
	public String uniqueMobID;
	public int uniqueMobLength;
	
	public DifficultySyncPacket() {
		
	}
	
	public DifficultySyncPacket(EntityLivingBase entity, int difficulty, List<String> modifiers) {
		this.difficultyLevel = difficulty;
		this.modifiers = modifiers;
		this.uniqueMobID = entity.getUniqueID().toString();
		this.uniqueMobLength = uniqueMobID.getBytes().length;
		this.entityID = entity.getEntityId();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.difficultyLevel = buf.readInt();
		//this.uniqueMobLength = buf.readInt();
		//this.uniqueMobID = buf.readBytes(uniqueMobLength).toString(Charset.defaultCharset());
		LogHelper.LogInfo("Mob ID Received: "+entityID);
		//LogHelper.LogInfo("Unique Mob ID Received: "+uniqueMobID);
		//LogHelper.LogInfo("Length in bytes: "+uniqueMobLength);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(difficultyLevel);
		//buf.writeInt(uniqueMobLength);
		//buf.writeBytes(uniqueMobID.getBytes(Charset.defaultCharset()));
		LogHelper.LogInfo("Mob ID Sent: "+entityID);
		//LogHelper.LogInfo("Unique Mob ID Sent: "+uniqueMobID);
		//LogHelper.LogInfo("Length in bytes: "+uniqueMobLength);
	}
	
	public static class DifficultySyncPacketHandler implements IMessageHandler<DifficultySyncPacket, IMessage> {

		@Override
		public IMessage onMessage(DifficultySyncPacket message, MessageContext ctx) {
			LogHelper.LogInfo("Received message: "+message.difficultyLevel);
			EntityPlayer player = DifficultyMod.proxy.GetPlayerByContext(ctx);
						
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					LogHelper.LogInfo("Looking up: "+message.entityID);
					LogHelper.LogInfo("Player World Null? "+(player.world == null));
					Entity targetEntity = player.getEntityWorld().getEntityByID(message.entityID);
					
					for (Entity ent:player.world.loadedEntityList) {
						LogHelper.LogInfo("Loaded entity: "+ent.getEntityId()+" - "+ent.getName());
					}
					
					LogHelper.LogInfo("Target Entity Null? "+(targetEntity == null));
					IDifficulty diff = targetEntity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
					if (diff != null) {
						diff.setDifficulty(message.difficultyLevel);
						for (String modifier:message.modifiers) {
							diff.addModifier(modifier);
						}
					}
				});
			}
			
			/*EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			
			serverPlayer.getServerWorld().addScheduledTask(() -> {
				Entity targetEntity = player.world.getEntityByID(message.entityID);
				LogHelper.LogInfo("Target Entity Null? "+(targetEntity == null));
				IDifficulty diff = targetEntity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
				if (diff != null) {
					diff.setDifficulty(message.difficultyLevel);
					for (String modifier:message.modifiers) {
						diff.addModifier(modifier);
					}
				}
			});*/
			
			/*player.getServer().addScheduledTask(() -> {
				Entity targetEntity = player.world.getEntityByID(message.entityID);
				LogHelper.LogInfo("Target Entity Null? "+(targetEntity == null));
				IDifficulty diff = targetEntity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
				if (diff != null) {
					diff.setDifficulty(message.difficultyLevel);
					for (String modifier:message.modifiers) {
						diff.addModifier(modifier);
					}
				}
			});*/
			//Doesn't need to generate a response
			return null;
		}
		
	}

}
