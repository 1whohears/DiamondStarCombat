package com.onewhohears.dscombat.data.weapon;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WeaponShootParameters {
	
	public final Level level;
	public final Entity owner;
	public final Vec3 launchPos, direction;
	public final EntityVehicle vehicle;
	public final boolean ignoreRecoil;
    public final boolean isTurret;
    public final boolean isPlayer;
    public final WeaponTargetParameters targetParams;
	
	public WeaponShootParameters(Level level, Entity owner, Vec3 launchPos, Vec3 direction,
                                 @Nullable EntityVehicle vehicle, boolean ignoreRecoil, boolean isTurret,
                                 WeaponTargetParameters targetParams) {
		this.level = level;
		this.owner = owner;
        this.launchPos = launchPos;
		this.direction = direction;
		this.vehicle = vehicle;
		this.ignoreRecoil = ignoreRecoil;
		this.isTurret = isTurret;
		this.isPlayer = owner instanceof Player;
        this.targetParams = targetParams;
    }
	
}
