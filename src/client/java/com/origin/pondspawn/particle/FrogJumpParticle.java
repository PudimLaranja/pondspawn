package com.origin.pondspawn.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FrogJumpParticle extends SpriteBillboardParticle {
    private final double startX;
    private final double startY;
    private final double startZ;

    public FrogJumpParticle(
            ClientWorld clientWorld, double x, double y, double z,
            SpriteProvider spriteProvider,
            double xSpeed, double ySpeed, double zSpeed
    ) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed);

        this.setSpriteForAge(spriteProvider);

        this.velocityX = xSpeed;
        this.velocityY = ySpeed;
        this.velocityZ = zSpeed;
        this.x = x;
        this.y = y;
        this.z = z;
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.scale = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);

        float color_range = 0.3f;
        float reverse_range = 1.0f - color_range;

        this.red = this.random.nextFloat() * color_range + reverse_range;
        this.green = this.random.nextFloat() * color_range + reverse_range;
        this.blue = this.random.nextFloat() * color_range + reverse_range;
        this.maxAge = (int)(Math.random() * (double)10.0F) + 40;

    }


    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            float f = (float)this.age / (float)this.maxAge;
            float g = f;
            f = -f + f * f * 2.0F;
            f = 1.0F - f;
            double speedScale = 2;
            this.x = this.startX + this.velocityX * speedScale * (double)f;
            this.y = this.startY + this.velocityY * speedScale * (double)f + (double)(1.0F - g);
            this.z = this.startZ + this.velocityZ * speedScale * (double)f;
        }
    }

    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    public float getSize(float tickDelta) {
        float f = ((float)this.age + tickDelta) / (float)this.maxAge;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        return this.scale * f;
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
        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new FrogJumpParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
