package com.origin.pondspawn.mixin;

import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.command.ClearTongue;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.globalconfig.PlayerPhysicsConfig;
import com.origin.pondspawn.init.ModComponents;
import com.origin.pondspawn.weightSystem.WeightManager;
import io.github.apace100.origins.component.PlayerOriginComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerWithTongueData {

    @Unique private Tongue tongueEntity;
    @Unique @Nullable
    private Entity Target = null;

    @Unique
    @Nullable Runnable teleportCallback = null;

    @Override
    public void pondspawn$setTarget(Entity value) {
        this.Target = value;
    }

    @Override
    public Entity pondspawn$getTarget() {
        return this.Target;
    }

    @Override
    public void pondspawn$setTongueEntity(Tongue value) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (value == null) {
            ModComponents.TONGUE.get(player).setTongueUuid(null);
        } else {
            ModComponents.TONGUE.get(player).setTongueUuid(value.getUuid());
        }

        this.tongueEntity = value;
    }

    @Override
    public Tongue pondspawn$getTongueEntity() {
        if (this.tongueEntity != null && !this.tongueEntity.isRemoved()) {
            return this.tongueEntity;
        }

        PlayerEntity player = (PlayerEntity) (Object) this;
        UUID uuid = ModComponents.TONGUE.get(player).getTongueUuid();

        if (uuid == null) return null;

        World world = player.getWorld();

        if (world instanceof ServerWorld serverWorld) {
            Entity entity = serverWorld.getEntity(uuid);
            if (entity instanceof Tongue tongue) {
                this.tongueEntity = tongue;
            }
        }
        else {
            Box searchBox = player.getBoundingBox().expand(64.0);

            List<Tongue> found = world.getEntitiesByType(
                    TypeFilter.instanceOf(Tongue.class),
                    searchBox,
                    entity -> entity.getUuid().equals(uuid)
            );

            if (!found.isEmpty()) {
                this.tongueEntity = found.getFirst();
            }
        }

        return this.tongueEntity;
    }

    @ModifyVariable(
            method = "handleFallDamage",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private float modifyFallDistance(float fallDistance) {

        PlayerEntity player = (PlayerEntity) (Object) this;

        Identifier originId = PondspawnOrigin.id("pondspawn");


        PlayerOriginComponent component = (PlayerOriginComponent) io.github.apace100.origins.registry.ModComponents
                .ORIGIN.get(player);

        if (component != null) {
            boolean isFrog = component.getOrigins().values().stream()
                    .anyMatch(origin -> origin.getId().equals(originId));

            if (isFrog) {
                float safeDistance = 5.0f;
                return Math.max(0, fallDistance - safeDistance);
            }
        }

        return fallDistance;
    }

    @Inject(method = "damage",at = @At("HEAD"))
    private void onTakeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ClearTongue.killTongue((PlayerEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void handleTonguePhysics(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        Tongue tongue = this.pondspawn$getTongueEntity();


        player.setNoGravity(false);

        if (tongue != null && !tongue.isRemoved()/* && !player.getWorld().isClient()*/ ) {
            if (tongue.getAnimationController() != 1.0) return;

            double swingMaxSpeed = 0.6;
            double pullMaxSpeed = 1.2;

            double tongueLength = 0;
            double maxVelocity = 0;
            boolean isPull = false;
            double drag = 0;


            switch (tongue.tongueMode) {
                case LOOSE, DEFAULT -> {
                    tongueLength = Tongue.TONGUE_LENGTH;
                    maxVelocity = swingMaxSpeed;
                    drag = PlayerPhysicsConfig.lockMultiplier;
                }
                case LOCK -> {
                    tongueLength = Math.clamp(!player.isOnGround() ? tongue.getLockLength() - 1.3 : tongue.getLockLength(),0d,Double.POSITIVE_INFINITY);
                    maxVelocity = swingMaxSpeed;
                    drag = PlayerPhysicsConfig.lockMultiplier;

                }
                case PULL -> {
                    tongueLength = 0;
                    maxVelocity = pullMaxSpeed;
                    isPull = true;
                    drag = PlayerPhysicsConfig.pullMultiplier;
                }
            }

            if (this.pondspawn$getTarget() != null) {
                Entity entity = this.pondspawn$getTarget();
                applyTonguePhysicsWithEntity(player,entity,tongueLength,0.5);
            } else {
                applyTonguePhysics(player,tongueLength,maxVelocity,isPull);
                player.setVelocity(
                        player.getVelocity().multiply(drag)
                );
            }


        }
    }

    @Unique
    private Vec3d vecClamp(Vec3d vec, double max){

        if (vec.lengthSquared() > max * max) {
            return vec.normalize().multiply(max);
        }

        return vec;
    }


    @Unique
    private void applyTonguePhysics(PlayerEntity player, double maxLength,double maxVelocity,boolean isPull) {
        Tongue tongue = this.pondspawn$getTongueEntity();
        if (tongue == null) return;

        Vec3d tonguePos = tongue.getPos();
        Vec3d playerPos = player.getPos();


        Vec3d vecToTongue = tonguePos.subtract(playerPos);
        double currentDist = vecToTongue.length();
        double distanceToEye = tonguePos.subtract(player.getEyePos()).length();

        boolean belowTongue = playerPos.y < tonguePos.y;

        player.setNoGravity(currentDist > maxLength + 1.5 || isPull && belowTongue);

        Vec3d velocity = player.getVelocity();
        Vec3d ropeDir = vecToTongue.normalize();

        if (currentDist > maxLength || (currentDist < maxLength && !player.isOnGround() && !isPull && belowTongue)) {


            double stretch = currentDist - maxLength;

            double correctionAmount = Math.min(stretch * 0.10, 0.2);
            velocity = velocity.add(ropeDir.multiply(correctionAmount));


        }
        double radialSpeed = velocity.dotProduct(ropeDir);
        if (belowTongue) {
            if (radialSpeed < 0 && !isPull && !player.isOnGround()) {
                velocity = velocity.subtract(ropeDir.multiply(radialSpeed));
                velocity = velocity.multiply(1.03);
            }
            else if (isPull && currentDist < maxLength + 1.0) {
                player.setNoGravity(true);
                velocity = velocity.subtract(ropeDir.multiply(radialSpeed));
                velocity = velocity.multiply(PlayerPhysicsConfig.tangentMultiplier);
            }
        }


        player.setVelocity(vecClamp(velocity,maxVelocity));
        player.velocityModified = true;
        if (belowTongue) player.fallDistance = 0f;
    }

    @Unique private void applyTonguePhysicsWithEntity(PlayerEntity player,Entity entity,double maxLength, double maxVelocity) {
        Tongue tongue = this.pondspawn$getTongueEntity();
        if (tongue == null) return;

        Vec3d tonguePos = tongue.getPos();
        Vec3d playerPos = player.getPos();
        Vec3d vecToTongue = tonguePos.subtract(playerPos);
        double currentDist = vecToTongue.length();

        Vec3d velocity = player.getVelocity();
        Vec3d ropeDir = vecToTongue.normalize();

        if (currentDist > maxLength ) {


            double stretch = currentDist - maxLength;

            double correctionAmount = Math.min(stretch * 0.10, 0.2);
            velocity = velocity.add(ropeDir.multiply(correctionAmount));


            double playerWeight = WeightManager.getWeight(player);
            double entityWeight = WeightManager.getWeight(entity);

            double totalWeight = playerWeight + entityWeight;

            double mult = 0.5;

            double playerMult = entityWeight / totalWeight * 2.0 * mult;
            double entityMult = playerWeight / totalWeight * mult;

            if (playerMult < 0.7) playerMult = 1.0;
            if (entityMult < 0.7) entityMult = 1.0;


            if (playerMult > 0.3) {
                player.addVelocity(velocity.multiply(playerMult));
                player.setVelocity(vecClamp(player.getVelocity(),maxVelocity));
                player.velocityModified = true;
                if (playerPos.y < entity.getPos().y) player.fallDistance = 0f;
            }

            if (entityMult > 0.3) {

                if (entity.getPos().y < playerPos.y) entity.fallDistance = 0f;
                if (entity instanceof PlayerEntity playerEntity) {
                    playerEntity.addVelocity(velocity.multiply(-entityMult * 2.0));
                    playerEntity.velocityModified = true;
                } else {
                    entity.addVelocity(velocity.multiply(-entityMult));
                }
                entity.setVelocity(vecClamp(entity.getVelocity(),maxVelocity));
            }
        }
    }
}