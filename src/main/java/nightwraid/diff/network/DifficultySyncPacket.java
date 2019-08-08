package nightwraid.diff.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.utils.ByteBufUtil;
import nightwraid.diff.utils.LogHelper;

public class DifficultySyncPacket implements IMessage {
	public int entityID;
	public List<String> modifiers;
	public int modCount;
	public int difficultyLevel;
	
	public DifficultySyncPacket() {
		modifiers = new ArrayList<String>();
	}
	
	public DifficultySyncPacket(EntityLivingBase entity, int difficulty, List<String> modifiers) {
		this.entityID = entity.getEntityId();
		this.difficultyLevel = difficulty;
		this.modifiers = modifiers;
		this.modCount = modifiers.size();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.difficultyLevel = buf.readInt();
		this.modCount = buf.readInt();
		
		int i = 0;
		while (i<modCount) {
			String mod = ByteBufUtils.readUTF8String(buf);
			i++;
			this.modifiers.add(mod);
		}
		
		//LogHelper.LogInfo("Mob ID Received: "+entityID);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(difficultyLevel);
		buf.writeInt(modCount);
		for (String mod:modifiers) {
			ByteBufUtils.writeUTF8String(buf, mod);
		}
		//LogHelper.LogInfo("Mob ID Sent: "+entityID);
	}
	
	public static class DifficultySyncPacketHandler implements IMessageHandler<DifficultySyncPacket, IMessage> {

		@Override
		public IMessage onMessage(DifficultySyncPacket message, MessageContext ctx) {
			//LogHelper.LogInfo("Received message: "+message.difficultyLevel);
			EntityPlayer player = DifficultyMod.proxy.GetPlayerByContext(ctx);
			
			//This message really only goes from server to client to sync capability data
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					//LogHelper.LogInfo("Looking up: "+message.entityID);
					//LogHelper.LogInfo("Player World Null? "+(player.world == null));
					
					//Replace this logic because getEntityByID seems to pretty much always return null?
					//Entity targetEntity = player.getEntityWorld().getEntityByID(message.entityID);
					Entity targetEntity = null;
					
					for (Entity ent:player.world.loadedEntityList) {
						if (ent.getEntityId() == message.entityID) {
							targetEntity = ent;
						}
						//LogHelper.LogInfo("Loaded entity: "+ent.getEntityId()+" - "+ent.getName());
					}
					
					//LogHelper.LogInfo("Target Entity Null? "+(targetEntity == null));
					IDifficulty diff = targetEntity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
					if (diff != null) {
						diff.setDifficulty(message.difficultyLevel);
						for (String modifier:message.modifiers) {
							diff.addModifier(modifier);
						}
					}
				});
			}
			//Doesn't need to generate a response
			return null;
		}
		
	}

}
