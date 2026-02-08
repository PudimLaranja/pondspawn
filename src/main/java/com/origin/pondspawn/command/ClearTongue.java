package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.init.ModComponents;
import com.origin.pondspawn.util.TickScheduler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class ClearTongue {
    private static int clearLogic(CommandContext<ServerCommandSource> context,boolean useCaller) throws CommandSyntaxException {

        Entity entity;

        if (useCaller) {
           entity = context.getSource().getEntity();
        } else {
            entity = EntityArgumentType.getEntity(context,"entity");
        }

        if (entity instanceof PlayerEntity player) {
            killTongue(player);
        }

        return 0;
    }

    public static void killTongue(PlayerEntity player) {
        Tongue tongue = ((PlayerWithTongueData) player).pondspawn$getTongueEntity();
        if (tongue != null) {
            if (!tongue.clearable) return;
            ((PlayerWithTongueData) player).pondspawn$setTarget(null);
            tongue.retract(() -> {
                ((PlayerWithTongueData) player).pondspawn$setTongueEntity(null);
            });
        }
    }

    private static void applyBoost(PlayerEntity player) {
        if (!player.isOnGround()) {
            double boost = 0.6;
            Vec3d playerRotationVector = player.getRotationVector();
            Vec3d dir = new Vec3d(playerRotationVector.x,0.6,playerRotationVector.z).normalize();
            player.addVelocity(dir.multiply(boost));
            player.velocityModified = true;
        }
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("clearTongue")
                            .executes(context -> clearLogic(context,true))
                    .then(
                            CommandManager.argument("entity", EntityArgumentType.entity())
                                    .executes(context -> clearLogic(context,false))


                    )
            );
        });
    }

}
