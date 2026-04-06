package com.origin.pondspawn.init;


import com.origin.pondspawn.particle.FrogJumpParticle;
import com.origin.pondspawn.particle.FrogTrailParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticlesClient {

    public static void register() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.FROG_JUMP_PARTICLE, FrogJumpParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FROG_TRAIL_PARTICLE, FrogTrailParticle.Factory::new);
    }

}
