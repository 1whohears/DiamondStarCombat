package com.onewhohears.dscombat.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.onewhohears.dscombat.data.vehicle.OutdatedVehicleEntityTypes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
	
	//net.minecraft.world.entity.EntityType.by(CompoundTag)
	//java.util.Optional<EntityType<?>>
	//net.minecraft.nbt.CompoundTag
	//@Inject(method = "by(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/Optional<EntityType<?>>;", at = @At("RETURN"), cancellable = true)
	@Inject(method = "by", at = @At("RETURN"), cancellable = true)
	private static void dscombat_EntityTypeByNBT(CompoundTag pCompound, CallbackInfoReturnable<Optional<EntityType<?>>> info) {
		//System.out.println("checking compound: "+pCompound);
		if (info.getReturnValue().isPresent()) return;
		info.setReturnValue(OutdatedVehicleEntityTypes.getEntityTypeByOldId(pCompound.getString("id")));
	}
	
}
