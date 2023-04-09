package com.onewhohears.dscombat.common.network.toclient;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

/*
 * TODO ToClientRecievePlaneData shouldn't be needed!
 * same with the add/remove radars and weapons
 * these should get updated when item changes in plane menu get sent to client
 * it's unnecessary extra data send from server to client
 */

public class ToClientRecievePlaneData extends IPacket {
	
	public final int id;
	public final int weaponIndex;
	public final List<WeaponData> weapons;
	public final List<PartSlot> slots;
	public final List<RadarData> radars;
	
	public ToClientRecievePlaneData(int id, int weaponIndex, List<WeaponData> weapons, List<PartSlot> slots, List<RadarData> radars) {
		this.id = id;
		this.weaponIndex = weaponIndex;
		this.weapons = weapons;
		this.slots = slots;
		this.radars = radars;
		//System.out.println("packet constructor "+pm);
	}
	
	public ToClientRecievePlaneData(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		weaponIndex = buffer.readInt();
		weapons = WeaponSystem.readWeaponsFromBuffer(buffer);
		slots = PartsManager.readSlotsFromBuffer(buffer);
		radars = RadarSystem.readRadarsFromBuffer(buffer);
		//System.out.println("decoding "+pm);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(weaponIndex);
		WeaponSystem.writeWeaponsToBuffer(buffer, weapons);
		PartsManager.writeSlotsToBuffer(buffer, slots);
		RadarSystem.writeRadarsToBuffer(buffer, radars);
		//System.out.println("encoding "+pm);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.planeDataPacket(id, slots, weapons, weaponIndex, radars);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
