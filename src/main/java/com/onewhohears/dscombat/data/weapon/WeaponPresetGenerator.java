package com.onewhohears.dscombat.data.weapon;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.JsonPresetGenerator;
import com.onewhohears.dscombat.data.weapon.TrackMissileData.TargetType;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.data.DataGenerator;

public class WeaponPresetGenerator extends JsonPresetGenerator<WeaponData>{
	
	protected void registerBullets() {
		addPresetToGenerate(BulletData.Builder
				.bulletBuilder(DSCombatMod.MODID, "20mm")
				.setFireRate(1)
				.setInnacuracy(0.3f)
				.setCanShootOnGround(true)
				.setDamage(15f)
				.setSpeed(10f)
				.setExplosionRadius(0)
				.setExplosive(false)
				.setDestoryTerrain(false)
				.setCausesFire(false)
				.setMaxAmmo(500)
				.setMaxAge(40)
				.setItem(ModItems.BULLET.getId())
				.setEntityType(ModEntities.BULLET.getId())
				.setRackEntityType(ModEntities.XM12.getId())
				.setShootSound(ModSounds.BULLET_SHOOT_1.getId())
				.setCompatibleWeaponPart(ModItems.XM12.getId())
				.setCraftNum(64)
				.addIngredient("minecraft:copper_ingot", 8)
				.addIngredient("minecraft:gunpowder", 2)
				.build());
		addPresetToGenerate(BulletData.Builder
				.bulletBuilder(DSCombatMod.MODID, "50mmhe")
				.setFireRate(4)
				.setInnacuracy(0.1f)
				.setCanShootOnGround(true)
				.setDamage(30f)
				.setSpeed(10f)
				.setExplosionRadius(3)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(false)
				.setMaxAmmo(250)
				.setMaxAge(40)
				.setItem(ModItems.BULLET.getId())
				.setEntityType(ModEntities.BULLET.getId())
				.setRackEntityType(ModEntities.XM12.getId())
				.setShootSound(ModSounds.BULLET_SHOOT_1.getId())
				.setCompatibleWeaponPart(ModItems.XM12.getId())
				.setCraftNum(16)
				.addIngredient("minecraft:copper_ingot", 8)
				.addIngredient("minecraft:gunpowder", 4)
				.build());
		addPresetToGenerate(BulletData.Builder
				.bulletBuilder(DSCombatMod.MODID, "120mmhe")
				.setFireRate(40)
				.setInnacuracy(0.04f)
				.setCanShootOnGround(true)
				.setDamage(60f)
				.setSpeed(10f)
				.setExplosionRadius(6f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(false)
				.setMaxAmmo(4)
				.setMaxAge(40)
				.setItem(ModItems.BULLET.getId())
				.setEntityType(ModEntities.BULLET.getId())
				.setNoRack()
				.setShootSound(ModSounds.BULLET_SHOOT_1.getId())
				.setNoCompatible()
				.setCraftNum(1)
				.addIngredient("minecraft:copper_ingot", 16)
				.addIngredient("minecraft:gunpowder", 8)
				.build());
	}
	
	protected void registerAAMissiles() {
		addPresetToGenerate(MissileData.Builder
				.trackMissileBuilder(DSCombatMod.MODID, "aim120b")
				.setFireRate(50)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(50f)
				.setSpeed(4f)
				.setExplosionRadius(5f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(3)
				.setMaxAge(400)
				.setFuelTicks(250)
				.setTurnRadius(100f)
				.setAcceleration(0.05f)
				.setBleed(0.03f)
				.setFuseDistance(3f)
				.setFieldOfView(70f)
				.setTargetType(TargetType.AIR)
				.setItem(ModItems.TRACK_MISSILE.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 20)
				.addIngredient("minecraft:tnt", 3)
				.addIngredient("minecraft:coal_block", 5)
				.addIngredient("minecraft:amethyst_shard")
				.addIngredient("minecraft:intel_pentium")
				.build());
		addPresetToGenerate(MissileData.Builder
				.irMissileBuilder(DSCombatMod.MODID, "aim9p5")
				.setFireRate(20)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(40f)
				.setSpeed(2.5f)
				.setExplosionRadius(3f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(4)
				.setMaxAge(300)
				.setFuelTicks(300)
				.setTurnRadius(70f)
				.setAcceleration(0.04f)
				.setBleed(0.01f)
				.setFuseDistance(3f)
				.setFieldOfView(70f)
				.setFlareResistance(1f)
				.setItem(ModItems.IR_MISSILE.getId())
				.setEntityType(ModEntities.IR_MISSILE_1.getId())
				.setRackEntityType(ModEntities.LIGHT_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.LIGHT_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 16)
				.addIngredient("minecraft:tnt", 1)
				.addIngredient("minecraft:coal_block", 3)
				.addIngredient("minecraft:spider_eye", 2)
				.addIngredient("minecraft:ti83")
				.build());
		// FIXME 2.1 finish AA Missile presets
	}
	
	protected void registerAGMissiles() {
		// FIXME 2.2 finish AG Missile presets
	}
	
	@Override
	protected void registerPresets() {
		registerBullets();
		registerAAMissiles();
		registerAGMissiles();
	}	
	
	public WeaponPresetGenerator(DataGenerator output) {
		super(output, "weapons");
	}

	@Override
	public String getName() {
		return "Weapons: "+DSCombatMod.MODID;
	}
	
	
	
}
