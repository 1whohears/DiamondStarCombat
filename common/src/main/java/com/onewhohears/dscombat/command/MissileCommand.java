package com.onewhohears.dscombat.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.command.argument.WeaponArgument;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.common.core.SimulatedEntityManager;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MissileCommand {
	
	public MissileCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("missile").requires((stack) -> stack.hasPermission(2))
                .then(Commands.literal("no_owner")
                        .then(testMissileParams(ctx -> null))
                )
                .then(Commands.argument("owner", EntityArgument.entity())
                        .then(testMissileParams(ctx -> EntityArgument.getEntity(ctx, "owner")))
                )
        );
	}

    interface GetOwnerCommand {
        Entity apply(CommandContext<CommandSourceStack> t) throws CommandSyntaxException;
    }

    private ArgumentBuilder<CommandSourceStack, ?> testMissileParams(GetOwnerCommand owner) {
        return Commands.argument("target", EntityArgument.entities())
                .then(Commands.argument("pos", Vec3Argument.vec3()).executes((context) -> {
                            return testMissile(context, EntityArgument.getEntities(context, "target"),
                                    Vec3Argument.getVec3(context, "pos"), null, owner.apply(context),
                                    -1, -1000, -1000);})
                        .then(Commands.argument("weapon", WeaponArgument.weapon()).executes((context) -> {
                                    return testMissile(context, EntityArgument.getEntities(context, "target"),
                                            Vec3Argument.getVec3(context, "pos"),
                                            WeaponArgument.getWeapon(context, "weapon"), owner.apply(context),
                                            -1, -1000, -1000);})
                                .then(Commands.argument("speed", DoubleArgumentType.doubleArg(0, 100)).executes((context) -> {
                                            return testMissile(context, EntityArgument.getEntities(context, "target"),
                                                    Vec3Argument.getVec3(context, "pos"),
                                                    WeaponArgument.getWeapon(context, "weapon"),
                                                    owner.apply(context),
                                                    DoubleArgumentType.getDouble(context, "speed"), -1000, -1000);})
                                        .then(Commands.argument("pitch", AngleArgument.angle()).executes((context) -> {
                                                    return testMissile(context, EntityArgument.getEntities(context, "target"),
                                                            Vec3Argument.getVec3(context, "pos"),
                                                            WeaponArgument.getWeapon(context, "weapon"),
                                                            owner.apply(context),
                                                            DoubleArgumentType.getDouble(context, "speed"),
                                                            AngleArgument.getAngle(context, "pitch"), -1000);})
                                                .then(Commands.argument("yaw", AngleArgument.angle()).executes((context) -> {
                                                            return testMissile(context, EntityArgument.getEntities(context, "target"),
                                                                    Vec3Argument.getVec3(context, "pos"),
                                                                    WeaponArgument.getWeapon(context, "weapon"),
                                                                    owner.apply(context),
                                                                    DoubleArgumentType.getDouble(context, "speed"),
                                                                    AngleArgument.getAngle(context, "pitch"),
                                                                    AngleArgument.getAngle(context, "yaw"));})
                                                        )))));
    }
	
	private int testMissile(CommandContext<CommandSourceStack> context, Collection<? extends Entity> targets, Vec3 pos,
							WeaponStats weaponStats, Entity owner, double initSpeed, float pitch, float yaw) throws CommandSyntaxException {
		String defaultId = "aim120b";
		if (weaponStats == null) weaponStats = WeaponPresets.get().get(defaultId);
		if (weaponStats == null) {
			context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.default_weapon_not_exist", defaultId));
			return 0;
		}
		WeaponInstance<?> weapon = weaponStats.createWeaponInstance();
		int i = 0;
		for (Entity e : targets) {
			Vec3 dp = e.position().subtract(pos).normalize();
			if (pitch != -1000) {
				if (yaw == -1000) yaw = UtilAngles.getYaw(dp);
				dp = Vec3.directionFromRotation(pitch, yaw);
			}
            Level level = UtilEntity.getLevel(e);
			EntityWeapon<?> ew = weapon.getEntity(level);
			if (ew == null) continue;
			ew.setOwner(owner);
			ew.setPos(pos);
			weapon.setDirection(ew, dp);
			if (initSpeed != -1) ew.setDeltaMovement(dp.scale(initSpeed));
			if (ew instanceof EntityMissile<?> missile) {
				Entity v = e.getRootVehicle();
				missile.target = v;
				missile.targetPos = v.position();
                SimulatedEntityManager.get().startSimulatingEntity(missile);
			}
			level.addFreshEntity(ew);
            DependencySafety.onWeaponShoot(ew);
			//ew.tick();
			++i;
		}
		if (i == 0) context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.no_targets_found"));
		else if (i > 0) {
            int num = i;
            context.getSource().sendSuccess(() -> UtilMCText.translatable("success.dscombat.launched_missile", num), true);
        }
		return 1;
	}
	
}
