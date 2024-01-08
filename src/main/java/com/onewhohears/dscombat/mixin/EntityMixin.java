package com.onewhohears.dscombat.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	//net.minecraft.world.entity.Entity.collide(Vec3)
	//net.minecraft.world.phys.Vec3
	/**
	 * adds RotableHitbox VoxelShapes to the list of entity colliders an entity can collide with.
	 */
	@ModifyVariable(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("STORE"))
	private List<VoxelShape> dscombat_EntityCollisionList(List<VoxelShape> list, @Local LocalRef<Vec3> pVec) {
		// Mojang decided to make this a ImmutableCollections so list.add throws an exception
		List<VoxelShape> colliders = new ArrayList<>(list); 
		Entity entity = (Entity)(Object)this;
		Vec3 move = pVec.get();
		AABB entityBox = entity.getBoundingBox().expandTowards(move);
		for (PartEntity<?> part : entity.level.getPartEntities()) {
			if (entity.equals(part)) continue;
			if (part.getParent().equals(entity)) continue;
			if (part.distanceToSqr(entity) > 262144) continue;
			if (!(part instanceof RotableHitbox hitbox)) continue;
			boolean stuck = hitbox.handlePosibleCollision(colliders, entity, entityBox, move);
			if (stuck) {
				pVec.set(move.multiply(1, 0, 1));
				entity.setPos(entity.position().add(0, 1E-7, 0));
				entity.setOnGround(true);
				entity.resetFallDistance();
			}
		}
		return colliders;
	}
	
}
