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
	
	@Inject(method = "getEntities", at = @At("RETURN"), cancellable = true)
	private void dscombat_GetEntities(@Nullable Entity pEntity, AABB pBoundingBox, Predicate<? super Entity> pPredicate, CallbackInfoReturnable<List<Entity>> cir) {
		Level level = (Level)(Object)this;
		List<Entity> entities = cir.getReturnValue();
		List<RotableHitbox> hitboxes = RotableHitboxes.getHitboxes(level.dimension());
		for (int i = 0; i < hitboxes.size(); ++i) {
			if (!hitboxes.get(i).getBoundingBox().intersects(pBoundingBox)) continue;
			entities.add(hitboxes.get(i));
		}
		//cir.setReturnValue(entities);
	}
	
}
