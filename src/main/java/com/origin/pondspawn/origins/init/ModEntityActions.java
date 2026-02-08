package com.origin.pondspawn.origins.init;

import com.origin.pondspawn.origins.entityactions.ExecuteCommandsActionType;
import com.origin.pondspawn.origins.entityactions.ThirstHandlerActionType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class ModEntityActions {
    public static void register() {
        // Pass the factory directly to the correct registry
        Registry.register(
                ApoliRegistries.ENTITY_ACTION,
                ExecuteCommandsActionType.getFactory().getSerializerId(),
                ExecuteCommandsActionType.getFactory()
            );
        Registry.register(
                ApoliRegistries.ENTITY_ACTION,
                ThirstHandlerActionType.getFactory().getSerializerId(),
                ThirstHandlerActionType.getFactory()
        );
    }

}