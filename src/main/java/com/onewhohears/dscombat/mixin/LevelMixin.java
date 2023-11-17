package com.onewhohears.dscombat.mixin;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.PartEntity;

@Mixin(Level.class)
public abstract class LevelMixin {
	
	// net.minecraft.world.level.Level.getEntities(Entity, AABB, Predicate<? super Entity>)
	// java.util.List<Entity>
	// net.minecraft.world.entity.Entity
	// net.minecraft.world.phys.AABB
	// java.util.function.Predicate<? super Entity>
	@Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", 
			at = @At("RETURN"))
	private void dscombat_RotableHitboxCollisionCheck(@Nullable Entity entity, AABB aabb, Predicate<? super Entity> predicate, 
			CallbackInfoReturnable<List<Entity>> ci) {
		if (entity == null) return;
		List<Entity> entities = ci.getReturnValue();
		for (PartEntity<?> part : entity.level.getPartEntities()) {
			if (entity.equals(part)) continue;
			if (part.getParent().equals(entity)) continue;
			if (!predicate.test(part)) continue;
			if (!(part instanceof RotableHitbox hitbox)) continue;
			if (hitbox.couldCollide(entity) && hitbox.getHitbox().isColliding(aabb)) {
				entities.add(hitbox);
				//System.out.println("ADDING HITBOX FOR "+entity);
			}
		}
	}
	
}
