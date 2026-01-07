package com.origin.pondspawn.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.entity.custum.Tongue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    private static final Identifier TONGUE_ID = PondspawnOrigin.id("tongue");

    public static final EntityType<Tongue> TONGUE_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            TONGUE_ID,
            EntityType.Builder
                    .create(Tongue::new, SpawnGroup.MISC)
                    .dimensions(0.5f,0.5f)
                    .build("tongue")
    );

    public static void load() {}
}
