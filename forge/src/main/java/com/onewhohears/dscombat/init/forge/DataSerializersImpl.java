package com.onewhohears.dscombat.init.forge;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DataSerializersImpl {

    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, DSCombatMod.MODID);

    public static <T> EntityDataSerializer<T> register(String id, EntityDataSerializer<T> serializer) {
        DATA_SERIALIZERS.register(id, () -> serializer);
        return serializer;
    }

    public static void register(IEventBus eventBus) {
        DATA_SERIALIZERS.register(eventBus);
    }

}
