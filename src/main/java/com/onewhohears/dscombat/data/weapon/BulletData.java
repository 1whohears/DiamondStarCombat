package com.onewhohears.dscombat.data.weapon;

import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BulletData extends WeaponData {
	
	public static class Builder extends AbstractWeaponBuilders.BulletBuilder<Builder> {

		protected Builder(String namespace, String name, JsonPresetFactory<? extends BulletData> sup) {
			super(namespace, name, sup, WeaponType.BULLET);
		}
		
		public static Builder bulletBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new BulletData(key, json));
		}
		
	}
	
	private final float damage;
	private final double speed;
	private final boolean explosive;
	private final boolean destroyTerrain;
	private final boolean causesFire;
	private final float explosionRadius;
	private final float innacuracy;
	
	public BulletData(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.damage = json.get("damage").getAsFloat();
		this.speed = json.get("speed").getAsDouble();
		this.explosive = json.get("explosive").getAsBoolean();
		this.destroyTerrain = json.get("destroyTerrain").getAsBoolean();
		this.causesFire = json.get("causesFire").getAsBoolean();
		this.explosionRadius = json.get("explosionRadius").getAsFloat();
		this.innacuracy = json.get("innacuracy").getAsFloat();
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BULLET;
	}
	
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new EntityBullet(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction) {
		EntityBullet bullet = (EntityBullet) super.getShootEntity(level, owner, pos, direction);
		if (bullet == null) return null;
		bullet.setDeltaMovement(direction.scale(speed));
		return bullet;
	}
	
	@Override
	public void setDirection(EntityWeapon weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		Random r = new Random();
		pitch = pitch + (r.nextFloat()-0.5f) * 2f * innacuracy;
		yaw = yaw + (r.nextFloat()-0.5f) * 2f * innacuracy;
		weapon.setXRot(pitch);
		weapon.setYRot(yaw);
		direction.subtract(direction).add(UtilAngles.rotationToVector(yaw, pitch));
	}
	
	public float getDamage() {
		return damage;
	}
	
	public double getSpeed() {
		return speed;
	}

	public boolean isExplosive() {
		return explosive;
	}

	public boolean isDestroyTerrain() {
		return destroyTerrain;
	}

	public float getExplosionRadius() {
		return explosionRadius;
	}

	public boolean isCausesFire() {
		return causesFire;
	}

	public float getInnacuracy() {
		return innacuracy;
	}

	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new BulletData(getKey(), getJsonData());
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(new ComponentColor(Component.literal("Damage: ").append(getDamage()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Max Speed: ").append(getSpeed()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Innacuracy: ").append(getInnacuracy()+""), 0x040404));
		if (isExplosive()) {
			list.add(new ComponentColor(Component.literal("EXPLOSIVE"), 0xaa0000));
			list.add(new ComponentColor(Component.literal("Radius: ").append(getExplosionRadius()+""), 0x040404));
		}
		if (isCausesFire()) list.add(new ComponentColor(Component.literal("INCENDIARY"), 0xaa0000));
		return list;
	}

}
