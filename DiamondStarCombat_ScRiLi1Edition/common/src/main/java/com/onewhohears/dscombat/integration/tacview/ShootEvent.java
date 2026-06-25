package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.tacview.client.core.ClientPlayback;
import com.onewhohears.tacview.common.core.recordevent.EntityRecordEvent;
import org.jetbrains.annotations.NotNull;

public class ShootEvent extends EntityRecordEvent<EntityWeapon> {

    public ShootEvent(@NotNull EntityWeapon weapon) {
        super("weapon_shoot", weapon);
    }

    public ShootEvent(@NotNull JsonObject data) {
        super(data);
    }

    @Override
    protected void onEventPlayback(@NotNull ClientPlayback playback, @NotNull EntityWeapon weapon) {

    }

    @Override
    protected void addSaveData(@NotNull JsonObject jsonObject) {

    }

    @Override
    protected void readSaveData(@NotNull JsonObject jsonObject) {

    }
}
