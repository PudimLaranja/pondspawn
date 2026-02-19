package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.origin.pondspawn.weightSystem.WeightManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class WeightCommand {
    private static int commandLogic(CommandContext<ServerCommandSource> context, boolean useCaller) throws CommandSyntaxException {

        Entity entity;

        if (useCaller) {
           entity = context.getSource().getEntity();
        } else {
            entity = EntityArgumentType.getEntity(context,"entity");
        }

        if (entity != null) {
            double weight = WeightManager.getWeight(entity);

            if (context.getSource().getEntity() instanceof PlayerEntity player) {
                player.sendMessage(Text.literal("the entity weighs %f".formatted(weight)));
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
            dispatcher.register(CommandManager.literal("weight")
                            .executes(context -> commandLogic(context,true))
                    .then(
                            CommandManager.argument("entity", EntityArgumentType.entity())
                                    .executes(context -> commandLogic(context,false))


                    )
            );
        });
    }

}
