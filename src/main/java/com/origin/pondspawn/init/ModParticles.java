package com.origin.pondspawn.init;

import com.origin.pondspawn.PondspawnOrigin;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {

    public static final SimpleParticleType FROG_JUMP_PARTICLE = register("frog_jump_particle", FabricParticleTypes.simple());

    public static final SimpleParticleType FROG_TRAIL_PARTICLE = register("frog_trail_particle", FabricParticleTypes.simple());

    private static SimpleParticleType register(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, PondspawnOrigin.id(name),particleType);
    }

    public static void register() {}

}
