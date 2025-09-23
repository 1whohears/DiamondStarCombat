package com.onewhohears.dscombat.init.fabric;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class DataSerializersImpl {

    public static <T> EntityDataSerializer<T> register(String id, EntityDataSerializer<T> serializer) {
        EntityDataSerializers.registerSerializer(serializer);
        return serializer;
    }

}
