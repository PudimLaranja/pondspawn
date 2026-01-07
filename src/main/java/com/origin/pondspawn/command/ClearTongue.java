package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;
import com.origin.pondspawn.init.ModComponents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;

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
            Tongue tongue = ((PlayerWithTongueData) player).pondspawn$getTongueEntity();
            if (tongue != null) {
                tongue.retract(() -> {
                    ((PlayerWithTongueData) player).pondspawn$setTongueEntity(null);
                });
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
