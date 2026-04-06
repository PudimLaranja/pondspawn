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

public class ClearTongue {
    private static int commandLogic(CommandContext<ServerCommandSource> context, boolean useCaller) throws CommandSyntaxException {

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
                ((PlayerWithTongueData) player).pondspawn$setTongueOut(false);
            });
        }
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAccess,
                environment
        ) -> {
            dispatcher.register(CommandManager.literal("clearTongue")
                            .executes(context -> commandLogic(context,true))
                    .then(
                            CommandManager.argument("entity", EntityArgumentType.entity())
                                    .executes(context -> commandLogic(context,false))


                    )
            );
        });
    }

}
