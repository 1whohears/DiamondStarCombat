package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.MineDispenserStats;
import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MineDispenserInstance<T extends MineDispenserStats> extends PartInstance<T> {
	
	private int mines = 0;
	private EntityMine.MineType mineType = EntityMine.MineType.ANTI_PERSONNEL;
	
	public MineDispenserInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
		mines = getStats().getMaxMines();
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		mines = tag.getInt("mines");
		if (tag.contains("mineType")) {
			mineType = EntityMine.MineType.fromId(tag.getInt("mineType"));
		}
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putInt("mines", mines);
		tag.putInt("mineType", mineType.getId());
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		mines = buffer.readInt();
		mineType = EntityMine.MineType.fromId(buffer.readInt());
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeInt(mines);
		buffer.writeInt(mineType.getId());
	}
	
	public int addMines(int mines) {
		int max = getStats().getMaxMines();
		this.mines += mines;
		if (this.mines < 0) {
			int r = this.mines;
			this.mines = 0;
			return r;
		}
		else if (this.mines > max) {
			int r = this.mines - max;
			this.mines = max;
			return r;
		}
		setDirty();
		return 0;
	}
	
	public void setMines(int mines) {
		int max = getStats().getMaxMines();
		if (mines > max) mines = max;
		else if (mines < 0) mines = 0;
		this.mines = mines;
		setDirty();
	}
	
	public int getMines() {
		return mines;
	}
	
	public void setMineType(EntityMine.MineType type) {
		this.mineType = type;
		setDirty();
	}
	
	public EntityMine.MineType getMineType() {
		return mineType;
	}
	
	public boolean deployMine() {
		if (isDamaged()) return false;
		if (getParent() == null) return false;
		if (getMines() <= 0) return false;
		
		Level level = getParent().getWorld();
		Vec3 pos = getParent().position().add(getRelPos()).add(0, -0.5, 0);
		
		EntityMine mine = new EntityMine(level, pos, mineType);
		mine.setDeltaMovement(getParent().getDeltaMovement().multiply(0.5, 0, 0.5));
		level.addFreshEntity(mine);
		
		addMines(-1);
		return true;
	}

	@Override
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		super.addToolTips(tips, isAdvanced);
		tips.add(UtilMCText.translatable("info.dscombat.mines")
				.append(" "+getMines()+"/"+getStats().getMaxMines())
				.setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		tips.add(UtilMCText.translatable("info.dscombat.mine_type")
				.append(" "+mineType.name())
				.setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}
}
