package com.origin.pondspawn.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ModUtil {
    @Nullable
    public static Entity getEntityByUUID(World world, UUID uuid) {
        return world instanceof ServerWorld serverWorld ? serverWorld.getEntity(uuid) : null;
    }
}
