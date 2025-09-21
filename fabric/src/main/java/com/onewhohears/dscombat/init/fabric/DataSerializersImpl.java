package com.onewhohears.dscombat.init.fabric;

import net.minecraft.network.syncher.EntityDataSerializer;

public class DataSerializersImpl {

    public static <T> EntityDataSerializer<T> register(String id, EntityDataSerializer<T> serializer) {
        return serializer;
    }

}
