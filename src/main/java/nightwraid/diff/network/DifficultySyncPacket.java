package nightwraid.diff.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.utils.LogHelper;

public class DifficultySyncPacket implements IMessage {
	public int entityID;
	public List<String> modifiers;
	public int difficultyLevel;
	
	public DifficultySyncPacket() {
		
	}
	
	public DifficultySyncPacket(EntityLivingBase entity, int difficulty, List<String> modifiers) {
		this.entityID = entity.getEntityId();
		this.difficultyLevel = difficulty;
		this.modifiers = modifiers;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		this.entityID = buf.readInt();
		this.difficultyLevel = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		buf.writeInt(entityID);
		buf.writeInt(difficultyLevel);
	}
	
	public static class DifficultySyncPacketHandler implements IMessageHandler<DifficultySyncPacket, IMessage> {

		@Override
		public IMessage onMessage(DifficultySyncPacket message, MessageContext ctx) {
			LogHelper.LogInfo("Received message: "+message.difficultyLevel);
			EntityPlayer player = DifficultyMod.proxy.GetPlayerByContext(ctx);
			Entity targetEntity = player.world.getEntityByID(message.entityID);
			IDifficulty diff = targetEntity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			if (diff != null) {
				diff.setDifficulty(message.difficultyLevel);
				for (String modifier:message.modifiers) {
					diff.addModifier(modifier);
				}
			}
			//Doesn't need to generate a response
			return null;
		}
		
	}

}
