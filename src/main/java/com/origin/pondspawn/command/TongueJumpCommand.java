package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.globalconfig.PlayerPhysicsConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class TongueJumpCommand {
    private static int commandLogic(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        Entity entity = context.getSource().getEntity();

        if (entity instanceof PlayerEntity player) {
            if (((PlayerWithTongueData) player).pondspawn$jumpAllowed()) {

                ClearTongue.killTongue(player);
                Vec3d dir = player.getRotationVector().multiply(1,0,1).add(0,1,0);
                player.setVelocity(dir.multiply(PlayerPhysicsConfig.jumpVector));
                player.velocityModified = true;
            }
        }

        return 0;
    }


    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("tongueJump")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(TongueJumpCommand::commandLogic)

            );
        });
    }

}
