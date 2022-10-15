package com.onewhohears.dscombat.data;

import java.nio.charset.Charset;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundWeaponAmmoPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public abstract class WeaponData {
	
	private String id;
	private Vec3 pos;
	private int maxAge;
	private int currentAmmo;
	private int maxAmmo;
	private int fireRate;
	private int recoilTime;
	private String failedLaunchReason;
	
	public static enum WeaponType {
		BULLET,
		ROCKET,
		BOMB
	}
	
	protected WeaponData(String id, Vec3 pos, int maxAge, int maxAmmo, int fireRate) {
		this.id = id;
		this.pos = pos;
		this.maxAge = maxAge;
		this.maxAmmo = maxAmmo;
		this.fireRate = fireRate;
	}
	
	public WeaponData(CompoundTag tag) {
		String preset = tag.getString("preset");
		if (!preset.isEmpty()) {
			WeaponData data = WeaponPresets.getById(preset);
			if (data != null) tag.merge(data.write());
		}
		id = tag.getString("id");
		double x, y, z;
		x = tag.getDouble("posx");
		y = tag.getDouble("posy");
		z = tag.getDouble("posz");
		pos = new Vec3(x, y, z);
		maxAge = tag.getInt("maxAge");
		currentAmmo = tag.getInt("currentAmmo");
		maxAmmo = tag.getInt("maxAmmo");
		fireRate = tag.getInt("fireRate");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putString("id", getId());
		tag.putDouble("posx", getLaunchPos().x);
		tag.putDouble("posy", getLaunchPos().y);
		tag.putDouble("posz", getLaunchPos().z);
		tag.putInt("maxAge", maxAge);
		tag.putInt("currentAmmo", currentAmmo);
		tag.putInt("maxAmmo", maxAmmo);
		tag.putInt("fireRate", fireRate);
		return tag;
	}
	
	public WeaponData(FriendlyByteBuf buffer) {
		read(buffer);
	}
	
	public void read(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		int idLength = buffer.readInt();
		id = buffer.readCharSequence(idLength, Charset.defaultCharset()).toString();
		double x, y, z;
		x = buffer.readDouble();
		y = buffer.readDouble();
		z = buffer.readDouble();
		pos = new Vec3(x, y, z);
		maxAge = buffer.readInt();
		currentAmmo = buffer.readInt();
		maxAmmo = buffer.readInt();
		fireRate = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeInt(getId().length());
		buffer.writeCharSequence(getId(), Charset.defaultCharset());
		buffer.writeDouble(getLaunchPos().x);
		buffer.writeDouble(getLaunchPos().y);
		buffer.writeDouble(getLaunchPos().z);
		buffer.writeInt(maxAge);
		buffer.writeInt(currentAmmo);
		buffer.writeInt(maxAmmo);
		buffer.writeInt(fireRate);
	}
	
	public abstract WeaponType getType();
	
	public abstract EntityAbstractWeapon shoot(Level level, EntityAbstractAircraft vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ);
	
	public void updateClientAmmo(EntityAbstractAircraft vehicle) {
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
				new ClientBoundWeaponAmmoPacket(vehicle.getId(), this.getId(), this.getCurrentAmmo()));
	}
	
	protected void tick() {
		if (recoilTime > 1) --recoilTime;
	}
	
	/**
	 * called inside the shoot function
	 * @param ammoNum
	 * @return if this weapon can shoot
	 */
	public boolean checkAmmo(int ammoNum, Entity shooter) {
		if (shooter instanceof ServerPlayer p) {
			if (p.isCreative()) return true;
		}
		return currentAmmo >= ammoNum;
	}
	
	public boolean checkRecoil() {
		return recoilTime <= 1;
	}
	
	public String getId() {
		return id;
	}
	
	public Vec3 getLaunchPos() {
		return pos;
	}

	public void setLaunchPos(Vec3 pos) {
		this.pos = pos;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public int getCurrentAmmo() {
		return currentAmmo;
	}

	public void setCurrentAmmo(int currentAmmo) {
		if (currentAmmo < 0) currentAmmo = 0;
		if (currentAmmo > maxAmmo) currentAmmo = maxAmmo;
		this.currentAmmo = currentAmmo;
	}
	
	public void addAmmo(int num) {
		this.setCurrentAmmo(currentAmmo+num);
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}
	
	public void setMaxAmmo(int max) {
		maxAmmo = max;
	}

	public int getFireRate() {
		return fireRate;
	}

	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}
	
	public boolean isFailedLaunch() {
		return failedLaunchReason != null;
	}
	
	@Nullable
	public String getFailedLaunchReason() {
		return failedLaunchReason;
	}
	
	public void setLaunchSuccess(int ammoNum, Entity shooter) {
		failedLaunchReason = null;
		if (shooter instanceof ServerPlayer p) {
			if (p.isCreative()) ammoNum = 0;
		}
		this.addAmmo(-ammoNum);
		recoilTime = this.getFireRate();
	}
	
	public void setLaunchFail(String failedLaunchReason) {
		this.failedLaunchReason = failedLaunchReason;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof WeaponData w) return w.getId().equals(id);
		return false;
	}
	
}
