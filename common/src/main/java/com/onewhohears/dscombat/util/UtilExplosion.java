package com.onewhohears.dscombat.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UtilExplosion {

    /**
     * Creates an explosion with separate damage radius.
     * explosionRadius controls block destruction, explosionDamageRadius controls entity damage.
     * 
     * @param explosionRadius Radius for block destruction and visual effects
     * @param explosionDamageRadius Radius for entity damage (can be larger than explosionRadius)
     * @param damageMultiplier Multiplier for damage amount (1.0 = normal, 0.5 = half damage, 2.0 = double damage)
     */
    public static Explosion createExplosionWithDamageRadius(
            Level level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            double x, double y, double z,
            float explosionRadius,
            float explosionDamageRadius,
            float damageMultiplier,
            boolean fire,
            Level.ExplosionInteraction interaction) {
        
        // CRITICAL: Check if chunks are loaded before creating explosion
        // This prevents server freeze when explosions occur outside loaded chunks
        net.minecraft.world.level.ChunkPos chunkPos = new net.minecraft.world.level.ChunkPos(
            new net.minecraft.core.BlockPos((int)x, (int)y, (int)z)
        );
        if (!level.hasChunk(chunkPos.x, chunkPos.z)) {
            // Chunks not loaded - skip explosion to prevent server freeze
            return null;
        }
        
        // Create the vanilla explosion for blocks and visual effects
        Explosion explosion = level.explode(source, damageSource, null, x, y, z, explosionRadius, fire, interaction);
        
        // If damage radius is same or smaller, use vanilla behavior only
        if (explosionDamageRadius <= explosionRadius) {
            return explosion;
        }
        
        // Apply damage in the larger radius
        applyExtendedDamage(level, source, damageSource, x, y, z, explosionRadius, explosionDamageRadius, damageMultiplier);
        
        return explosion;
    }

    /**
     * Applies damage to entities in extended radius beyond the explosion radius
     */
    private static void applyExtendedDamage(Level level, @Nullable Entity source, 
                                           @Nullable DamageSource damageSource,
                                           double x, double y, double z, 
                                           float explosionRadius, float damageRadius,
                                           float damageMultiplier) {
        
        // Find all entities in damage radius
        AABB bounds = new AABB(x - damageRadius, y - damageRadius, z - damageRadius, 
                               x + damageRadius, y + damageRadius, z + damageRadius);
        List<Entity> entities = level.getEntities(source, bounds);
        
        Vec3 explosionPos = new Vec3(x, y, z);
        double damageDiameter = damageRadius * 2.0;
        
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (entity == source) continue;
            
            // Calculate distance
            Vec3 entityPos = entity.position();
            double distance = entityPos.distanceTo(explosionPos);
            
            // Skip if outside damage radius
            if (distance > damageRadius) continue;
            
            // Skip if already in vanilla explosion radius (vanilla handles it)
            if (distance <= explosionRadius) continue;
            
            // Calculate damage based on distance from explosion
            double distCheck = distance / damageDiameter;
            
            // Use reduced seen percent for extended damage (0.7 = 70% visibility minimum)
            double seenPercent = Math.max(0.7, Explosion.getSeenPercent(explosionPos, entity));
            double expFactor = (1.0 - distCheck) * seenPercent;
            
            float damage = (float)((int)((expFactor * expFactor + expFactor) * 3.5 * damageDiameter + 1.0));
            
            // Apply damage multiplier
            damage *= damageMultiplier;
            
            if (damage > 0.5f && damageSource != null) {
                living.hurt(damageSource, damage);
            }
        }
    }
}
