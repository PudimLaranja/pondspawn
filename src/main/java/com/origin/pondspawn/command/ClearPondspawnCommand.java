package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.custum.TongueScarf;
import com.origin.pondspawn.entity.custum.TongueScarfMouth;
import com.origin.pondspawn.entity.custum.TongueTip;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;

public class ClearPondspawnCommand {
    public static int commandLogic(CommandContext<ServerCommandSource> context) {
        ServerWorld serverWorld = context.getSource().getWorld();

        serverWorld.iterateEntities().forEach(entity -> {
            if (
                    entity instanceof Tongue ||
                    entity instanceof TongueScarf ||
                    entity instanceof TongueScarfMouth ||
                    entity instanceof TongueTip

            ) entity.kill();
        });
        return 0;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAcess,
                envioronment
                ) -> {
            dispatcher.register(CommandManager.literal("pondspawnClear")
                    .requires(source -> source.hasPermissionLevel(Common.command_permission))
                    .executes(ClearPondspawnCommand::commandLogic)
            );
        });
    }
}
