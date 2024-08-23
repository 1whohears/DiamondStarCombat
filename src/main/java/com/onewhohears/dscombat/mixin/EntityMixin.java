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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	//net.minecraft.world.entity.Entity.collide(Vec3)
	//net.minecraft.world.phys.Vec3
	/**
	 * updates the collide vector to account for collisions with RotableHitbox
	 */
	@Inject(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", 
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;lengthSqr()D", shift = At.Shift.AFTER))
	private void dscombat_EntityCollideVec(CallbackInfoReturnable<Vec3> cir, @Local LocalRef<Vec3> vec3) {
		Entity entity = (Entity)(Object)this;
		if (entity.noPhysics) return;
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
	
}
