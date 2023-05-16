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
				.setItem(ModItems.B_20MM.getId())
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
				.setItem(ModItems.B_50MMHE.getId())
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
				.setItem(ModItems.B_120MMHE.getId())
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
				.trackMissileBuilder(DSCombatMod.MODID, "aim7f")
				.setFireRate(80)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(80f)
				.setSpeed(3.5f)
				.setExplosionRadius(7f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(2)
				.setMaxAge(600)
				.setFuelTicks(450)
				.setTurnRadius(140f)
				.setAcceleration(0.06f)
				.setBleed(0.05f)
				.setFuseDistance(4f)
				.setFieldOfView(40f)
				.setTargetType(TargetType.AIR)
				.setItem(ModItems.AIM7F.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 24)
				.addIngredient("minecraft:tnt", 5)
				.addIngredient("minecraft:coal_block", 8)
				.addIngredient("dscombat:ti83")
				.build());
		addPresetToGenerate(MissileData.Builder
				.trackMissileBuilder(DSCombatMod.MODID, "aim7mh")
				.setFireRate(80)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(80f)
				.setSpeed(3.5f)
				.setExplosionRadius(8.5f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(2)
				.setMaxAge(600)
				.setFuelTicks(450)
				.setTurnRadius(130f)
				.setAcceleration(0.06f)
				.setBleed(0.05f)
				.setFuseDistance(4f)
				.setFieldOfView(50f)
				.setTargetType(TargetType.AIR)
				.setItem(ModItems.AIM7MH.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 24)
				.addIngredient("minecraft:tnt", 6)
				.addIngredient("minecraft:coal_block", 8)
				.addIngredient("dscombat:ti83", 2)
				.build());
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
				.setTurnRadius(90f)
				.setAcceleration(0.05f)
				.setBleed(0.03f)
				.setFuseDistance(3f)
				.setFieldOfView(70f)
				.setTargetType(TargetType.AIR)
				.setItem(ModItems.AIM120B.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 20)
				.addIngredient("minecraft:tnt", 3)
				.addIngredient("minecraft:coal_block", 5)
				.addIngredient("minecraft:amethyst_shard")
				.addIngredient("dscombat:intel_pentium")
				.build());
		addPresetToGenerate(MissileData.Builder
				.trackMissileBuilder(DSCombatMod.MODID, "aim120c")
				.setFireRate(50)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(50f)
				.setSpeed(4.5f)
				.setExplosionRadius(5f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(3)
				.setMaxAge(400)
				.setFuelTicks(250)
				.setTurnRadius(100f)
				.setAcceleration(0.05f)
				.setBleed(0.025f)
				.setFuseDistance(3f)
				.setFieldOfView(70f)
				.setTargetType(TargetType.AIR)
				.setItem(ModItems.AIM120C.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 20)
				.addIngredient("minecraft:tnt", 3)
				.addIngredient("minecraft:coal_block", 5)
				.addIngredient("minecraft:amethyst_shard")
				.addIngredient("dscombat:intel_pentium")
				.build());
		addPresetToGenerate(MissileData.Builder
				.irMissileBuilder(DSCombatMod.MODID, "aim9l")
				.setFireRate(20)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(40f)
				.setSpeed(2.0f)
				.setExplosionRadius(3f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(4)
				.setMaxAge(300)
				.setFuelTicks(300)
				.setTurnRadius(75f)
				.setAcceleration(0.03f)
				.setBleed(0.01f)
				.setFuseDistance(3f)
				.setFieldOfView(50f)
				.setFlareResistance(1.5f)
				.setItem(ModItems.AIM9L.getId())
				.setEntityType(ModEntities.IR_MISSILE_1.getId())
				.setRackEntityType(ModEntities.LIGHT_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.LIGHT_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 12)
				.addIngredient("minecraft:tnt", 1)
				.addIngredient("minecraft:coal_block", 3)
				.addIngredient("minecraft:spider_eye", 1)
				.addIngredient("dscombat:ti83")
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
				.setFieldOfView(60f)
				.setFlareResistance(1f)
				.setItem(ModItems.AIM9P5.getId())
				.setEntityType(ModEntities.IR_MISSILE_1.getId())
				.setRackEntityType(ModEntities.LIGHT_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.LIGHT_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 16)
				.addIngredient("minecraft:tnt", 1)
				.addIngredient("minecraft:coal_block", 3)
				.addIngredient("minecraft:spider_eye", 2)
				.addIngredient("dscombat:ti83")
				.build());
		addPresetToGenerate(MissileData.Builder
				.irMissileBuilder(DSCombatMod.MODID, "aim9x")
				.setFireRate(20)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(45f)
				.setSpeed(3f)
				.setExplosionRadius(4f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(4)
				.setMaxAge(300)
				.setFuelTicks(300)
				.setTurnRadius(60f)
				.setAcceleration(0.05f)
				.setBleed(0.01f)
				.setFuseDistance(3f)
				.setFieldOfView(70f)
				.setFlareResistance(0.7f)
				.setItem(ModItems.AIM9X.getId())
				.setEntityType(ModEntities.IR_MISSILE_1.getId())
				.setRackEntityType(ModEntities.LIGHT_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.LIGHT_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 16)
				.addIngredient("minecraft:tnt", 2)
				.addIngredient("minecraft:coal_block", 4)
				.addIngredient("minecraft:spider_eye", 2)
				.addIngredient("dscombat:intel_pentium")
				.build());
	}
	
	protected void registerAGMissiles() {
		addPresetToGenerate(MissileData.Builder
				.posMissileBuilder(DSCombatMod.MODID, "agm114k")
				.setFireRate(20)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(40f)
				.setSpeed(3f)
				.setExplosionRadius(3f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(6)
				.setMaxAge(200)
				.setFuelTicks(200)
				.setTurnRadius(30f)
				.setAcceleration(0.05f)
				.setBleed(0.02f)
				.setFuseDistance(2f)
				.setFieldOfView(-1f)
				.setItem(ModItems.AGM114K.getId())
				.setEntityType(ModEntities.POS_MISSILE_1.getId())
				.setRackEntityType(ModEntities.LIGHT_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.LIGHT_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 8)
				.addIngredient("minecraft:tnt", 1)
				.addIngredient("minecraft:coal_block", 2)
				.addIngredient("dscombat:ti83")
				.build());
		addPresetToGenerate(MissileData.Builder
				.posMissileBuilder(DSCombatMod.MODID, "agm65l")
				.setFireRate(40)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(60f)
				.setSpeed(2f)
				.setExplosionRadius(5f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(4)
				.setMaxAge(300)
				.setFuelTicks(300)
				.setTurnRadius(60f)
				.setAcceleration(0.025f)
				.setBleed(0.02f)
				.setFuseDistance(1.5f)
				.setFieldOfView(-1f)
				.setItem(ModItems.AGM65L.getId())
				.setEntityType(ModEntities.POS_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 12)
				.addIngredient("minecraft:tnt", 3)
				.addIngredient("minecraft:coal_block", 2)
				.addIngredient("dscombat:ti83")
				.build());
		addPresetToGenerate(MissileData.Builder
				.trackMissileBuilder(DSCombatMod.MODID, "agm65g")
				.setTargetType(TargetType.GROUND)
				.setFireRate(40)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(60f)
				.setSpeed(2f)
				.setExplosionRadius(5f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(4)
				.setMaxAge(300)
				.setFuelTicks(300)
				.setTurnRadius(60f)
				.setAcceleration(0.025f)
				.setBleed(0.02f)
				.setFuseDistance(1.5f)
				.setFieldOfView(-1f)
				.setItem(ModItems.AGM65G.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 12)
				.addIngredient("minecraft:tnt", 3)
				.addIngredient("minecraft:coal_block", 2)
				.addIngredient("dscombat:ti83")
				.build());
		addPresetToGenerate(MissileData.Builder
				.trackMissileBuilder(DSCombatMod.MODID, "agm84e")
				.setTargetType(TargetType.GROUND)
				.setFireRate(50)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(60f)
				.setSpeed(2.5f)
				.setExplosionRadius(4f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(3)
				.setMaxAge(300)
				.setFuelTicks(300)
				.setTurnRadius(45f)
				.setAcceleration(0.035f)
				.setBleed(0.02f)
				.setFuseDistance(2f)
				.setFieldOfView(-1f)
				.setItem(ModItems.AGM84E.getId())
				.setEntityType(ModEntities.TRACK_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 20)
				.addIngredient("minecraft:tnt", 3)
				.addIngredient("minecraft:coal_block", 4)
				.addIngredient("dscombat:ti83", 2)
				.build());
	}
	
	protected void registerOtherMissiles() {
		addPresetToGenerate(MissileData.Builder
				.antiRadarMissileBuilder(DSCombatMod.MODID, "rifel1")
				.setFireRate(40)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(60f)
				.setSpeed(4f)
				.setExplosionRadius(8f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(2)
				.setMaxAge(600)
				.setFuelTicks(400)
				.setTurnRadius(100f)
				.setAcceleration(0.04f)
				.setBleed(0.02f)
				.setFuseDistance(2f)
				.setFieldOfView(40f)
				.setItem(ModItems.RIFEL1.getId())
				.setEntityType(ModEntities.ANTI_RADAR_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 20)
				.addIngredient("minecraft:tnt", 4)
				.addIngredient("minecraft:coal_block", 4)
				.addIngredient("dscombat:intel_pentium")
				.build());
		addPresetToGenerate(MissileData.Builder
				.torpedoBuilder(DSCombatMod.MODID, "torpedo1")
				.setFireRate(40)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(50f)
				.setSpeed(3f)
				.setExplosionRadius(4f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(true)
				.setMaxAmmo(3)
				.setMaxAge(600)
				.setFuelTicks(400)
				.setTurnRadius(80f)
				.setAcceleration(0.04f)
				.setBleed(0.04f)
				.setFuseDistance(2f)
				.setFieldOfView(60f)
				.setTargetType(TargetType.WATER)
				.setItem(ModItems.TORPEDO1.getId())
				.setEntityType(ModEntities.TORPEDO_MISSILE_1.getId())
				.setRackEntityType(ModEntities.HEAVY_MISSILE_RACK.getId())
				.setShootSound(ModSounds.MISSILE_LAUNCH_1.getId())
				.setCompatibleWeaponPart(ModItems.HEAVY_MISSILE_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 24)
				.addIngredient("minecraft:tnt", 5)
				.addIngredient("minecraft:coal_block", 8)
				.addIngredient("dscombat:ti83")
				.build());
	}
	
	protected void registerBombs() {
		addPresetToGenerate(BombData.Builder
				.bombBuilder(DSCombatMod.MODID, "anm30")
				.setFireRate(5)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(20f)
				.setSpeed(0f)
				.setExplosionRadius(3)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(false)
				.setMaxAmmo(16)
				.setMaxAge(200)
				.setItem(ModItems.BOMB.getId())
				.setEntityType(ModEntities.BOMB.getId())
				.setRackEntityType(ModEntities.BOMB_RACK.getId())
				.setShootSound(ModSounds.BOMB_SHOOT_1.getId())
				.setCompatibleWeaponPart(ModItems.BOMB_RACK.getId())
				.setCraftNum(4)
				.addIngredient("minecraft:iron_ingot", 8)
				.addIngredient("minecraft:tnt", 2)
				.build());
		addPresetToGenerate(BombData.Builder
				.bombBuilder(DSCombatMod.MODID, "anm57")
				.setFireRate(9)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(30f)
				.setSpeed(0f)
				.setExplosionRadius(4.5f)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(false)
				.setMaxAmmo(8)
				.setMaxAge(200)
				.setItem(ModItems.BOMB.getId())
				.setEntityType(ModEntities.BOMB.getId())
				.setRackEntityType(ModEntities.BOMB_RACK.getId())
				.setShootSound(ModSounds.BOMB_SHOOT_1.getId())
				.setCompatibleWeaponPart(ModItems.BOMB_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 8)
				.addIngredient("minecraft:tnt", 1)
				.build());
		addPresetToGenerate(BombData.Builder
				.bombBuilder(DSCombatMod.MODID, "anm64")
				.setFireRate(17)
				.setInnacuracy(0f)
				.setCanShootOnGround(false)
				.setDamage(40f)
				.setSpeed(0f)
				.setExplosionRadius(7)
				.setExplosive(true)
				.setDestoryTerrain(true)
				.setCausesFire(false)
				.setMaxAmmo(4)
				.setMaxAge(200)
				.setItem(ModItems.BOMB.getId())
				.setEntityType(ModEntities.BOMB.getId())
				.setRackEntityType(ModEntities.BOMB_RACK.getId())
				.setShootSound(ModSounds.BOMB_SHOOT_1.getId())
				.setCompatibleWeaponPart(ModItems.BOMB_RACK.getId())
				.setCraftNum(1)
				.addIngredient("minecraft:iron_ingot", 12)
				.addIngredient("minecraft:tnt", 2)
				.build());
	}
	
	@Override
	protected void registerPresets() {
		// TODO 0.5 re-balance weapon stats
		registerBullets();
		registerAAMissiles();
		registerAGMissiles();
		registerOtherMissiles();
		registerBombs();
	}	
	
	public WeaponPresetGenerator(DataGenerator output) {
		super(output, "weapons");
	}

	@Override
	public String getName() {
		return "Weapons: "+DSCombatMod.MODID;
	}
	
	
	
}
