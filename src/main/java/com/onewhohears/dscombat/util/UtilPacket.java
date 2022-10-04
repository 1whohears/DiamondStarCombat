package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilPacket {
	
	public static void entityMovePacket(int id, Vec3 pos, Vec3 move, float pitch, float yaw) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		Entity e = world.getEntity(id);
		if (e != null) {
			e.setPos(pos);
			e.setDeltaMovement(move);
			e.setXRot(pitch);
			e.setYRot(yaw);
		}
	}
	
	public static void pingsPacket(int id, List<RadarPing> pings) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		EntityAbstractAircraft plane = (EntityAbstractAircraft) world.getEntity(id);
		if (plane != null) {
			RadarData radar = plane.getRadar();
			radar.readClientPingsFromServer(pings);
		}
	}
	
}
