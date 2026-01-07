package com.origin.pondspawn.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.command.enums.PhysicsConstants;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.globalconfig.PlayerPhysicsConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;

public class ChangePhysics {
    private static int changeLogic(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String constant_name = StringArgumentType.getString(context,"constant");

        double value = DoubleArgumentType.getDouble(context,"value");

        switch (constant_name){
            case "pull" -> {
                PlayerPhysicsConfig.pullMultiplier = value;
            }
            case "swing" -> {
                PlayerPhysicsConfig.lockMultiplier = value;
            }
            case "tangent" -> {
                PlayerPhysicsConfig.tangentMultiplier = value;
            }
        }

        return 0;
    }

    private static int getLogic(CommandContext<ServerCommandSource> context) throws  CommandSyntaxException {

        String constant_name = StringArgumentType.getString(context,"constant");

        if (context.getSource().getEntity() instanceof PlayerEntity player) {

            switch (constant_name){
                case "pull" -> {
                    player.sendMessage(
                            Text.literal("%f".formatted(PlayerPhysicsConfig.pullMultiplier))
                    );
                }
                case "swing" -> {
                    player.sendMessage(
                            Text.literal("%f".formatted(PlayerPhysicsConfig.lockMultiplier))
                    );
                }
                case "tangent" -> {
                    player.sendMessage(
                            Text.literal("%f".formatted(PlayerPhysicsConfig.tangentMultiplier))
                    );
                }
            }
        }

        return 0;

    }

    private static final SuggestionProvider<ServerCommandSource> PHYSICS_SUGGESTIONS =
            (context, builder) -> {
                String[] types = Arrays.stream(PhysicsConstants.values())
                        .map(PhysicsConstants::asString)
                        .toArray(String[]::new);

                return CommandSource.suggestMatching(types,builder);
            };

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("changePhysics")
                    .then(
                        CommandManager.literal("set")
                                .then(
                                    CommandManager.argument("constant", StringArgumentType.word())
                                    .suggests(PHYSICS_SUGGESTIONS)
                                    .then(
                                        CommandManager.argument("value", DoubleArgumentType.doubleArg(0.0,2.0))
                                        .executes(ChangePhysics::changeLogic)
                                    )
                                )

                    )
                    .then(
                            CommandManager.literal("get")
                                    .then(
                                            CommandManager.argument("constant", StringArgumentType.word())
                                                    .suggests(PHYSICS_SUGGESTIONS)
                                                    .executes(ChangePhysics::getLogic)
                                    )
                    )
            );
        });
    }
}
