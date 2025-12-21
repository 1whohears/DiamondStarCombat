package com.onewhohears.dscombat.data.replay;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityRecorder<K extends EntityKeyframe<E>, E extends Entity> {

    private final List<K> keyframes = new ArrayList<>();

}
