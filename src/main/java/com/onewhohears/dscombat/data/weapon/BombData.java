package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class BombData extends BulletData {
	
	/*public BombData(RegistryObject<EntityType<?>> entityType, ResourceLocation texture, RegistryObject<SoundEvent> shootSound, List<Ingredient> ingredients,
			String id, Vec3 launchPos, int maxAge, int maxAmmo, int fireRate, boolean canShootOnGround,
			float damage, double speed, float innacuracy, boolean explosive, boolean destroyTerrain, 
			boolean causesFire, double explosiveDamage, float explosionRadius) {
		super(entityType, shootSound, ingredients,
				id, launchPos, maxAge, maxAmmo, fireRate, canShootOnGround, damage, speed, innacuracy,
				explosive, destroyTerrain, causesFire, explosiveDamage, explosionRadius);
	}*/
	
	public BombData(CompoundTag tag) {
		super(tag);
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		return tag;
	}
	
	public BombData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BOMB;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		
		return list;
	}
}
