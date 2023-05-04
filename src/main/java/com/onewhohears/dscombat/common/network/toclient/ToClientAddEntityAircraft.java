package com.onewhohears.dscombat.common.network.toclient;

import java.util.List;

import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;

public class ToClientAddEntityAircraft extends ClientboundAddEntityPacket {
	
	public final String preset;
	public final int weaponIndex;
	public final List<WeaponData> weapons;
	public final List<PartSlot> slots;
	public final List<RadarData> radars;
	
	public ToClientAddEntityAircraft(EntityAircraft entity) {
		super(entity);
		this.preset = entity.preset;
		this.weaponIndex = entity.weaponSystem.getSelectedIndex();
		this.weapons = entity.weaponSystem.getWeapons();
		this.slots = entity.partsManager.getSlots();
		this.radars = entity.radarSystem.getRadars();
	}
	
	public ToClientAddEntityAircraft(FriendlyByteBuf buffer) {
		super(buffer);
		preset = buffer.readUtf();
		weaponIndex = buffer.readInt();
		weapons = WeaponSystem.readWeaponsFromBuffer(buffer);
		slots = PartsManager.readSlotsFromBuffer(buffer);
		radars = RadarSystem.readRadarsFromBuffer(buffer);
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(preset);
		buffer.writeInt(weaponIndex);
		WeaponSystem.writeWeaponsToBuffer(buffer, weapons);
		PartsManager.writeSlotsToBuffer(buffer, slots);
		RadarSystem.writeRadarsToBuffer(buffer, radars);
	}

	@Override
	public void handle(ClientGamePacketListener handler) {
		super.handle(handler);
		UtilPacket.planeDataPacket(getId(), preset, slots, weapons, weaponIndex, radars);
	}

}
