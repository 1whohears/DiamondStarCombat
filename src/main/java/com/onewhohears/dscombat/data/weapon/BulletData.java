package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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
	private final int explodeNum;
	
	public BulletData(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.damage = json.get("damage").getAsFloat();
		this.speed = json.get("speed").getAsDouble();
		this.explosive = json.get("explosive").getAsBoolean();
		this.destroyTerrain = json.get("destroyTerrain").getAsBoolean();
		this.causesFire = json.get("causesFire").getAsBoolean();
		this.explosionRadius = json.get("explosionRadius").getAsFloat();
		this.innacuracy = json.get("innacuracy").getAsFloat();
		this.explodeNum = UtilParse.getIntSafe(json, "explodeNum", 1);
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
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		EntityBullet bullet = (EntityBullet) super.getShootEntity(params);
		if (bullet == null) return null;
		bullet.setDeltaMovement(bullet.getLookAngle().scale(speed));
		return bullet;
	}
	
	@Override
	public void setDirection(EntityWeapon weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		Random r = new Random();
		pitch = pitch + (r.nextFloat()-0.5f) * 2f * innacuracy;
		yaw = yaw + (r.nextFloat()-0.5f) * 2f * innacuracy;
		weapon.setXRot(pitch-changeLaunchPitch);
		weapon.setYRot(yaw);
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
	
	public int getExplodeNum() {
		return explodeNum;
	}
	
	@Override
	public double getMobTurretRange() {
		return Math.max(300, getSpeed() * getMaxAge());
	}

	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new BulletData(getKey(), getJsonData());
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		tips.add(UtilMCText.literal("Damage: ").append(getDamage()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (advanced) tips.add(UtilMCText.literal("Max Speed: ").append(getSpeed()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (isExplosive()) {
			tips.add(UtilMCText.literal("Explosion Radius: ")
				.append(getExplosionRadius()+"").setStyle(Style.EMPTY.withColor(0xAA0000)));
			if (advanced) tips.add(UtilMCText.literal("Explosions: ")
				.append(getExplodeNum()+"").setStyle(Style.EMPTY.withColor(0xAA0000)));
		}
		if (advanced) tips.add(UtilMCText.literal("Innacuracy: ").append(getInnacuracy()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (isCausesFire()) tips.add(UtilMCText.literal("INCENDIARY").setStyle(Style.EMPTY.withColor(0xAA0000)));
	}

	@Override
	public String getWeaponTypeCode() {
		String code = "S";
		if (isExplosive()) code += "E";
		if (isCausesFire()) code += "I";
		return code;
	}
	
	@Override
	public String getDefaultIconLocation() {
		if (isExplosive()) return MODID+":textures/ui/weapon_icons/he_bullet.png";
		return MODID+":textures/ui/weapon_icons/bullet.png";
	}
	
	@Override
	protected Vec3 getStartMove(EntityVehicle vehicle) {
		Vec3 move = vehicle.getLookAngle().scale(getSpeed());
		if (vehicle.isWeaponAngledDown() && canAngleDown())
			move = UtilAngles.rotateVector(move, Vector3f.XN.rotationDegrees(25f));
		return move;
	}

}
