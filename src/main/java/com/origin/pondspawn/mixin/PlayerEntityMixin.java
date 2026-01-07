package com.origin.pondspawn.mixin;

import com.mojang.authlib.GameProfile;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.globalconfig.PlayerPhysicsConfig;
import com.origin.pondspawn.init.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


//code directly copied from this mod https://modrinth.com/mod/yori3os-grappling-hooks

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerWithTongueData {
    @Unique
    private Tongue tongueEntity;


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
        if (this.tongueEntity == null) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            if (player.getWorld() instanceof ServerWorld world) {
                if (world.getEntity(
                        ModComponents.TONGUE.get(player).getTongueUuid()
                ) instanceof Tongue tongue) {
                    this.tongueEntity = tongue;
                }
            }
        }
        return this.tongueEntity;
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void handleTonguePhysics(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

       Tongue tongue = this.pondspawn$getTongueEntity();


        if (tongue != null && !tongue.isRemoved()/* && !player.getWorld().isClient()*/ ) {
            if (tongue.getAnimationController() != 1.0) return;

            double swingMaxSpeed = 0.65;
            double pullMaxSpeed = 1.2;

            switch (tongue.tongueMode) {
                case LOOSE, DEFAULT -> {
                    applyTonguePhysics(player,Tongue.TONGUE_LENGTH,swingMaxSpeed);
                    player.setVelocity(
                            player.getVelocity().multiply(PlayerPhysicsConfig.lockMultiplier)
                    );
                }
                case LOCK -> {
                    applyTonguePhysics(player,tongue.getLockLength(),swingMaxSpeed);
                    player.setVelocity(
                            player.getVelocity().multiply(PlayerPhysicsConfig.lockMultiplier)
                    );
                }
                case PULL -> {
                    applyTonguePhysics(player,0,pullMaxSpeed);
                    player.setVelocity(
                            player.getVelocity().multiply(PlayerPhysicsConfig.pullMultiplier)
                    );
                }
            }
        }
    }

    @Unique
    private Vec3d clamp(Vec3d vec, double max){

        if (vec.length() > max) {
            return vec.normalize().multiply(max);
        }

        return vec;
    }

    @Unique
    private void applyTonguePhysics(PlayerEntity player, double maxLength,double maxVelocity) {
        Tongue tongue = this.pondspawn$getTongueEntity();
        if (tongue == null) return;

        Vec3d tonguePos = tongue.getPos();
        Vec3d playerPos = player.getEyePos();
        Vec3d vecToTongue = tonguePos.subtract(playerPos);
        double currentDist = vecToTongue.length();

        if (currentDist > maxLength) {
            Vec3d velocity = player.getVelocity();
            Vec3d ropeDir = vecToTongue.normalize();

            // 1. Kill radial velocity (moving away from hook)
            double radialSpeed = velocity.dotProduct(ropeDir);
            if (radialSpeed < 0) {
                // This 'flattens' the movement against the sphere of the rope
                velocity = velocity.subtract(ropeDir.multiply(radialSpeed));
                velocity = velocity.multiply(PlayerPhysicsConfig.tangentMultiplier);
            }

            // 2. Smoothed Snap (The Lag Protector)
            double stretch = currentDist - maxLength;

            // Instead of 0.15, we use a value that won't exceed a safe speed.
            // 0.4 blocks/tick is a fast but manageable pull.
            double correctionAmount = Math.min(stretch * 0.15, 0.4);
            velocity = velocity.add(ropeDir.multiply(correctionAmount));

            player.setVelocity(clamp(velocity,maxVelocity));
            player.sendMessage(Text.literal(
                    "%f".formatted(player.getVelocity().length())
            ));
            player.velocityModified = true;
            player.fallDistance = 0f;
        }
    }
}
