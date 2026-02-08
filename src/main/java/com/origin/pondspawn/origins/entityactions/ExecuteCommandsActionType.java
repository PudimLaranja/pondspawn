package com.origin.pondspawn.origins.entityactions;

import com.origin.pondspawn.PondspawnOrigin;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

public class ExecuteCommandsActionType {

    public static void action(Entity entity, List<String> commands) {
        MinecraftServer server = entity.getServer();
        if (server == null || entity.getWorld().isClient) return;

        // Use the entity's command source stack
        ServerCommandSource source = entity.getCommandSource()
                .withLevel(Apoli.config.executeCommand.permissionLevel)
                .withOutput(Apoli.config.executeCommand.showOutput ? server : CommandOutput.DUMMY);

        for (String command : commands) {
            server.getCommandManager().executeWithPrefix(source, command);
        }
    }

    public static ActionTypeFactory<Entity> getFactory() {
        return new ActionTypeFactory<>(
                PondspawnOrigin.id("execute_commands"),
                new SerializableData()
                        .add("commands", SerializableDataTypes.STRINGS),
                (data, entity) -> action(entity, data.get("commands"))
        );
    }
}