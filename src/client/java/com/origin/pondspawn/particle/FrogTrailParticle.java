package com.origin.pondspawn.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FrogTrailParticle extends SpriteBillboardParticle {

    public FrogTrailParticle(ClientWorld clientWorld,
                             double x, double y, double z,
                             SpriteProvider spriteProvider,
                             double speedX, double speedY, double speedZ
    ) {
        super(clientWorld, x, y, z, speedX, speedY, speedZ);

        this.velocityMultiplier = 0.96F;
        this.ascending = true;
        this.scale *= 0.75F;
        this.collidesWithWorld = false;
        float color_range = 0.3f;
        float reverse_range = 1.0f - color_range;

        this.red = this.random.nextFloat() * color_range + reverse_range;
        this.green = this.random.nextFloat() * color_range + reverse_range;
        this.blue = this.random.nextFloat() * color_range + reverse_range;
        this.maxAge = (int)(Math.random() * (double)10.0F) + 40;
        this.setVelocity(speedX * 0.01 / (double)2.0F, speedY * 0.01, speedZ * 0.01 / (double)2.0F);
        this.setMaxAge(clientWorld.random.nextInt(30) + 10);
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        @Nullable
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ
        ) {
            return new FrogTrailParticle(world,x,y,z,this.spriteProvider,velocityX,velocityY,velocityZ);
        }
    }
}
