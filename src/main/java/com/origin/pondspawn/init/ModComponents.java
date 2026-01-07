package com.origin.pondspawn.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.component.classes.MyTongueComponent;
import com.origin.pondspawn.component.interfaces.TongueComponent;
import net.minecraft.entity.Entity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<TongueComponent> TONGUE = ComponentRegistry
            .getOrCreate(PondspawnOrigin.id("tongue"), TongueComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Correct order: Register for the base class first
        registry.registerFor(Entity.class, TONGUE, entity -> new MyTongueComponent());

        // Correct signature for 1.21.1 players:
        registry.registerForPlayers(TONGUE, player -> new MyTongueComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
