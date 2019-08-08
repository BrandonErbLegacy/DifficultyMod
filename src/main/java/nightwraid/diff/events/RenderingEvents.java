package nightwraid.diff.events;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;
import nightwraid.diff.utils.LogHelper;

public class RenderingEvents {
	@SubscribeEvent
	public static void EntityRenderPre(RenderLivingEvent.Pre event) {
		//LogHelper.LogInfo("PreRender event");
		Entity entity = event.getEntity();
		if (entity instanceof EntityLivingBase) {
			IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			//LogHelper.LogInfo("Capability is null: "+(diff == null));
			if (diff != null) {
				//LogHelper.LogInfo("Modifiers: "+diff.getModifiers().size() + ", Diff: "+diff.getDifficulty());
				if (diff.getModifiers().size() > 0) {
	                GlStateManager.pushMatrix();
	                GlStateManager.enableBlend();
	                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
	                GlStateManager.color(0.56f, 0.16f, 0.125f, 0.5f);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void EntityRenderPost(RenderLivingEvent.Post event) {
		Entity entity = event.getEntity();
		if (entity instanceof EntityLivingBase) {
			IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
			if (diff != null) {
				if (diff.getModifiers().size() > 0) {
	                GlStateManager.disableBlend();
	                GlStateManager.popMatrix();
				}
			}
		}
	}
}
