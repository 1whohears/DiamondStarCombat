package com.onewhohears.dscombat.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.data.WeaponPresets;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MissileCommand {
	
	public MissileCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("missile").requires(null).requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("target", EntityArgument.entities())
			.then(Commands.argument("pos", Vec3Argument.vec3()).executes((context) -> {
					return testMissile(context, EntityArgument.getEntities(context, "target"), 
							Vec3Argument.getVec3(context, "pos"), null);})
			.then(Commands.argument("nbt", CompoundTagArgument.compoundTag()).executes((context) -> {
				return testMissile(context, EntityArgument.getEntities(context, "target"), 
						Vec3Argument.getVec3(context, "pos"), 
						CompoundTagArgument.getCompoundTag(context, "nbt"));})
		))));
	}
	
	private int testMissile(CommandContext<CommandSourceStack> context, Collection<? extends Entity> targets, Vec3 pos, CompoundTag tag) throws CommandSyntaxException {
		Entity source = context.getSource().getEntity();
		MissileData data;
		if (tag == null) data = WeaponPresets.getDefaultTestMissile();
		else data = new MissileData(tag);
		int i = 0;
		for (Entity e : targets) {
			EntityMissile missile = new EntityMissile(e.level, source, data);
			missile.setPos(pos);
			Vec3 dp = e.position().subtract(missile.position()).normalize();
			missile.setXRot(UtilAngles.getPitch(dp));
			missile.setYRot(UtilAngles.getYaw(dp));
			missile.setDeltaMovement(dp.scale(1d));
			if (e.getRootVehicle() != null) missile.target = e.getRootVehicle();
			else missile.target = e;
			missile.targetPos = missile.target.position();
			int cx = missile.chunkPosition().x;
			int cz = missile.chunkPosition().z;
			ChunkManager.addChunk(missile, cx, cz);
			if (e.level.addFreshEntity(missile)) ++i;
		}
		if (i == 0) context.getSource().sendFailure(UtilMCText.simpleText("No targets found!"));
		else if (i > 0) context.getSource().sendSuccess(UtilMCText.simpleText("Launched "+i+" missiles!"), true);
		return i;
	}
	
}
