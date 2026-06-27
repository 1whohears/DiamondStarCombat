package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.mixin.CameraAccess;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class ClientCameraEventHandlers {

    private static Entity prevGimbal;
    @Nullable
    static private QuaternionF prevQ;
    private static boolean wasTrackingTarget = false;

    // Тряска камеры для наземной техники
    private static float groundShakePitch = 0f;
    private static float groundShakeRoll  = 0f;
    private static int   groundShakeTick = 0;

    public static final CameraAngles CAMERA_ANGLES = new CameraAngles();

    public static void onSetupCameraAngles(Camera camera, float pt, CameraAngles angles) {
        Minecraft m = Minecraft.getInstance();
        final var player = m.player;
        if (player == null) return;
        if (!player.isPassenger()) {
            if (isCameraEntityEqual(m, prevGimbal)) m.setCameraEntity(player);
            prevGimbal = null;
            return;
        }
        if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
        boolean detached = !m.options.getCameraType().isFirstPerson();
        boolean mirrored = m.options.getCameraType().isMirrored();
        float camYOffset = 0;
        boolean isPilot = false, isCopilot = false;
        if (player.getVehicle() instanceof EntityRidablePart seat) {
            isPilot = seat.isPilotSeat();
            isCopilot = seat.isCoPilotSeat();
            if (DSCClientInputs.isGimbalMode()) camYOffset = seat.getCameraYOffset();
            // Note: Turret rotation limits are handled server-side in EntityTurret.rotateTowards()
            // We don't restrict camera here to allow smooth aiming
        }
        if ((prevGimbal != null && prevGimbal.isRemoved())
                || (!DSCClientInputs.isGimbalMode() && !isCameraEntityEqual(m, player))) {
            m.setCameraEntity(player);
            prevGimbal = null;
        }
        if (DSCClientInputs.isGimbalMode() && (isPilot || isCopilot || camYOffset == 0)
                && vehicle.getGimbalForPilotCamera() != null) {
            EntityGimbal gimbal = vehicle.getGimbalForPilotCamera();
            if (!isCameraEntityEqual(m, gimbal)) m.setCameraEntity(gimbal);
            gimbal.setXRot(player.getViewXRot(pt));
            gimbal.setYRot(player.getViewYRot(pt));
            prevGimbal = gimbal;
            camYOffset = -0.2f;
        }
        if (isPilot) {
            boolean resetMousePressed = ClientInputManager.RESET_MOUSE.isPressed();
            RadarStats.RadarPing target = vehicle.radarSystem.getClientSelectedPing();
            Entity camEntity = m.getCameraEntity();
            if (DSCClientInputs.isCameraTrackTarget() && target != null && !resetMousePressed && camEntity != null) {
                Vec3 diff = target.pos.subtract(camEntity.getEyePosition(pt));
                float x = UtilAngles.getPitch(diff);
                float y = UtilAngles.getYaw(diff);
                setAngles(angles, player, x, y, mirrored);
                wasTrackingTarget = true;
            } else if (DSCClientInputs.isCameraLockedForward() && !DSCClientInputs.isLookAround()) {
                lookForward(angles, player, mirrored, pt, vehicle);
                wasTrackingTarget = false;
            } else if (DSCClientInputs.isCameraFreeRelative()
                    || (DSCClientInputs.isLookAround() && DSCClientInputs.isMouseDirect())) {
                QuaternionF qPT = vehicle.getClientQ(pt);
                if (resetMousePressed) {
                    lookForward(angles, player, mirrored, pt, vehicle);
                } else if (prevQ != null) {
                    float[] relativeAngles;
                    if (wasTrackingTarget) {
                        relativeAngles = new float[] {DSCClientInputs.xRotPreTrack, DSCClientInputs.yRotPreTrack};
                    } else {
                        relativeAngles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), prevQ);
                    }
                    float[] globalAngles = UtilAngles.relativeToGlobalDegrees(relativeAngles[0], relativeAngles[1], qPT);
                    float x = globalAngles[0];
                    float y = globalAngles[1];
                    setAngles(angles, player, x, y, mirrored);
                    DSCClientInputs.xRotPreTrack = relativeAngles[0];
                    DSCClientInputs.yRotPreTrack = relativeAngles[1];
                }
                prevQ = qPT;
                wasTrackingTarget = false;
            }
        } else {
            // For non-pilot passengers: use player's current view angles
            // This allows free camera rotation while maintaining vehicle roll
            float pitch = player.getViewXRot(pt);
            float yaw = player.getViewYRot(pt);
            
            // Handle mirrored camera (F5 third person front view)
            if (mirrored) {
                pitch *= -1;
                yaw += 180;
            }
            
            angles.setPitch(pitch);
            angles.setYaw(yaw);
        }
        // Roll: turrets have no roll (vehicle roll would look unnatural for a fixed gun seat)
        boolean isTurretSeat = player.getVehicle() instanceof EntityTurret;
        float zi = isTurretSeat ? 0f : UtilAngles.lerpAngle(pt, vehicle.zRotO, vehicle.zRot);
        if (detached && mirrored) zi *= -1;
        angles.setRoll(zi);
        double camDist = vehicle.getStats().cameraDistance;
        if (detached && isPilot && camDist > 4) {
            double vehicleCamDist = Math.min(0, 4-getMaxDist(camera, player, camDist));
            ((CameraAccess)camera).invokeSetRotation(angles.getYaw(), angles.getPitch());
            camera.move(vehicleCamDist, 0, 0);
        }
        QuaternionF q = null;
        if (camYOffset != 0) {
            q = UtilAngles.lerpQ(pt, vehicle.getPrevQ(), vehicle.getClientQ());
            Vec3 yawAxis = UtilAngles.getYawAxis(q);
            camera.setPosition(camera.getPosition().add(yawAxis.scale(camYOffset)));
        }
        if (DSCClientInputs.getLeanAmount() != 0) {
            if (q == null) q = UtilAngles.lerpQ(pt, vehicle.getPrevQ(), vehicle.getClientQ());
            Vec3 pitchAxis = UtilAngles.getPitchAxis(q);
            camera.setPosition(camera.getPosition().add(pitchAxis.scale(-DSCClientInputs.getLeanAmount())));
        }

        // --- Тряска камеры для наземной техники ---
        if (vehicle.getVehicleType() == com.onewhohears.dscombat.data.vehicle.VehicleType.CAR
                && vehicle.isOnGround() && vehicle.isOperational()) {
            Vec3 vel = vehicle.getDeltaMovement();
            float speed = (float) Math.sqrt(vel.x * vel.x + vel.z * vel.z);
            float maxSpeed = (float) vehicle.getMaxSpeedForMotion();
            float speedRatio = maxSpeed > 0 ? Math.min(speed / maxSpeed, 1f) : 0f;

            // Амплитуда мягкая — максимум 0.45 градуса
            float amp = speedRatio * 0.45f;

            groundShakeTick++;

            // Медленные синусоиды — период pitch ~1.2 сек, roll ~2 сек
            float pitchTarget = (float) Math.sin(groundShakeTick * 0.26) * amp;
            float rollTarget  = (float) Math.sin(groundShakeTick * 0.16 + 0.9) * amp * 0.5f;

            // Очень сильное сглаживание — lerp с коэффициентом 0.06
            groundShakePitch += (pitchTarget - groundShakePitch) * 0.06f;
            groundShakeRoll  += (rollTarget  - groundShakeRoll)  * 0.06f;

            angles.setPitch(angles.getPitch() + groundShakePitch);
            angles.setRoll(angles.getRoll()   + groundShakeRoll);
        } else {
            // Плавное затухание
            groundShakePitch *= 0.85f;
            groundShakeRoll  *= 0.85f;
            groundShakeTick = 0;
        }
    }

    private static boolean isCameraEntityEqual(Minecraft m, @Nullable Entity e) {
        if (e == null || m.getCameraEntity() == null) return false;
        return m.getCameraEntity().equals(e);
    }

    private static void lookForward(CameraAngles angles, Player player,
                                    boolean mirrored, float pt, EntityVehicle vehicle) {
        float x = UtilAngles.lerpAngle(pt, vehicle.xRotO, vehicle.getXRot());
        float y = UtilAngles.lerpAngle180(pt, vehicle.yRotO, vehicle.getYRot());
        setAngles(angles, player, x, y, mirrored);
        DSCClientInputs.xRotPreTrack = x;
        DSCClientInputs.yRotPreTrack = y;
    }

    private static void setAngles(CameraAngles angles, Player player, float x, float y, boolean mirrored) {
        player.setXRot(x);
        player.xRotO = x;
        player.setYRot(y);
        player.yRotO = y;
        if (mirrored) {
            x *= -1;
            y += 180;
        }
        angles.setPitch(x);
        angles.setYaw(y);
    }

    public static double getMaxDist(Camera cam, Player player, double dist) {
        Vec3 from = cam.getPosition();
        Vec3f d = Vec3f.from(cam.getLookVector());
        d.mul((float)-dist);
        Vec3 to = from.add(d.x(), d.y(), d.z());
        HitResult hitresult = UtilEntity.getLevel(player).clip(new ClipContext(from, to,
                ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
        if (hitresult.getType() != HitResult.Type.MISS) {
            double d0 = hitresult.getLocation().distanceTo(from);
            if (d0 < dist) dist = d0;
        }
        return dist;
    }

    public static void clientTickSetMouseCallback(Minecraft m) {
        if (m.player == null || m.player.tickCount != 1) return;
        // Always install our callback (handles both FREE_RELATIVE rotation and MOUSE_DIRECT)
        GLFW.glfwSetCursorPosCallback(m.getWindow().getWindow(), customMouseCallback);
    }

    public static final GLFWCursorPosCallbackI customMouseCallback = (window, x, y) -> {
        Minecraft m = Minecraft.getInstance();
        m.execute(() -> {
            double xn = x, yn = y;
            if (window != m.getWindow().getWindow()) return;
            if (DSCClientInputs.isMouseDirect()
                    && m.player != null && m.screen == null
                    && m.player.getRootVehicle() instanceof EntityVehicle) {
                if (DSCClientInputs.isLookAround()) {
                    // Shift held: free camera, pass mouse movement normally
                    m.mouseHandler.onMove(window, x, y);
                    return;
                }
                // Accumulate raw delta for MOUSE_DIRECT; don't rotate camera
                double dx = x - m.mouseHandler.xpos();
                double dy = y - m.mouseHandler.ypos();
                DSCClientInputs.accumulateMouseDelta(dx, dy);
                // Keep cursor locked at current position so Minecraft sees no movement
                GLFW.glfwSetCursorPos(window, m.mouseHandler.xpos(), m.mouseHandler.ypos());
                m.mouseHandler.onMove(window, m.mouseHandler.xpos(), m.mouseHandler.ypos());
                return;
            }
            if (Config.CLIENT.cameraTurnRelativeToVehicle.get()
                    && DSCClientInputs.isCameraFree()
                    && m.player != null && m.screen == null
                    && m.player.getRootVehicle() instanceof EntityVehicle craft) {
                double r = Math.toRadians(craft.zRot);
                double dx = x - m.mouseHandler.xpos();
                double dy = y - m.mouseHandler.ypos();
                double cosR = Math.cos(r), sinR = Math.sin(r);
                xn = dx*cosR - dy*sinR + m.mouseHandler.xpos();
                yn = dy*cosR + dx*sinR + m.mouseHandler.ypos();
                GLFW.glfwSetCursorPos(window, xn, yn);
            }
            m.mouseHandler.onMove(window, xn, yn);
        });
    };

    public static float getTurretZoom() {
        Minecraft m = Minecraft.getInstance();
        if (m.player == null) return 1.0f;
        Entity vehicle = m.player.getVehicle();
        if (vehicle instanceof EntityTurret turret) {
            if (DSCClientInputs.isZoomIn()) {
                float zoom = turret.getStats().getZoom();
                // Only apply zoom if it's explicitly set (not default 1.0f)
                if (zoom != 1.0f) {
                    return zoom;
                }
            }
        }
        return 1.0f;
    }

}
