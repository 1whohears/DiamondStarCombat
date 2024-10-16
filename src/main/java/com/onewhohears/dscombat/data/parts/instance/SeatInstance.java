package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.SeatStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SeatInstance<T extends SeatStats> extends PartInstance<T> {

	private boolean eject;

	public SeatInstance(T stats) {
		super(stats);
	}

	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		eject = tag.getBoolean("eject");
	}

	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putBoolean("eject", eject);
		return tag;
	}

	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		eject = buffer.readBoolean();
	}

	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeBoolean(eject);
	}

	public boolean canEject() {
		return eject;
	}

	public void setCanEject(boolean eject) {
		this.eject = eject;
	}

	@Override
	public void setFilled(String param) {
		if (param != null && param.contains("eject")) setCanEject(true);
	}

	@Override
	public void setParamNotFilled(String param) {
		if (param != null && param.contains("eject")) setCanEject(true);
	}

	@Override
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		super.addToolTips(tips, isAdvanced);
		if (canEject()) tips.add(UtilMCText.translatable("info.dscombat.can_eject").setStyle(Style.EMPTY.withColor(0x00CC00)));
		else tips.add(UtilMCText.translatable("info.dscombat.cant_eject").setStyle(Style.EMPTY.withColor(0xCC0000)));
	}
}
