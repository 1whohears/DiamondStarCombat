package com.onewhohears.dscombat.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.onewhohears.dscombat.command.argument.WeaponArgument;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MissileCommand {
	
	public MissileCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("missile").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("target", EntityArgument.entities())
			.then(Commands.argument("pos", Vec3Argument.vec3()).executes((context) -> {
				return testMissile(context, EntityArgument.getEntities(context, "target"), 
						Vec3Argument.getVec3(context, "pos"), null, null);})
			.then(Commands.argument("weapon", WeaponArgument.weapon()).executes((context) -> {
				return testMissile(context, EntityArgument.getEntities(context, "target"), 
						Vec3Argument.getVec3(context, "pos"), 
						WeaponArgument.getWeapon(context, "weapon"), null);})
			.then(Commands.argument("owner", EntityArgument.entity()).executes((context) -> {
				return testMissile(context, EntityArgument.getEntities(context, "target"), 
						Vec3Argument.getVec3(context, "pos"), 
						WeaponArgument.getWeapon(context, "weapon"), 
						EntityArgument.getEntity(context, "owner"));})
			)))));
	}
	
	private int testMissile(CommandContext<CommandSourceStack> context, Collection<? extends Entity> targets, Vec3 pos, WeaponStats weaponStats, Entity owner) throws CommandSyntaxException {
		String defaultId = "aim120b";
		if (weaponStats == null) weaponStats = WeaponPresets.get().get(defaultId);
		if (weaponStats == null) {
			context.getSource().sendFailure(UtilMCText.literal("Default Weapon Preset "+defaultId+" does not exist?!"));
			return 0;
		}
		WeaponInstance<?> weapon = weaponStats.createWeaponInstance();
		int i = 0;
		for (Entity e : targets) {
			Vec3 dp = e.position().subtract(pos).normalize();
			EntityWeapon<?> ew = weapon.getEntity(e.level);
			ew.setOwner(owner);
			ew.setPos(pos);
			weapon.setDirection(ew, dp);
			if (ew instanceof EntityMissile<?> missile) {
				Entity v = e.getRootVehicle();
				if (v != null) e = v;
				missile.target = e;
				missile.targetPos = e.position();
			}
			e.level.addFreshEntity(ew);
			//ew.tick();
			++i;
		}
		if (i == 0) context.getSource().sendFailure(UtilMCText.literal("No targets found!"));
		else if (i > 0) context.getSource().sendSuccess(UtilMCText.literal("Launched "+i+" missiles!"), true);
		return 1;
	}
	
}
