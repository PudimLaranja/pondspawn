package com.origin.pondspawn.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AnimationCommand {
    public static int commandLogic(CommandContext<ServerCommandSource> context) {
        float animationPercent = FloatArgumentType.getFloat(context,"percent");

        if (context.getSource().getEntity() instanceof PlayerEntity player)  {
            if (
                    ((PlayerWithTongueData) player).pondspawn$getTongueEntity() instanceof Tongue tongue
            ) {
                tongue.setAnimationController(animationPercent);
            } else {
                player.sendMessage(Text.literal("Couldn't find tongue object"));
            }
        }
        return 0;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAcess,
                envioronment
                ) -> {
            dispatcher.register(CommandManager.literal("animation").then(
                        CommandManager.argument("percent", FloatArgumentType.floatArg(0f,1f))
                                .executes(AnimationCommand::commandLogic)
                    )
            );
        });
    }
}
