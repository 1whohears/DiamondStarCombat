package com.onewhohears.dscombat.data;

import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EntityTypeTagGen extends EntityTypeTagsProvider {
	
	public EntityTypeTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, DSCombatMod.MODID, existingFileHelper);
	}
	
	@Override
	protected void addTags() {
		tag(ModTags.EntityTypes.PROJECTILE)
			.addTag(ModTags.EntityTypes.BULLET)
			.addTag(ModTags.EntityTypes.BOMB)
			.addTag(ModTags.EntityTypes.MISSILE)
			.addTag(ModTags.EntityTypes.WEAK_PROJECTILE)
			.addTag(ModTags.EntityTypes.PIERCING_PROJECTILE);
		tag(ModTags.EntityTypes.WEAK_PROJECTILE)
			.addTag(EntityTypeTags.ARROWS);
		tag(ModTags.EntityTypes.PIERCING_PROJECTILE)
			.addTag(EntityTypeTags.IMPACT_PROJECTILES)
			.add(EntityType.TRIDENT);
		tag(ModTags.EntityTypes.BULLET)
			.addOptional(new ResourceLocation("cgm:projectile"))
			.addOptional(new ResourceLocation("moguns:taki"))
			.addOptional(new ResourceLocation("moguns:magma_cream"))
			.addOptional(new ResourceLocation("moguns:flare"));
		tag(ModTags.EntityTypes.BOMB)
			.addOptional(new ResourceLocation("cgm:grenade"))
			.addOptional(new ResourceLocation("cgm:throwable_grenade"))
			.addOptional(new ResourceLocation("cgm:throwable_stun_grenade"));
		tag(ModTags.EntityTypes.MISSILE)
			.addOptional(new ResourceLocation("cgm:missile"));
		tag(ModTags.EntityTypes.VEHICLE)
			.addTag(ModTags.EntityTypes.PLANE)
			.addTag(ModTags.EntityTypes.HELI)
			.addTag(ModTags.EntityTypes.CAR)
			.addTag(ModTags.EntityTypes.TANK)
			.addTag(ModTags.EntityTypes.BOAT)
			.addTag(ModTags.EntityTypes.SUBMARINE);
		tag(ModTags.EntityTypes.PLANE);
		tag(ModTags.EntityTypes.HELI);
		tag(ModTags.EntityTypes.CAR);
		tag(ModTags.EntityTypes.TANK);
		tag(ModTags.EntityTypes.BOAT);
		tag(ModTags.EntityTypes.SUBMARINE);
		tag(ModTags.EntityTypes.RADAR_MOBS);
		tag(ModTags.EntityTypes.RADAR_VEHICLES)
			.addTag(ModTags.EntityTypes.VEHICLE)
			.add(EntityType.BOAT, EntityType.MINECART, ModEntities.PARACHUTE.get())
			.addOptional(new ResourceLocation("simpleplanes:plane"))
			.addOptional(new ResourceLocation("simpleplanes:large_plane"))
			.addOptional(new ResourceLocation("simpleplanes:helicopter"))
			.addOptional(new ResourceLocation("simpleplanes:parachute"));
		tag(ModTags.EntityTypes.TURRET_SHOOT)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_RANDOM)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_DUMBASS)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_STUPID)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_NORMAL)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_SMART);
		tag(ModTags.EntityTypes.TURRET_SHOOT_RANDOM);
		tag(ModTags.EntityTypes.TURRET_SHOOT_DUMBASS);
		tag(ModTags.EntityTypes.TURRET_SHOOT_STUPID);
		tag(ModTags.EntityTypes.TURRET_SHOOT_NORMAL);
		tag(ModTags.EntityTypes.TURRET_SHOOT_SMART);
		tag(ModTags.EntityTypes.TURRET_TARGET_MONSTERS);
		tag(ModTags.EntityTypes.TURRET_TARGET_PLAYERS);
		tag(ModTags.EntityTypes.OVERRIDE_RADAR_VISABILITY);
		tag(ModTags.EntityTypes.IR_EMITTER)
			.addTag(ModTags.EntityTypes.IR_EMITTER_LOW)
			.addTag(ModTags.EntityTypes.IR_EMITTER_MED)
			.addTag(ModTags.EntityTypes.IR_EMITTER_HIGH)
			.addTag(ModTags.EntityTypes.IR_EMITTER_EXTREME);
		tag(ModTags.EntityTypes.IR_EMITTER_LOW)
			.add(EntityType.PLAYER, EntityType.ALLAY, EntityType.PHANTOM, EntityType.SHULKER_BULLET, EntityType.VEX);
		tag(ModTags.EntityTypes.IR_EMITTER_MED)
			.add(EntityType.WITHER_SKULL, EntityType.BEE, EntityType.FIREWORK_ROCKET, EntityType.SMALL_FIREBALL, EntityType.GHAST)
			.addTag(ModTags.EntityTypes.MISSILE)
			.addOptional(new ResourceLocation("simpleplanes:plane"))
			.addOptional(new ResourceLocation("simpleplanes:large_plane"))
			.addOptional(new ResourceLocation("simpleplanes:helicopter"));
		tag(ModTags.EntityTypes.IR_EMITTER_HIGH)
			.add(EntityType.FIREBALL, EntityType.DRAGON_FIREBALL, EntityType.BLAZE, EntityType.MAGMA_CUBE);
		tag(ModTags.EntityTypes.IR_EMITTER_EXTREME)
			.add(EntityType.WITHER, EntityType.ENDER_DRAGON);
	}
	
}
