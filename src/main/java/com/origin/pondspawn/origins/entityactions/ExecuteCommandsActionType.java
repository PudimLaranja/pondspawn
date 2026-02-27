package com.origin.pondspawn.origins.entityactions;

import com.origin.pondspawn.origins.init.ModActionTypes;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExecuteCommandsActionType extends EntityActionType {

    public static final TypedDataObjectFactory<ExecuteCommandsActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
            new SerializableData()
                    .add("commands",SerializableDataTypes.STRINGS),
            data -> new ExecuteCommandsActionType(
                    data.get("commands")
            ),
            (actionType,serializableData) -> serializableData.instance()
                    .set("commands",actionType.commands)
    );

    private final List<String> commands;

    public ExecuteCommandsActionType(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void accept(EntityActionContext context) {
        Entity entity = context.entity();

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

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ModActionTypes.EXECUTE_COMMANDS;
    }

}