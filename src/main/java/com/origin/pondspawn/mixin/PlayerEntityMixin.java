package com.origin.pondspawn.mixin;

import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.command.ClearTongue;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TargetTypes;
import com.origin.pondspawn.entity.enums.TongueModes;
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
import org.joml.Vector2d;
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

    @Inject(method = "travel", at = @At("HEAD"))
    private void handleTonguePhysics(Vec3d travelVector,CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        Tongue tongue = this.pondspawn$getTongueEntity();

        player.setNoGravity(false);

        if (tongue != null && !tongue.isRemoved()/* && !player.getWorld().isClient()*/ ) {
            if (tongue.getAnimationController() != 1.0) return;
            if (tongue.getTargetMode() == TargetTypes.AIR) return;

            double swingMaxSpeed = 0.6;
            double pullMaxSpeed = 1.2;

            double tongueLength = 0;
            double maxVelocity = 0;

            player.setNoGravity(false);


            switch (tongue.tongueMode) {
                case LOOSE, DEFAULT -> {
                    tongueLength = Tongue.TONGUE_LENGTH;
                    maxVelocity = swingMaxSpeed;
                }
                case LOCK -> {
                    tongueLength = Math.max(tongue.getLockLength() - (!player.isOnGround() ? 3.0 : 0),1);
                    maxVelocity = swingMaxSpeed;
                }
                case PULL -> {
                    tongueLength = 0.5;
                    maxVelocity = pullMaxSpeed;
                }
            }

            if (this.pondspawn$getTarget() != null) {
                Entity entity = this.pondspawn$getTarget();
                assert entity != null;

                applyTongueConnectionPhysics(
                        player,entity,
                        WeightManager.getWeight(player),
                        WeightManager.getWeight(entity),
                        tongueLength
                );

            }  else {
                applyTonguePhysics(player,tongueLength);
            }


        }
    }


    @Unique
    private void applyTonguePhysics(PlayerEntity player, double maxLength) {
        Tongue tongue = this.pondspawn$getTongueEntity();
        if (tongue == null) return;

        Vec3d tonguePos = tongue.getPos();
        Vec3d playerEyePos = player.getEyePos();

        Vec3d vecToTongue = tonguePos.subtract(playerEyePos);
        Vec3d dirToTongue = vecToTongue.normalize();

        double dist = vecToTongue.length();

        Vec3d vel = player.getVelocity();
        double velRadial = vel.dotProduct(dirToTongue);
        Vec3d velTangential = vel.subtract(dirToTongue.multiply(velRadial));

        if (dist > maxLength) {
            double stretch = dist - maxLength;
            if (tongue.tongueMode == TongueModes.PULL) {
                player.setNoGravity(true);
                if (stretch > 0.1) {
                    velRadial = (0.115 + Math.max(stretch * 2.0,2.0) * 0.045) * 1.0;
                }
            } else {
                if (stretch > 0.1) {
                    velRadial = (0.115 + 0.9 * 0.045) * 1.0;
                }
            }

            if (stretch > 0.03) {
                double new_velRadial = stretch * 0.0333;
                if (!(velRadial > new_velRadial)) {
                    velRadial = new_velRadial;
                }
            } else {
                velRadial = 0;
            }

            if (
                    !player.isFallFlying() &&
                    !player.isOnGround() &&
                    !(tongue.tongueMode == TongueModes.PULL)
            ) {
                velTangential = velTangential.multiply(1.05); //default 1.048;
            }

        }

        Vec3d velNew;

        if (player.isOnGround()) {
            velNew = velTangential.add(dirToTongue.multiply(velRadial));
        } else {
            velNew = velTangential.add(dirToTongue.multiply(velRadial * 0.99));

            if (dist < maxLength + 0.4) {
                double drag = 0.3;

                velNew = velNew.multiply(drag,0.6,drag);
            }
        }



        player.setVelocity(velNew);
        player.velocityModified = true;
        if (dirToTongue.y > -0.15 && dist + 0.5 > maxLength) {
           player.onLanding();
        }

    }

    private void applyTongueConnectionPhysics(PlayerEntity player, Entity entity, double playerWeight, double entityWeight, double maxLength) {
        if (player == null || entity == null) return;

        // 1. Get Positions
        // We use getPos() for center-of-mass physics logic
        Vec3d playerPos = player.getPos();
        Vec3d entityPos = entity.getPos();

        Vec3d vecToEntity = entityPos.subtract(playerPos);
        double dist = vecToEntity.length();

        // If the rope is loose (distance < max length), physics do not apply.
        if (dist <= maxLength) return;

        // 2. Calculate Directions
        Vec3d dirToEntity = vecToEntity.normalize();
        Vec3d dirToPlayer = dirToEntity.negate(); // Direction from Entity -> Player

        // 3. Calculate Weight Ratios (Who gets pulled more?)
        double totalWeight = playerWeight + entityWeight;

        // If Entity is heavy, player gets pulled 90%. If Player is heavy, Entity gets pulled 90%.
        double playerInfluence = entityWeight / totalWeight;
        double entityInfluence = playerWeight / totalWeight;

        // 4. Calculate Tension (The "Stretch" force)
        double stretch = dist - maxLength;

        // Elasticity factor: determines how "snappy" the tongue is.
        // Derived from your reference's logic (stretch * ~0.033). Adjusted slightly for entity-to-entity feel.
        double tensionForce = stretch * 0.06;

        // --- APPLY TO PLAYER ---
        Vec3d pVel = player.getVelocity();

        // Split velocity into: Moving towards/away (Radial) vs Swinging around (Tangential)
        double pVelRadial = pVel.dotProduct(dirToEntity);
        Vec3d pVelTangential = pVel.subtract(dirToEntity.multiply(pVelRadial));

        // If player is moving AWAY from entity, apply drag to that motion so they don't fly off endlessly
        if (pVelRadial < 0) {
            pVelRadial *= 0.5; // Dampen the 'moving away' energy
        }

        // Add the tension pull towards the entity
        pVelRadial += tensionForce * playerInfluence;

        // Recombine and set
        // Note: We multiply tangential by 0.99 to simulate air resistance/friction slightly, preventing infinite orbits
        Vec3d pVelNew = pVelTangential.multiply(0.99).add(dirToEntity.multiply(pVelRadial));
        player.setVelocity(pVelNew);
        player.velocityModified = true;

        // --- APPLY TO ENTITY ---
        Vec3d eVel = entity.getVelocity();

        // Split velocity
        double eVelRadial = eVel.dotProduct(dirToPlayer);
        Vec3d eVelTangential = eVel.subtract(dirToPlayer.multiply(eVelRadial));

        // If entity is moving AWAY from player, apply drag
        if (eVelRadial < 0) {
            eVelRadial *= 0.5;
        }

        // Add tension pull towards the player
        eVelRadial += tensionForce * entityInfluence;

        // Recombine and set
        Vec3d eVelNew = eVelTangential.multiply(0.99).add(dirToPlayer.multiply(eVelRadial));
        entity.setVelocity(eVelNew);
        entity.velocityModified = true;

        // Optional: If you want the landing logic from your original code
        // This checks if the player is being pulled upwards significantly
        if (dirToEntity.y > -0.15 && dist > maxLength + 0.5) {
            //player.onLanding();
        }
    }
}