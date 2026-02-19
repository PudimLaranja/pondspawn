package com.origin.pondspawn.origins.init;

import com.origin.pondspawn.origins.entityactions.ExecuteCommandsActionType;
import com.origin.pondspawn.origins.entityactions.ThirstHandlerActionType;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class ModEntityActions {
    public static void register() {
        // Pass the factory directly to the correct registry
        register(ExecuteCommandsActionType.getFactory());
        register(ThirstHandlerActionType.getFactory());
    }

    private static <F extends ActionTypeFactory<Entity>> F register(F factory) {
        return Registry.register(
                    ApoliRegistries.ENTITY_ACTION,
                    factory.getSerializerId(),
                    factory
                );
    }
}