package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> VEHICLE_ROAD_KILL = registerDamageType("vehicle_road_kill");
    public static final ResourceKey<DamageType> VEHICLE_FALL = registerDamageType("vehicle_fall");
    public static final ResourceKey<DamageType> VEHICLE_COLLIDE = registerDamageType("vehicle_collide");

    public static final ResourceKey<DamageType> BULLET = registerDamageType("bullet");
    public static final ResourceKey<DamageType> BULLET_EXPLODE = registerDamageType("bullet_explode");
    public static final ResourceKey<DamageType> BOMB = registerDamageType("bomb");
    public static final ResourceKey<DamageType> MISSILE_CONTACT = registerDamageType("missile_contact");
    public static final ResourceKey<DamageType> MISSILE = registerDamageType("missile");
    public static final ResourceKey<DamageType> TORPEDO = registerDamageType("torpedo");
    public static final ResourceKey<DamageType> IR_MISSILE = registerDamageType("ir_missile");

    public static ResourceKey<DamageType> registerDamageType(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.tryBuild(DSCombatMod.MODID, id));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        bootstrapRegister(context, VEHICLE_ROAD_KILL);
        bootstrapRegister(context, VEHICLE_FALL);
        bootstrapRegister(context, VEHICLE_COLLIDE);
        bootstrapRegister(context, BULLET);
        bootstrapRegister(context, BULLET_EXPLODE);
        bootstrapRegister(context, BOMB);
        bootstrapRegister(context, MISSILE_CONTACT);
        bootstrapRegister(context, MISSILE);
        bootstrapRegister(context, TORPEDO);
        bootstrapRegister(context, IR_MISSILE);
    }

    private static void bootstrapRegister(BootstapContext<DamageType> context, ResourceKey<DamageType> type) {
        context.register(type, new DamageType(type.location().getPath(), 0));
    }


    public static Holder<DamageType> getDamageTypeHolder(Level level, ResourceKey<DamageType> key) {
        return level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(key)
                .orElseThrow(() -> new IllegalStateException("Missing damage type: " + key.location()));
    }
}
