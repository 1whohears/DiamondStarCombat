package com.onewhohears.dscombat.data.vehicle;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeaLevels {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String, SeaLevelData> map = new HashMap<>();

    public static double getAirPressure(ResourceKey<Level> dimension, double posY) {
        String dimId = dimension.location().toString();
        if (!map.containsKey(dimId)) return 0.8;
        return map.get(dimId).getAirPressure(posY);
    }

    public static void readConfig() {
        LOGGER.info("SeaLevels READ CONFIG");
        map.clear();
        List<? extends String> list =  Config.COMMON.dimensionSeaLevels.get();
        for (String level : list) {
            String[] data = level.split("!");
            String dimension = data[0];
            int sea_level = Integer.parseInt(data[1]);
            int space_level = Integer.parseInt(data[2]);
            map.put(dimension, new SeaLevelData(sea_level, space_level));
        }
    }

    public static class SeaLevelData {
        public final int sea_level, space_level;
        public SeaLevelData(int sea_level, int space_level) {
            this.sea_level = sea_level;
            this.space_level = space_level;
        }
        public double getAirPressure(double posY) {
            double scale = 1.0;
            double exp = 2.0;
            if (posY <= sea_level) {
                return scale;
            } else if (posY > space_level) {
                return 0.0;
            } else {
                posY -= sea_level;
                return Math.pow(Math.abs(posY - space_level), exp) * Math.pow(space_level, -exp);
            }
        }
    }

}
