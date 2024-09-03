package com.onewhohears.dscombat.mixin;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.onewhohears.dscombat.entity.vehicle.RotableHitbox;
import com.onewhohears.dscombat.entity.vehicle.RotableHitboxes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@Mixin(Level.class)
public abstract class LevelMixin {
	
	@Inject(method = "getEntities", at = @At("RETURN"))
	private void dscombat_GetEntities(@Nullable Entity pEntity, AABB pBoundingBox, Predicate<? super Entity> pPredicate, CallbackInfoReturnable<List<Entity>> cir) {
		Level level = (Level)(Object)this;
		List<Entity> entities = cir.getReturnValue();
		List<RotableHitbox> hitboxes = RotableHitboxes.getHitboxes(level.dimension());
		for (int i = 0; i < hitboxes.size(); ++i) { // MUST USE INDEX LOOP TO AVOID ARRAY MODIFICATION ERRORS
			RotableHitbox hitbox = hitboxes.get(i);
			if (hitbox == null) continue;
			if (containsEntity(entities, hitbox)) continue;
			if (!pPredicate.test(hitbox)) continue;
			if (!hitbox.getBoundingBox().intersects(pBoundingBox)) continue;
			entities.add(hitbox);
		}
	}
	
	private static boolean containsEntity(List<Entity> entities, Entity entity) {
		for (Entity e : entities) 
			if (e.getId() == entity.getId())
				return true;
		return false;
	}
	
}
