package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.tacview.common.core.EntityKeyframe;
import com.onewhohears.tacview.common.core.KeyframeValue;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class WeaponKeyframe<E extends EntityWeapon> extends EntityKeyframe<E> {

    public final KeyframeValue.IntV<E> age = registerIntValue("age", EntityWeapon::getAge, EntityWeapon::setAge);

    protected WeaponKeyframe() {
        super();
    }

    public WeaponKeyframe(@NotNull E entity) {
        super(entity);
    }

    public WeaponKeyframe(@NotNull JsonObject data) {
        super(data);
    }

    public static class Generic extends WeaponKeyframe<EntityWeapon> {
        protected Generic() {
            super();
        }
        public Generic(@NotNull EntityWeapon entity) {
            super(entity);
        }
        public Generic(@NotNull JsonObject data) {
            super(data);
        }
    }

    public static class Missile extends WeaponKeyframe<EntityMissile<?>> {
        public final KeyframeValue.Vec3V<EntityMissile<?>> targetPos = registerVec3Value("targetPos",
                entity -> entity.targetPos != null ? entity.targetPos : Vec3.ZERO,
                (entity, value) -> entity.targetPos = value);
        protected Missile() {
            super();
        }
        public Missile(@NotNull EntityMissile<?> entity) {
            super(entity);
        }
        public Missile(@NotNull JsonObject data) {
            super(data);
        }
    }
}
