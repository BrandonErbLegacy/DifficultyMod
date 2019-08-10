package nightwraid.diff.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.BossInfo.Color;
import nightwraid.diff.capabilities.DifficultyProvider;
import nightwraid.diff.capabilities.IDifficulty;

public class BossHealthbarTracker {
	public List<EntityPlayer> playersTracking;
	public BossInfoServer bossInfo;
	
	public BossHealthbarTracker(EntityLiving entity, EntityPlayer initialPlayer) {
		//Create the info server
		ITextComponent name = new TextComponentString(GetNameByModifiers(entity));
		BossInfoServer info = new BossInfoServer(name, Color.WHITE, BossInfo.Overlay.PROGRESS);
		info.setDarkenSky(true);
		this.bossInfo = info;
		
		playersTracking = new ArrayList<EntityPlayer>();
		playersTracking.add(initialPlayer);
	}
	
	public BossInfoServer GetServer() {
		return this.bossInfo;
	}
	
	public void AddTrackingPlayer(EntityPlayer player) {
		playersTracking.add(player);
	}
	
	public void RemoveTrackingPlayer(EntityPlayer player) {
		playersTracking.remove(player);
	}
	
	public int PlayerTrackingCount() {
		return playersTracking.size();
	}
	
	public String GetNameByModifiers(EntityLiving entity) {
		String nameString = "";
		IDifficulty diff = entity.getCapability(DifficultyProvider.DIFFICULTY_CAPABILITY, null);
		if (diff != null) {
			for(String mod:diff.getModifiers()) {
				nameString = nameString + mod + " ";
			}
		}
		nameString = nameString + entity.getName();
		return nameString;
	}
}
