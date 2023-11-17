package com.onewhohears.dscombat.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
	private List<VoxelShape> dscombat_EntityCollisionList(List<VoxelShape> list, Vec3 move) {
		// Mojang decided to make this a ImmutableCollections so list.add throws an exception
		List<VoxelShape> colliders = new ArrayList<>(list); 
		Entity entity = (Entity)(Object)this;
		AABB aabb = entity.getBoundingBox().expandTowards(move).inflate(1.0E-7D);
		for (PartEntity<?> part : entity.level.getPartEntities()) {
			if (entity.equals(part)) continue;
			if (part.getParent().equals(entity)) continue;
			if (!(part instanceof RotableHitbox hitbox)) continue;
			hitbox.handlePosibleCollision(colliders, entity, aabb, move);
		}
		return colliders;
	}
	
	//net.minecraft.world.level.EntityGetter.getEntityCollisions(Entity, AABB)
	//net.minecraft.world.entity.Entity
	//net.minecraft.world.phys.AABB
	/*final String METHOD = "Lnet/minecraft/world/level/EntityGetter;"
			+ "getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;";
	
	//com.google.common.collect.ImmutableList.Builder<VoxelShape>
	//net.minecraft.world.phys.shapes.VoxelShape
	final String TARGET = "Lcom/google/common/collect/ImmutableList$Builder;"
			+ "add(Lnet/minecraft/world/phys/shapes/VoxelShape;)Lcom/google/common/collect/ImmutableList$Builder;";
	
	@Inject(method = METHOD, at = @At(value = "INVOKE", target = TARGET, shift = At.Shift.AFTER))
	private void dscombat_RotableHitboxCollisionDetection(CallbackInfo ci) {
		System.out.println("AFTER SHAPE BEING ADDED");
	}*/
	
}
