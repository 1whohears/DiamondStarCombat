package com.onewhohears.dscombat.data.tag;

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
		// WEAPONS
		tag(ModTags.EntityTypes.PROJECTILE)
			.addTag(EntityTypeTags.IMPACT_PROJECTILES)
			.addTag(ModTags.EntityTypes.BULLET)
			.addTag(ModTags.EntityTypes.BOMB)
			.addTag(ModTags.EntityTypes.MISSILE)
			.addTag(ModTags.EntityTypes.ANTI_TANK_SHELL);
		tag(ModTags.EntityTypes.ANTI_TANK_SHELL)
			.add(EntityType.TRIDENT)
			.addTag(ModTags.EntityTypes.MISSILE);
		tag(ModTags.EntityTypes.BULLET)
			.add(ModEntities.BULLET.get())
			.addOptional(new ResourceLocation("cgm:projectile"))
			.addOptional(new ResourceLocation("moguns:taki"))
			.addOptional(new ResourceLocation("moguns:magma_cream"))
			.addOptional(new ResourceLocation("moguns:flare"));
		tag(ModTags.EntityTypes.BOMB)
			.add(ModEntities.BOMB.get(), ModEntities.BUNKER_BUSTER.get())
			.addOptional(new ResourceLocation("cgm:grenade"))
			.addOptional(new ResourceLocation("cgm:throwable_grenade"))
			.addOptional(new ResourceLocation("cgm:throwable_stun_grenade"));
		tag(ModTags.EntityTypes.MISSILE)
			.add(ModEntities.POS_MISSILE.get(), ModEntities.IR_MISSILE.get(), ModEntities.TRACK_MISSILE.get(), ModEntities.ANTI_RADAR_MISSILE.get())
			.add(ModEntities.TORPEDO_MISSILE.get())
			.addOptional(new ResourceLocation("cgm:missile"));
		// VEHICLES
		tag(ModTags.EntityTypes.VEHICLE)
			.addTag(ModTags.EntityTypes.PLANE)
			.addTag(ModTags.EntityTypes.HELI)
			.addTag(ModTags.EntityTypes.CAR)
			.addTag(ModTags.EntityTypes.TANK)
			.addTag(ModTags.EntityTypes.BOAT)
			.addTag(ModTags.EntityTypes.SUBMARINE)
			.addTag(ModTags.EntityTypes.MISC_VEHICLE);
		tag(ModTags.EntityTypes.MISC_VEHICLE)
			.add(EntityType.BOAT, EntityType.MINECART, ModEntities.PARACHUTE.get())
			.addOptional(new ResourceLocation("simpleplanes:parachute"))
			.addOptional(new ResourceLocation("iceandfire:fire_dragon"))
			.addOptional(new ResourceLocation("iceandfire:ice_dragon"))
			.addOptional(new ResourceLocation("iceandfire:lightning_dragon"));
		tag(ModTags.EntityTypes.PLANE)
			.add(ModEntities.PLANE.get())
			.addOptional(new ResourceLocation("simpleplanes:plane"))
			.addOptional(new ResourceLocation("simpleplanes:large_plane"));
		tag(ModTags.EntityTypes.HELI)
			.add(ModEntities.HELICOPTER.get())
			.addOptional(new ResourceLocation("simpleplanes:helicopter"));
		tag(ModTags.EntityTypes.CAR)
			.add(ModEntities.CAR.get());
		tag(ModTags.EntityTypes.TANK)
			.add(ModEntities.CAR.get());
		tag(ModTags.EntityTypes.BOAT)
			.add(ModEntities.BOAT.get());
		tag(ModTags.EntityTypes.SUBMARINE)
			.add(ModEntities.SUBMARINE.get());
		// TURRET
		tag(ModTags.EntityTypes.TURRET_SHOOT)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_RANDOM)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_DUMBASS)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_STUPID)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_NORMAL)
			.addTag(ModTags.EntityTypes.TURRET_SHOOT_SMART);
		tag(ModTags.EntityTypes.TURRET_SHOOT_RANDOM)
			.add(EntityType.FROG, EntityType.CREEPER, EntityType.WARDEN);
		tag(ModTags.EntityTypes.TURRET_SHOOT_DUMBASS)
			.add(EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.ZOMBIFIED_PIGLIN);
		tag(ModTags.EntityTypes.TURRET_SHOOT_STUPID)
			.add(EntityType.ZOMBIE, EntityType.ALLAY, EntityType.DROWNED, EntityType.HUSK, EntityType.PIGLIN_BRUTE)
			.add(EntityType.ZOMBIE_VILLAGER, EntityType.STRAY);
		tag(ModTags.EntityTypes.TURRET_SHOOT_NORMAL)
			.add(EntityType.IRON_GOLEM, EntityType.ENDERMAN, EntityType.WITHER_SKELETON);
		tag(ModTags.EntityTypes.TURRET_SHOOT_SMART)
			.add(EntityType.SKELETON, EntityType.PIGLIN, EntityType.PILLAGER, EntityType.EVOKER)
			.add(EntityType.VINDICATOR, EntityType.WITCH, EntityType.BLAZE, EntityType.SNOW_GOLEM);
		tag(ModTags.EntityTypes.TURRET_TARGET_MONSTERS)
			.add(EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.FROG, EntityType.ALLAY);
		tag(ModTags.EntityTypes.TURRET_TARGET_PLAYERS)
			.add(EntityType.ZOMBIE, EntityType.CAVE_SPIDER, EntityType.SPIDER, EntityType.CREEPER, EntityType.DROWNED)
			.add(EntityType.ENDERMAN, EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.BLAZE, EntityType.EVOKER)
			.add(EntityType.HUSK, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.SKELETON, EntityType.STRAY)
			.add(EntityType.VINDICATOR, EntityType.WARDEN, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.ZOMBIE_VILLAGER);
		// IR EMMITERS
		tag(ModTags.EntityTypes.IR_EMITTER)
			.addTag(ModTags.EntityTypes.FLARE)
			.addTag(ModTags.EntityTypes.VEHICLE)
			.addTag(ModTags.EntityTypes.IR_EMITTER_LOW)
			.addTag(ModTags.EntityTypes.IR_EMITTER_MED)
			.addTag(ModTags.EntityTypes.IR_EMITTER_HIGH)
			.addTag(ModTags.EntityTypes.IR_EMITTER_EXTREME);
		tag(ModTags.EntityTypes.FLARE)
			.add(ModEntities.FLARE.get());
		tag(ModTags.EntityTypes.IR_EMITTER_LOW)
			.add(EntityType.PLAYER, EntityType.ALLAY, EntityType.PHANTOM, EntityType.SHULKER_BULLET, EntityType.VEX)
			.addOptional(new ResourceLocation("iceandfire:ice_dragon"));
		tag(ModTags.EntityTypes.IR_EMITTER_MED)
			.add(EntityType.WITHER_SKULL, EntityType.BEE, EntityType.FIREWORK_ROCKET, EntityType.SMALL_FIREBALL, EntityType.GHAST)
			.addTag(ModTags.EntityTypes.MISSILE)
			.addOptional(new ResourceLocation("simpleplanes:plane"))
			.addOptional(new ResourceLocation("simpleplanes:large_plane"))
			.addOptional(new ResourceLocation("simpleplanes:helicopter"));
		tag(ModTags.EntityTypes.IR_EMITTER_HIGH)
			.add(EntityType.FIREBALL, EntityType.DRAGON_FIREBALL, EntityType.BLAZE, EntityType.MAGMA_CUBE);
		tag(ModTags.EntityTypes.IR_EMITTER_EXTREME)
			.add(EntityType.WITHER, EntityType.ENDER_DRAGON)
			.addOptional(new ResourceLocation("iceandfire:fire_dragon"))
			.addOptional(new ResourceLocation("iceandfire:lightning_dragon"));
		// OTHER
		tag(ModTags.EntityTypes.ALWAYS_GROUNDED).add(EntityType.BOAT, EntityType.MINECART);
	}
	
}
