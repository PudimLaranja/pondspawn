package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.custum.TongueTip;
import com.origin.pondspawn.entity.enums.TargetTypes;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.init.ModEntityTypes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;


public class TongueCommand {



    private static int commandLogic(CommandContext<ServerCommandSource> context, boolean useCaller, boolean raycast)
            throws CommandSyntaxException
    {

        var source = context.getSource();

        Entity entity;

        if (useCaller) {
            entity = source.getEntity();
        } else {
            entity = EntityArgumentType.getEntity(context,"entity");
        }

        if (entity == null) return -1;
        World world = entity.getWorld();

        if (entity instanceof PlayerEntity player) {
            if (((PlayerWithTongueData) player).pondspawn$getTongueEntity() instanceof Tongue tongue) {
                if (!tongue.retracting) {
                    tongue.setTongueMode(TongueModes.LOCK);
                    return 1;
                }

                tongue.kill();
                ((PlayerWithTongueData) player).pondspawn$setTongueEntity(null);
            }

            Tongue tongueEntity = new Tongue(ModEntityTypes.TONGUE_ENTITY_TYPE, world);
            Vec3d position;
            if (raycast) {
                position = getRaycastPos(player,tongueEntity,world);
                if (position == Vec3d.ZERO) {
                    ((PlayerWithTongueData) player).pondspawn$setTongueOut(false);
                    return 0;
                } else {
                    ((PlayerWithTongueData) player).pondspawn$setTongueOut(true);
                }
            } else {

                BlockPos blockPosition = BlockPosArgumentType.getBlockPos(context,"pos");

                position = new Vec3d(
                        blockPosition.getX(),
                        blockPosition.getY(),
                        blockPosition.getZ()
                );
            }

            tongueEntity.setLockLength(
                    position.subtract(player.getEyePos()).length()
            );

            tongueEntity.setTongueMode(TongueModes.LOCK);

            ((PlayerWithTongueData) player).pondspawn$setTongueEntity(tongueEntity);

            tongueEntity.setEntityTarget(entity.getUuid());
            tongueEntity.setPosition(position);

            TongueTip tip = TongueTip.factory(world,tongueEntity);
            tongueEntity.finish();


            world.spawnEntity(tongueEntity);
            world.spawnEntity(tip);

            return 1;

        } else {
            return -1;
        }
    }

    private static Vec3d getRaycastPos(PlayerEntity player,Tongue tongueEntity,World world) {

        double maxDistance = Tongue.TONGUE_LENGTH;

        Vec3d startPos = player.getEyePos();
        Vec3d lookVector = player.getRotationVector();
        Vec3d endPos = startPos.add(lookVector.multiply(maxDistance));

        Box searchBox = player.getBoundingBox().stretch(lookVector.multiply(maxDistance)).expand(1.0); // Expand slightly for reliability

        EntityHitResult entityHitResult = ProjectileUtil.raycast(
                player,
                startPos,
                endPos,
                searchBox,
                (targetEntity) ->
                        !targetEntity.isSpectator() && targetEntity.isAlive() && targetEntity != player &&
                            targetEntity instanceof PlayerEntity ||
                            targetEntity instanceof MobEntity ||
                            targetEntity instanceof ItemEntity
                , // Filter: ignore self, spectators
                maxDistance * maxDistance
        );


        if (entityHitResult != null) {
            Entity hitEntity = entityHitResult.getEntity();

            tongueEntity.FollowEntity = hitEntity.getUuid();

            tongueEntity.setTargetMode(TargetTypes.ENTITY);

            ((PlayerWithTongueData) player).pondspawn$setTarget(hitEntity);
            return hitEntity.getEyePos();

        } else {
            BlockHitResult blockHitResult = world.raycast(new RaycastContext(
                    startPos,
                    endPos,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player
            ));
             if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                tongueEntity.setTargetMode(TargetTypes.BLOCK);
                tongueEntity.blockTarget = blockHitResult.getBlockPos();
                return blockHitResult.getPos();
            } else {
                return Vec3d.ZERO;
            }
        }
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("tongue")
                    .requires(source -> source.hasPermissionLevel(Common.command_permission))
                            .executes(context -> commandLogic(context,true,true))
                    .then(
                            CommandManager.argument("entity",EntityArgumentType.entity())
                                .executes(context -> commandLogic(context,false, true)).then(
                            CommandManager.argument("pos",BlockPosArgumentType.blockPos())
                                .executes(context -> commandLogic(context,false,false)
                            ))
                    )
            );
        });
    }
}
