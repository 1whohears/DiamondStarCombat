package com.onewhohears.dscombat.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.onewhohears.dscombat.entity.vehicle.RotableHitbox;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	@Inject(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", 
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;lengthSqr()D", shift = At.Shift.AFTER))
	private void dscombat_EntityCollideVec(CallbackInfoReturnable<Vec3> cir, @Local LocalRef<Vec3> vec3) {
		Entity entity = (Entity)(Object)this;
		if (entity.noPhysics) return;
		if (!entity.getType().getDescriptionId().equals(EntityType.PLAYER.getDescriptionId())) return;
		Vec3 move = vec3.get();
		AABB entityBox = entity.getBoundingBox().expandTowards(move);
		AABB searchBox = entity.getBoundingBox().inflate(64);
		List<RotableHitbox> hitboxes = entity.level.getEntitiesOfClass(RotableHitbox.class, searchBox);
		for (RotableHitbox hitbox: hitboxes) {
			if (entity.equals(hitbox.getParent())) continue;
			move = hitbox.collide(entity, entityBox, move);
		}
		vec3.set(move);
		return;
	}
	
	/*@ModifyVariable(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At(value = "STORE", ordinal = 1))
	private Vec3 dscombat_EntityCollideVec(Vec3 vec3) {
		Entity entity = (Entity)(Object)this;
		Vec3 move = vec3;
		if (entity.noPhysics) return move;
		if (!entity.getType().getDescriptionId().equals(EntityType.PLAYER.getDescriptionId())) return move;
		AABB entityBox = entity.getBoundingBox().expandTowards(move);
		AABB searchBox = entity.getBoundingBox().inflate(64);
		List<RotableHitbox> hitboxes = entity.level.getEntitiesOfClass(RotableHitbox.class, searchBox);
		for (RotableHitbox hitbox: hitboxes) {
			if (entity.equals(hitbox.getParent())) continue;
			move = hitbox.collide(entity, entityBox, move);
		}
		return move;
	}*/
	
	//net.minecraft.world.entity.Entity.collide(Vec3)
	//net.minecraft.world.phys.Vec3
	/**
	 * adds RotableHitbox VoxelShapes to the list of entity colliders an entity can collide with.
	 */
	/*@ModifyVariable(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("STORE"))
	private List<VoxelShape> dscombat_EntityCollisionList(List<VoxelShape> list, @Local LocalRef<Vec3> pVec) {
		// Mojang decided to make this a ImmutableCollections so list.add throws an exception
		List<VoxelShape> colliders = new ArrayList<>(list); 
		Entity entity = (Entity)(Object)this;
		if (entity.noPhysics) return colliders;
		if (!entity.getType().getDescriptionId().equals(EntityType.PLAYER.getDescriptionId())) return colliders;
		Vec3 move = pVec.get();
		AABB entityBox = entity.getBoundingBox().expandTowards(move);
		AABB searchBox = entity.getBoundingBox().inflate(64);
		//List<EntityVehicle> vehicles = entity.level.getEntitiesOfClass(EntityVehicle.class, searchBox);
		List<RotableHitbox> hitboxes = entity.level.getEntitiesOfClass(RotableHitbox.class, searchBox);
		if (colliders.size() > 0) {
			System.out.println("++++++++++++++++++");
			System.out.println("colliders pre = "+colliders.toString());
		}
		//for (PartEntity<?> part : entity.level.getPartEntities()) {
		//for (EntityVehicle vehicle: vehicles)
		RotCollideResult result = RotCollideResult.NONE;
		for (RotableHitbox hitbox: hitboxes) {
		//for (RotableHitbox hitbox: vehicle.getHitboxes()) {
			if (entity.equals(hitbox.getParent())) continue;
			//if (!(part instanceof RotableHitbox hitbox)) continue;
			RotCollideResult r = hitbox.handlePosibleCollision(colliders, entity, entityBox, move);
			if (r == RotCollideResult.STUCK) {
				pVec.set(move.multiply(1, 0, 1));
				//entity.setPos(entity.position().add(0, 1E-7, 0));
				entity.setOnGround(true);
				entity.resetFallDistance();
			}
			if (r.isCollide()) result = r;
		}
		/*if (result.isCollide()) {
			colliders.remove(0);
			colliders.remove(0);
		}*
		//if (colliders.size() > 0) System.out.println("colliders post = "+colliders.toString());
		if (colliders.size() > 0) {
			System.out.println("colliders post = "+colliders.toString());
			Vec3 vec3 = move.lengthSqr() == 0.0D ? move : Entity.collideBoundingBox(entity, move, 
					entity.getBoundingBox(), entity.level, colliders);
			System.out.println("vec3 = "+vec3);
		}
		return colliders;
	}*/
	
}
