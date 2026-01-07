package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TargetTypes;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.init.ModComponents;
import com.origin.pondspawn.init.ModEntities;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.UUID;


public class TongueCommand {



    private static int tongue_logic(CommandContext<ServerCommandSource> context,boolean useCaller,boolean raycast) throws CommandSyntaxException {

        var source = context.getSource();
        source.sendFeedback(() -> Text.literal("called /tongue"), false);
        Entity entity;

        if (useCaller) {
            entity = source.getEntity();
        } else {
            entity = EntityArgumentType.getEntity(context,"entity");
        }

        World world = entity.getWorld();

        Vec3d position;

        PlayerEntity player;

        if (entity instanceof PlayerEntity playerEntity) {
            player = playerEntity;
        } else {
            return -1;
        }

        Tongue tongue = ((PlayerWithTongueData) player).pondspawn$getTongueEntity();

        if (tongue != null) {
            if (tongue.retracting) {
                tongue.kill();
                ((PlayerWithTongueData) player).pondspawn$setTongueEntity(null);
            } else {
                tongue.setTongueMode(TongueModes.LOCK);
                return 1;
            }
        }

        Tongue tongueEntity = new Tongue(
                ModEntities.TONGUE_ENTITY_TYPE,
                world
        );

        if (raycast) {
            position = getRaycastPos(player,tongueEntity,world);
            if (position == Vec3d.ZERO) return 0;
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

        tongueEntity.setMode(TargetTypes.PLAYER);



        tongueEntity.finish();

        world.spawnEntity(tongueEntity);

        return 1;
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
                (targetEntity) -> !targetEntity.isSpectator() && targetEntity.isAlive() && targetEntity != player, // Filter: ignore self, spectators
                maxDistance * maxDistance
        );


        if (entityHitResult != null) {
            Entity hitEntity = entityHitResult.getEntity();

            tongueEntity.FollowEntity = hitEntity.getUuid();
            tongueEntity.TargetMode = TargetTypes.ENTITY;
            return hitEntity.getPos();

        } else {
            BlockHitResult blockHitResult = world.raycast(new RaycastContext(
                    startPos,
                    endPos,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player
            ));

            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
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
                            .executes(context -> tongue_logic(context,true,true))
                    .then(
                            CommandManager.argument("entity",EntityArgumentType.entity())
                                    .executes(context -> tongue_logic(context,false, true))
                                    .then(
                                    CommandManager.argument("pos",BlockPosArgumentType.blockPos())
                                            .executes(context -> tongue_logic(context,false,false)
                                            )
                            )
                    )
            );
        });
    }
}
