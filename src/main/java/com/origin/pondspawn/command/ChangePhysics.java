package com.origin.pondspawn.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.origin.pondspawn.command.enums.PhysicsConstants;
import com.origin.pondspawn.globalconfig.PlayerPhysicsConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class ChangePhysics {

    private static final Vec3d startValue = PlayerPhysicsConfig.jumpVector;

    private static int commandLogic(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        double forward = DoubleArgumentType.getDouble(context,"forward");
        double upward = DoubleArgumentType.getDouble(context,"upward");

        PlayerPhysicsConfig.jumpVector = new Vec3d(0.0,upward,forward);

        if (context.getSource().getEntity() == null) return -1;
        context.getSource().getEntity().sendMessage(Text.literal(PlayerPhysicsConfig.jumpVector.toString()));
        return 0;
    }

    private static int reset(CommandContext<ServerCommandSource> context) {
        PlayerPhysicsConfig.jumpVector = startValue;

        if (context.getSource().getEntity() == null) return -1;
        context.getSource().getEntity().sendMessage(Text.literal(PlayerPhysicsConfig.jumpVector.toString()));

        return 0;
    }



    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("changePhysics")
                .then(
                        CommandManager.literal("reset")
                                .executes(ChangePhysics::reset)
                )
                .then(
                    CommandManager.argument("forward", DoubleArgumentType.doubleArg())
                            .then(
                                    CommandManager.argument("upward", DoubleArgumentType.doubleArg())
                                            .executes(ChangePhysics::commandLogic)
                            )
                )

            );
        });
    }
}
