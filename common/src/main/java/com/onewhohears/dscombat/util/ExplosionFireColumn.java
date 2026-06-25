package com.onewhohears.dscombat.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Управляет огненными столбами после взрыва техники
 */
public class ExplosionFireColumn {
    
    private static final List<FireColumn> activeColumns = new ArrayList<>();
    private static final Random random = new Random();
    
    /**
     * Создает новый огненный столб
     * @param vehicleId ID техники (-1 если техника уже удалена)
     */
    public static void createFireColumn(Vec3 pos, double radius, int vehicleId) {
        activeColumns.add(new FireColumn(pos, radius, System.currentTimeMillis(), vehicleId));
    }
    
    /**
     * Удаляет огненный столб для конкретной техники
     */
    public static void removeFireColumn(int vehicleId) {
        activeColumns.removeIf(column -> column.vehicleId == vehicleId);
    }
    
    /**
     * Очищает все огненные столбы (вызывается при остановке сервера)
     */
    public static void clearAll() {
        activeColumns.clear();
    }
    
    /**
     * Обновляет все активные огненные столбы (вызывается каждый тик)
     */
    public static void tick(Level level) {
        Iterator<FireColumn> iterator = activeColumns.iterator();
        long currentTime = System.currentTimeMillis();
        
        while (iterator.hasNext()) {
            FireColumn column = iterator.next();
            long elapsed = currentTime - column.startTime;
            
            // Проверяем, существует ли еще техника
            if (column.vehicleId != -1 && level.getEntity(column.vehicleId) == null) {
                iterator.remove();
                continue;
            }
            
            // Удаляем столб после 25 минут (5 мин огонь + 20 мин дым)
            if (elapsed > 1500000) { // 25 минут = 1500000 мс
                iterator.remove();
                continue;
            }
            
            // Первые 5 минут - огонь
            if (elapsed < 300000) { // 5 минут = 300000 мс
                spawnFireParticles(level, column.pos, column.radius, elapsed);
            }
            // Следующие 20 минут - черный дым
            else {
                spawnSmokeParticles(level, column.pos, column.radius, elapsed - 300000);
            }
        }
    }
    
    private static void spawnFireParticles(Level level, Vec3 pos, double radius, long elapsed) {
        double maxHeight = 4; // Фиксированная высота 4 блока
        
        // Высота растет со временем: за первые 10 секунд достигает максимума
        double currentHeight = Math.min(maxHeight, (elapsed / 10000.0) * maxHeight);
        
        // Огненный столб с хаотичным разлетанием
        for (double h = 0; h < currentHeight; h += 1.0) {
            // Основной огонь в центре столба - меньше частиц
            for (int i = 0; i < 2; i++) {
                level.addParticle(ParticleTypes.FLAME,
                    pos.x + random.nextGaussian() * 0.2,
                    pos.y + h,
                    pos.z + random.nextGaussian() * 0.2,
                    random.nextGaussian() * 0.02,
                    0.04 + random.nextDouble() * 0.03,
                    random.nextGaussian() * 0.02);
            }
            
            // Хаотичный огонь, разлетающийся в случайных направлениях
            if (random.nextFloat() < 0.6f) { // 60% шанс на каждом уровне
                double randomAngle = random.nextDouble() * Math.PI * 2; // Случайный угол
                double randomDistance = 0.3 + random.nextDouble() * 1.0; // Случайное расстояние 0.3-1.3 блока
                
                level.addParticle(ParticleTypes.FLAME,
                    pos.x + Math.cos(randomAngle) * randomDistance * 0.5,
                    pos.y + h + random.nextGaussian() * 0.2, // Небольшая вариация по высоте
                    pos.z + Math.sin(randomAngle) * randomDistance * 0.5,
                    Math.cos(randomAngle) * (0.03 + random.nextDouble() * 0.03), // Случайная скорость в стороны
                    0.01 + random.nextDouble() * 0.03,
                    Math.sin(randomAngle) * (0.03 + random.nextDouble() * 0.03));
            }
        }
        
        // Дополнительный яркий огонь на случайной высоте
        if (random.nextFloat() < 0.3f) {
            double sparkHeight = random.nextDouble() * currentHeight;
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                pos.x + random.nextGaussian() * 0.3,
                pos.y + sparkHeight,
                pos.z + random.nextGaussian() * 0.3,
                random.nextGaussian() * 0.02,
                0.06,
                random.nextGaussian() * 0.02);
        }
        
        // Sparks flying in random directions
        if (random.nextFloat() < 0.15f) {
            double sparkAngle = random.nextDouble() * Math.PI * 2;
            double sparkHeight = random.nextDouble() * currentHeight;
            
            level.addParticle(ParticleTypes.LAVA,
                pos.x + random.nextGaussian() * 0.2,
                pos.y + sparkHeight,
                pos.z + random.nextGaussian() * 0.2,
                Math.cos(sparkAngle) * (0.1 + random.nextDouble() * 0.1),
                0.05 + random.nextDouble() * 0.15,
                Math.sin(sparkAngle) * (0.1 + random.nextDouble() * 0.1));
        }
    }
    
    private static void spawnSmokeParticles(Level level, Vec3 pos, double radius, long elapsed) {
        double maxHeight = 4; // Фиксированная высота 4 блока
        
        // Высота растет со временем: за первые 15 секунд достигает максимума
        double currentHeight = Math.min(maxHeight, (elapsed / 15000.0) * maxHeight);
        
        // Только дым от костра - уменьшено количество
        for (double h = 0; h < currentHeight; h += 2.5) { // Увеличен шаг с 1.5 до 2.5
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                pos.x + random.nextGaussian() * radius * 0.2,
                pos.y + h,
                pos.z + random.nextGaussian() * radius * 0.2,
                random.nextGaussian() * 0.02,
                0.15,
                random.nextGaussian() * 0.02);
        }
    }
    
    private static class FireColumn {
        final Vec3 pos;
        final double radius;
        final long startTime;
        final int vehicleId;
        
        FireColumn(Vec3 pos, double radius, long startTime, int vehicleId) {
            this.pos = pos;
            this.radius = radius;
            this.startTime = startTime;
            this.vehicleId = vehicleId;
        }
    }
}
