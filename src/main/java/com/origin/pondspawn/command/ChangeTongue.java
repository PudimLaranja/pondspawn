package com.origin.pondspawn.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.Objects;

public class ChangeTongue {
    private static int commandLogic(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        Entity entity = EntityArgumentType.getEntity(context,"entity");

        String mode = StringArgumentType.getString(context,"mode");

        if (entity instanceof PlayerEntity player) {
           Tongue tongue = ((PlayerWithTongueData) player).pondspawn$getTongueEntity();
           if (tongue != null) {
               for (TongueModes currentMode:TongueModes.values()) {
                   if (Objects.equals(mode, currentMode.asString())) tongue.setTongueMode(currentMode);
               }
           }
        }

        return 0;
    }

    private static final SuggestionProvider<ServerCommandSource> TONGUE_MODE_SUGGESTIONS =
            (context, builder) -> {
                String[] types = Arrays.stream(TongueModes.values())
                        .map(TongueModes::asString)
                        .toArray(String[]::new);

                return CommandSource.suggestMatching(types,builder);
            };

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("changeTongue")
                    .requires(source -> source.hasPermissionLevel(Common.command_permission))
                    .then(
                            CommandManager.argument("entity", EntityArgumentType.entity()).then(
                            CommandManager.argument("mode", StringArgumentType.word())
                                .suggests(TONGUE_MODE_SUGGESTIONS)
                                .executes(ChangeTongue::commandLogic)


                    ))
            );
        });
    }
}
