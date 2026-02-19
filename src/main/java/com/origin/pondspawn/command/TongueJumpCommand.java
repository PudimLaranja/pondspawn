package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TongueJumpCommand {
    private static int commandLogic(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        Entity entity = context.getSource().getEntity();

        if (entity instanceof PlayerEntity player) {
            if (((PlayerWithTongueData) player).pondspawn$jumpAllowed()) {
                ClearTongue.killTongue(player);
                player.addVelocity(0,5,0);
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
                    .executes(TongueJumpCommand::commandLogic)

            );
        });
    }

}
