package nightwraid.diff.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class DifficultyProvider implements ICapabilitySerializable<NBTBase>{
	@CapabilityInject(IDifficulty.class)
	public static final Capability<IDifficulty> DIFFICULTY_CAPABILITY = null;
	
	private IDifficulty instance = DIFFICULTY_CAPABILITY.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == DIFFICULTY_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == DIFFICULTY_CAPABILITY ? DIFFICULTY_CAPABILITY.<T> cast(this.instance): null; 
	}

	@Override
	public NBTBase serializeNBT() {
		return DIFFICULTY_CAPABILITY.getStorage().writeNBT(DIFFICULTY_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		DIFFICULTY_CAPABILITY.getStorage().readNBT(DIFFICULTY_CAPABILITY, this.instance, null, nbt);
	}
}


