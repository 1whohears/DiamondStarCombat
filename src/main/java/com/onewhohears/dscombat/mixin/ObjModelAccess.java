package com.onewhohears.dscombat.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.math.Vector3f;

import net.minecraftforge.client.model.obj.ObjModel;

@Mixin(ObjModel.class)
public interface ObjModelAccess {
	@Accessor(value = "positions", remap = false)
	List<Vector3f> getPositions();
}
