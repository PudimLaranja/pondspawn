package com.origin.pondspawn.entity.custum;

import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.enums.TargetTypes;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.entity.util.PlayerPhysicsHandler;
import com.origin.pondspawn.init.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;


public class Tongue extends Entity {

    private static final Logger log = LogManager.getLogger(Tongue.class);

    public static final double TONGUE_LENGTH = 15.0;

    public boolean initialized = false;
    public boolean retracting = false;

    private double lockLength = TONGUE_LENGTH;

    public TongueModes tongueMode = TongueModes.LOOSE;

    private static final TrackedData<Integer> TARGET_TYPE = DataTracker
            .registerData(Tongue.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Optional<UUID>> ENTITY_TARGET_UUID = DataTracker
            .registerData(Tongue.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private static final TrackedData<Float> ANIMATION_CONTROLLER = DataTracker
            .registerData(Tongue.class, TrackedDataHandlerRegistry.FLOAT);

    public TargetTypes TargetMode = TargetTypes.DEFAULT;
    public UUID FollowEntity;

    public Vec3d positionTarget = new Vec3d(0, 0, 0);

    private static final String DATA_KEY = "data";
    public NbtCompound data = new NbtCompound();

    private Runnable onRetractedCallback;

    public Tongue(EntityType<?> type, World world) {
        super(type, world);

        this.ignoreCameraFrustum = true;

    }

    public void finish() {
        this.initialized = true;
    }

    public void retract(Runnable callback) {
        this.onRetractedCallback = callback;
        this.retracting = true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(TARGET_TYPE, TargetTypes.DEFAULT.ordinal());
        builder.add(ENTITY_TARGET_UUID, Optional.empty());
        builder.add(ANIMATION_CONTROLLER,0f);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("TongueMode")) {
            this.setMode(TargetTypes.valueOf(nbt.getString("TongueMode")));
        }
        if (nbt.contains("EntityTargetUuid")) {
            this.setEntityTarget(nbt.getUuid("EntityTargetUuid"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString("TongueMode", this.getMode().name());
        if (this.getEntityTarget() != null) {
            nbt.putUuid("EntityTargetUuid", this.getEntityTarget());
        }
    }


    @Override
    public void tick() {
        if (!initialized) return;
        super.tick();

        this.follow_entity();
        this.distance_kill_handler();
        this.retractHandler();
        this.extendHandler();
    }

    private void distance_kill_handler() {
//        UUID target = this.getEntityTarget();
//        if (this.getWorld() instanceof ServerWorld world && target != null) {
//            if (world.getEntity(target) instanceof PlayerEntity player) {
//                Tongue playerTongue = ((PlayerWithTongueData) player).pondspawn$getTongueEntity();
//                if (playerTongue != null) {
//                    if (playerTongue.getUuid() != this.getUuid()) this.kill();
//                } else {
//                    this.kill();
//                }
//                if (tongueMode == TongueModes.PULL) {
//                    double dist = player.getPos().add(0,1,0).subtract(this.getPos()).length();
//                    if (dist < 1.5) {
//                        ModComponents.TONGUE.get(player).setTongueUuid(null);
//                        ((PlayerWithTongueData) player).pondspawn$setTongueEntity(null);
//                        this.kill();
//                    }
//                }
//            } else {
//                this.kill();
//            }
//
//        }
    }

    private void retractHandler() {
        if (retracting) {
            float animationControl = getAnimationController();

            setAnimationController(
                    MathHelper.lerp(0.4f,animationControl,0.0f)
            );

            if (getAnimationController() < 0.1) {
                if (this.onRetractedCallback != null) {
                    this.onRetractedCallback.run();
                }
                this.kill();
            }
        }
    }

    private void extendHandler() {
        if (!retracting) {
            float animationControl = getAnimationController();

            animationControl += 0.4f;
            animationControl = MathHelper.clamp(animationControl,0.0f,1.0f);

            setAnimationController(animationControl);
        }
    }

    private void follow_entity() {

        if (TargetMode != TargetTypes.ENTITY) {
            return;
        }

        if (this.getWorld() instanceof ServerWorld world && FollowEntity != null) {
            Entity entity = world.getEntity(FollowEntity);
            if (entity == null) return;
            Vec3d pos = entity.getEyePos();

            this.setPosition(pos);

        }
    }

    public TargetTypes getMode() {
        int ordinal = this.dataTracker.get(TARGET_TYPE);
        if (ordinal >= 0 && ordinal < TargetTypes.values().length) {
            return TargetTypes.values()[ordinal];
        }
        return TargetTypes.DEFAULT;
    }

    public void setMode(TargetTypes mode) {
        this.dataTracker.set(TARGET_TYPE, mode.ordinal());
    }

    public UUID getEntityTarget() {
        return this.dataTracker.get(ENTITY_TARGET_UUID).orElse(null);
    }

    public void setEntityTarget(@Nullable UUID uuid) {
        this.dataTracker.set(ENTITY_TARGET_UUID, Optional.ofNullable(uuid));
    }

    public void setAnimationController(float value) {
        this.dataTracker.set(ANIMATION_CONTROLLER,value);
    }

    public float getAnimationController() {
        return this.dataTracker.get(ANIMATION_CONTROLLER);
    }

    public void setPositionTarget(Vec3d positionTarget) {
        this.positionTarget = positionTarget;

    }

    public void setTongueMode(TongueModes tongueMode) {
        messagePlayer(tongueMode.asString());
        if (tongueMode == TongueModes.LOCK) {
            if (this.getWorld() instanceof ServerWorld world) {
                if (world.getEntity(this.getEntityTarget()) instanceof  PlayerEntity player) {
                    this.setLockLength(
                            this.getPos().subtract(player.getEyePos()).length()
                    );
                }
            }
        }

        this.tongueMode = tongueMode;
    }

    public TongueModes getTongueMode() {
        return tongueMode;
    }

    public void setLockLength(double lockLength) {
        this.lockLength = MathHelper.clamp(lockLength,0,TONGUE_LENGTH);
    }

    public double getLockLength() {
        return this.lockLength;
    }

    private  void messagePlayer(String message) {
        if (this.getWorld() instanceof ServerWorld world) {
           if (world.getEntity(this.getEntityTarget()) instanceof PlayerEntity player) {
               player.sendMessage(Text.literal(message));
           }
        }
    }


}