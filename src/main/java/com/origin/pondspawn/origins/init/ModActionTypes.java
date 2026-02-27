package com.origin.pondspawn.origins.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.origins.entityactions.ExecuteCommandsActionType;
import com.origin.pondspawn.origins.entityactions.ThirstHandlerActionType;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class ModActionTypes {

    public static final ActionConfiguration<ExecuteCommandsActionType> EXECUTE_COMMANDS =
            register(ActionConfiguration.of(PondspawnOrigin.id("execute_commands"),ExecuteCommandsActionType.DATA_FACTORY));

    public static final ActionConfiguration<ThirstHandlerActionType> THIRST_HANDLER =
            register(ActionConfiguration.of(PondspawnOrigin.id("thirst_handler"),ThirstHandlerActionType.DATA_FACTORY));

    @SuppressWarnings("unchecked")
    public static <T extends EntityActionType> ActionConfiguration<T> register(ActionConfiguration<T> configuration) {

        ActionConfiguration<EntityActionType> casted = (ActionConfiguration<EntityActionType>) configuration;
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, casted.id(), casted);

        return configuration;


    }

    public static void register() {}
}