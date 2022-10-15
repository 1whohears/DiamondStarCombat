package com.onewhohears.dscombat.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.data.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.MissileData.TargetType;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class TestWeaponCommand {
	
	public TestWeaponCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("testweapon").requires(null).requires((stack) -> {return stack.hasPermission(2);})
			.then(Commands.literal("missile")
			.then(Commands.argument("target", EntityArgument.players())
			.executes((context) -> {
				return testMissile(context, EntityArgument.getPlayers(context, "target"));
			}
		))));
	}
	
	private int testMissile(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) throws CommandSyntaxException {
		int i = 0;
		for (ServerPlayer p : players) {
			MissileData data = new MissileData("test_missile", Vec3.ZERO, 
					300, 10, 10, 100, 4, 0, 
					true, true, false, 
					100, 4, TargetType.AIR, GuidanceType.PITBULL, 
					2, 0.04, 3, 70);
			EntityMissile missile = new EntityMissile(p.level, p, data);
			missile.setPos(p.position().add(300, 10, 0));
			Vec3 dp = p.position().subtract(missile.position());
			missile.setXRot(UtilAngles.getPitch(dp));
			missile.setYRot(UtilAngles.getYaw(dp));
			if (p.getRootVehicle() != null) missile.target = p.getRootVehicle();
			else missile.target = p;
			// TODO force load chunk for missile
			p.level.addFreshEntity(missile);
			++i;
		}
		context.getSource().sendSuccess(UtilMCText.simpleText("baller"), true);
		return i;
	}
	
}
