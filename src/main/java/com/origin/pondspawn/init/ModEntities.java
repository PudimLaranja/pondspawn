package com.origin.pondspawn.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.custum.TongueTip;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<Tongue> TONGUE_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            PondspawnOrigin.id("tongue"),
            EntityType.Builder
                    .create(Tongue::new, SpawnGroup.MISC)
                    .dimensions(0.5f,0.5f)
                    .build("tongue")
    );

    public static final EntityType<TongueTip> TONGUE_TIP_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            PondspawnOrigin.id("tongue_tip"),
            EntityType.Builder
                    .create(TongueTip::new,SpawnGroup.MISC)
                    .dimensions(0.5f,0.5f)
                    .build("tongue_tip")
    );

    public static void load() {}
}
