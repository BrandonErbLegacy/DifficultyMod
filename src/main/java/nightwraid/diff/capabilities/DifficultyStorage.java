package nightwraid.diff.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class DifficultyStorage implements IStorage<IDifficulty> {
	@Override
	public NBTBase writeNBT(Capability<IDifficulty> capability, IDifficulty instance, EnumFacing side) {
		NBTTagList modList = new NBTTagList();
		for(String mod:instance.getModifiers()) {
			modList.appendTag(new NBTTagString(mod));
		}
		NBTTagInt diff = new NBTTagInt(instance.getDifficulty());
		NBTTagCompound container = new NBTTagCompound();
		container.setTag("TAG_LIST", modList);
		container.setTag("DIFFICULTY", diff);
		return container;
	}

	@Override
	public void readNBT(Capability<IDifficulty> capability, IDifficulty instance, EnumFacing side, NBTBase nbt) {
		// TODO Auto-generated method stub
		NBTTagCompound tag = (NBTTagCompound) nbt;
		int diff = ((NBTTagInt) tag.getTag("DIFFICULTY")).getInt();
		NBTTagList modifiers = ((NBTTagList) tag.getTag("TAG_LIST"));
		instance.setDifficulty(diff);
		//Now this is some ugly af code.
		modifiers.forEach((NBTBase mod) -> instance.addModifier((((NBTTagString)mod).getString())));
	}
}