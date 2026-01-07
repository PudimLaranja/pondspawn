package com.origin.pondspawn.init;

import com.origin.pondspawn.entityactions.ExecuteCommandsActionType;
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
    }
}