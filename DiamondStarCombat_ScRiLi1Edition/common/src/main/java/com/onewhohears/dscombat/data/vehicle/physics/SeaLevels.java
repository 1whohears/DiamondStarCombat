package com.onewhohears.dscombat.data.vehicle.physics;

import com.onewhohears.dscombat.data.graph.SeaLevelsGraph;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SeaLevels {

    public static final String STAT_GRAPH_PREFIX = "air_density_";

    public static double getAirPressure(ResourceKey<Level> dimension, double posY) {
        String graphId = STAT_GRAPH_PREFIX+dimension.location().getNamespace()+"_"+dimension.location().getPath();
        SeaLevelsGraph graph = StatGraphs.get().getSeaLevelsGraph(graphId);
        if (graph != null) return graph.getLerpFloat((float)posY);
        return 1;
    }

    public static int getSeaLevel(ResourceKey<Level> dimension) {
        String graphId = STAT_GRAPH_PREFIX+dimension.location().getNamespace()+"_"+dimension.location().getPath();
        SeaLevelsGraph graph = StatGraphs.get().getSeaLevelsGraph(graphId);
        if (graph != null) return graph.getSeaLevel();
        return 0;
    }
}
