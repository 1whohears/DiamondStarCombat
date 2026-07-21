package com.onewhohears.dscombat.entity.ai.nav;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * yes this is a modified version of mojang's PathNavigation class.
 */
public abstract class VehicleNavigation {

    private static final int MAX_TIME_RECOMPUTE = 20;
    private static final int STUCK_CHECK_INTERVAL = 100;
    private static final float STUCK_THRESHOLD_DISTANCE_FACTOR = 0.25F;
    public static final int DEFAULT_VEHICLE_FOLLOW_RANGE = 64;

    protected final EntityVehicle vehicle;
    protected final Level level;
    protected @Nullable Path path;
    protected int tick;
    protected int lastStuckCheck;
    protected Vec3 lastStuckCheckPos;
    protected Vec3i timeoutCachedNode;
    protected long timeoutTimer;
    protected long lastTimeoutCheck;
    protected double timeoutLimit;
    protected float maxDistanceToWaypoint;
    protected boolean hasDelayedRecomputation;
    protected long timeLastRecompute;
    protected VehicleNodeEvaluator nodeEvaluator;
    private @Nullable BlockPos targetPos;
    private int reachRange;
    private float maxVisitedNodesMultiplier;
    private final VehiclePathFinder pathFinder;
    private boolean isStuck;

    public VehicleNavigation(EntityVehicle vehicle, Level level) {
        this.lastStuckCheckPos = Vec3.ZERO;
        this.timeoutCachedNode = Vec3i.ZERO;
        this.maxDistanceToWaypoint = 0.5F;
        this.maxVisitedNodesMultiplier = 1.0F;
        this.vehicle = vehicle;
        this.level = level;
        this.pathFinder = this.createPathFinder(DEFAULT_VEHICLE_FOLLOW_RANGE * 16);
    }

    public void resetMaxVisitedNodesMultiplier() {
        this.maxVisitedNodesMultiplier = 1.0F;
    }

    public void setMaxVisitedNodesMultiplier(float f) {
        this.maxVisitedNodesMultiplier = f;
    }

    @Nullable
    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    protected abstract VehiclePathFinder createPathFinder(int i);

    public void recomputePath() {
        if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
            if (this.targetPos != null) {
                this.path = null;
                this.path = this.createPath(this.targetPos, this.reachRange);
                this.timeLastRecompute = this.level.getGameTime();
                this.hasDelayedRecomputation = false;
            }
        } else {
            this.hasDelayedRecomputation = true;
        }

    }

    @Nullable
    public final Path createPath(double d, double e, double f, int i) {
        return this.createPath(BlockPos.containing(d, e, f), i);
    }

    @Nullable
    public Path createPath(Stream<BlockPos> stream, int i) {
        return this.createPath(stream.collect(Collectors.toSet()), 8, false, i);
    }

    @Nullable
    public Path createPath(Set<BlockPos> set, int i) {
        return this.createPath(set, 8, false, i);
    }

    @Nullable
    public Path createPath(BlockPos arg, int i) {
        return this.createPath(ImmutableSet.of(arg), 8, false, i);
    }

    @Nullable
    public Path createPath(BlockPos arg, int i, int j) {
        return this.createPath(ImmutableSet.of(arg), 8, false, i, (float)j);
    }

    @Nullable
    public Path createPath(Entity arg, int i) {
        return this.createPath(ImmutableSet.of(arg.blockPosition()), 16, true, i);
    }

    @Nullable
    protected Path createPath(Set<BlockPos> set, int i, boolean bl, int j) {
        return this.createPath(set, i, bl, j, DEFAULT_VEHICLE_FOLLOW_RANGE);
    }

    @Nullable
    protected Path createPath(Set<BlockPos> set, int j, boolean bl, int k, float f) {
        if (set.isEmpty()) {
            return null;
        } else if (this.vehicle.getY() < (double)this.level.getMinBuildHeight()) {
            return null;
        } else if (!this.canUpdatePath()) {
            return null;
        } else if (this.path != null && !this.path.isDone() && set.contains(this.targetPos)) {
            return this.path;
        } else {
            this.level.getProfiler().push("pathfind");
            BlockPos blockpos = bl ? this.vehicle.blockPosition().above() : this.vehicle.blockPosition();
            int i = (int)(f + (float)j);
            PathNavigationRegion pathnavigationregion = new PathNavigationRegion(this.level, blockpos.offset(-i, -i, -i), blockpos.offset(i, i, i));
            Path path = this.pathFinder.findPath(pathnavigationregion, this.vehicle, set, f, k, this.maxVisitedNodesMultiplier);
            this.level.getProfiler().pop();
            if (path != null && path.getTarget() != null) {
                this.targetPos = path.getTarget();
                this.reachRange = k;
                this.resetStuckTimeout();
            }

            return path;
        }
    }

    public boolean moveTo(double d, double e, double f) {
        return this.moveTo(this.createPath(d, e, f, 1));
    }

    public boolean moveTo(Entity arg) {
        Path path = this.createPath(arg, 1);
        return path != null && this.moveTo(path);
    }

    public boolean moveTo(@Nullable Path arg) {
        if (arg == null) {
            this.path = null;
            return false;
        } else {
            if (!arg.sameAs(this.path)) {
                this.path = arg;
            }

            if (this.isDone()) {
                return false;
            } else {
                this.trimPath();
                if (this.path.getNodeCount() <= 0) {
                    return false;
                } else {
                    Vec3 vec3 = this.getTempMobPos();
                    this.lastStuckCheck = this.tick;
                    this.lastStuckCheckPos = vec3;
                    return true;
                }
            }
        }
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                Vec3 vec3 = this.getTempMobPos();
                Vec3 vec31 = this.path.getNextEntityPos(this.vehicle);
                if (vec3.y > vec31.y && !this.vehicle.onGround() && Mth.floor(vec3.x) == Mth.floor(vec31.x) && Mth.floor(vec3.z) == Mth.floor(vec31.z)) {
                    this.path.advance();
                }
            }

            //DebugPackets.sendPathFindingPacket(this.level, this.vehicle, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                Vec3 vec32 = this.path.getNextEntityPos(this.vehicle);
                this.vehicle.pilotAiMoveControl.setWantedPosition(vec32.x, this.getGroundY(vec32), vec32.z);
            }
        }

    }

    protected double getGroundY(Vec3 arg) {
        BlockPos blockpos = BlockPos.containing(arg);
        return this.level.getBlockState(blockpos.below()).isAir() ? arg.y : WalkNodeEvaluator.getFloorLevel(this.level, blockpos);
    }

    protected void followThePath() {
        Vec3 vec3 = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.vehicle.getBbWidth() > 0.75F ? this.vehicle.getBbWidth() / 2.0F : 0.75F - this.vehicle.getBbWidth() / 2.0F;
        Vec3i vec3i = this.path.getNextNodePos();
        double d0 = Math.abs(this.vehicle.getX() - ((double)vec3i.getX() + (double)(this.vehicle.getBbWidth() + 1.0F) / (double)2.0F));
        double d1 = Math.abs(this.vehicle.getY() - (double)vec3i.getY());
        double d2 = Math.abs(this.vehicle.getZ() - ((double)vec3i.getZ() + (double)(this.vehicle.getBbWidth() + 1.0F) / (double)2.0F));
        boolean flag = d0 <= (double)this.maxDistanceToWaypoint && d2 <= (double)this.maxDistanceToWaypoint && d1 < (double)1.0F;
        if (flag || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(vec3)) {
            this.path.advance();
        }

        this.doStuckDetection(vec3);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 arg) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vec3 vec3 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if (!arg.closerThan(vec3, (double)2.0F)) {
                return false;
            } else if (this.canMoveDirectly(arg, this.path.getNextEntityPos(this.vehicle))) {
                return true;
            } else {
                Vec3 vec31 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 vec32 = vec3.subtract(arg);
                Vec3 vec33 = vec31.subtract(arg);
                double d0 = vec32.lengthSqr();
                double d1 = vec33.lengthSqr();
                boolean flag = d1 < d0;
                boolean flag1 = d0 < (double)0.5F;
                if (!flag && !flag1) {
                    return false;
                } else {
                    Vec3 vec34 = vec32.normalize();
                    Vec3 vec35 = vec33.normalize();
                    return vec35.dot(vec34) < (double)0.0F;
                }
            }
        }
    }

    protected void doStuckDetection(Vec3 arg) {
        if (this.tick - this.lastStuckCheck > 100) {
            float f = this.vehicle.getAiSpeed() >= 1.0F ? this.vehicle.getAiSpeed() : this.vehicle.getAiSpeed() * this.vehicle.getAiSpeed();
            float f1 = f * 100.0F * 0.25F;
            if (arg.distanceToSqr(this.lastStuckCheckPos) < (double)(f1 * f1)) {
                this.isStuck = true;
                this.stop();
            } else {
                this.isStuck = false;
            }

            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = arg;
        }

        if (this.path != null && !this.path.isDone()) {
            Vec3i vec3i = this.path.getNextNodePos();
            long i = this.level.getGameTime();
            if (vec3i.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += i - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = vec3i;
                double d0 = arg.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = this.vehicle.getAiSpeed() > 0.0F ? d0 / (double)this.vehicle.getAiSpeed() * (double)20.0F : (double)0.0F;
            }

            if (this.timeoutLimit > (double)0.0F && (double)this.timeoutTimer > this.timeoutLimit * (double)3.0F) {
                this.timeoutPath();
            }

            this.lastTimeoutCheck = i;
        }

    }

    private void timeoutPath() {
        this.resetStuckTimeout();
        this.stop();
    }

    private void resetStuckTimeout() {
        this.timeoutCachedNode = Vec3i.ZERO;
        this.timeoutTimer = 0L;
        this.timeoutLimit = (double)0.0F;
        this.isStuck = false;
    }

    public boolean isDone() {
        return this.path == null || this.path.isDone();
    }

    public boolean isInProgress() {
        return !this.isDone();
    }

    public void stop() {
        this.path = null;
    }

    protected abstract Vec3 getTempMobPos();

    protected abstract boolean canUpdatePath();

    protected boolean isInLiquid() {
        return this.vehicle.isInWaterOrBubble() || this.vehicle.isInLava();
    }

    protected void trimPath() {
        if (this.path != null) {
            for(int i = 0; i < this.path.getNodeCount(); ++i) {
                Node node = this.path.getNode(i);
                Node node1 = i + 1 < this.path.getNodeCount() ? this.path.getNode(i + 1) : null;
                BlockState blockstate = this.level.getBlockState(new BlockPos(node.x, node.y, node.z));
                if (blockstate.is(BlockTags.CAULDRONS)) {
                    this.path.replaceNode(i, node.cloneAndMove(node.x, node.y + 1, node.z));
                    if (node1 != null && node.y >= node1.y) {
                        this.path.replaceNode(i + 1, node.cloneAndMove(node1.x, node.y + 1, node1.z));
                    }
                }
            }
        }

    }

    protected boolean canMoveDirectly(Vec3 arg, Vec3 arg2) {
        return false;
    }

    public boolean canCutCorner(BlockPathTypes arg) {
        return arg != BlockPathTypes.DANGER_FIRE && arg != BlockPathTypes.DANGER_OTHER && arg != BlockPathTypes.WALKABLE_DOOR;
    }

    protected static boolean isClearForMovementBetween(Mob arg, Vec3 arg2, Vec3 arg3, boolean bl) {
        Vec3 vec3 = new Vec3(arg3.x, arg3.y + (double)arg.getBbHeight() * (double)0.5F, arg3.z);
        return arg.level().clip(new ClipContext(arg2, vec3, ClipContext.Block.COLLIDER, bl ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, arg)).getType() == HitResult.Type.MISS;
    }

    public boolean isStableDestination(BlockPos arg) {
        BlockPos blockpos = arg.below();
        return this.level.getBlockState(blockpos).isSolidRender(this.level, blockpos);
    }

    public VehicleNodeEvaluator getNodeEvaluator() {
        return this.nodeEvaluator;
    }

    public void setCanFloat(boolean bl) {
        this.nodeEvaluator.setCanFloat(bl);
    }

    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    public boolean shouldRecomputePath(BlockPos arg) {
        if (this.hasDelayedRecomputation) {
            return false;
        } else if (this.path != null && !this.path.isDone() && this.path.getNodeCount() != 0) {
            Node node = this.path.getEndNode();
            Vec3 vec3 = new Vec3(((double)node.x + this.vehicle.getX()) / (double)2.0F, ((double)node.y + this.vehicle.getY()) / (double)2.0F, ((double)node.z + this.vehicle.getZ()) / (double)2.0F);
            return arg.closerToCenterThan(vec3, (double)(this.path.getNodeCount() - this.path.getNextNodeIndex()));
        } else {
            return false;
        }
    }

    public float getMaxDistanceToWaypoint() {
        return this.maxDistanceToWaypoint;
    }

    public boolean isStuck() {
        return this.isStuck;
    }

}
