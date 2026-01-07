package com.origin.pondspawn.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class PlayerPhysicsHandler {
    public static Vec3d getDir(Vec3d from, Vec3d to) {
        return to.subtract(from).normalize();
    }

    public static double getDistance(Vec3d from, Vec3d to) {
        return to.subtract(from).length();
    }

    public static Vec3d toVec3d(Vector3f vec) {
        return new Vec3d(
                (double) vec.x,
                (double) vec.y,
                (double) vec.z
        );
    }

    public static Vector3f toVector3f(Vec3d vec) {
        return new Vector3f(
                (float) vec.x,
                (float) vec.y,
                (float) vec.z
        );
    }

    public static Vec3d sphereClamp(Vec3d vec, double radius) {
        if (vec.length() > radius) {
            return vec.normalize().multiply(radius);
        }

        return vec;
    }

    public static void applyVelocityTowards(Entity entity, Vec3d target) {
        Vec3d entityPos = entity.getPos();
        Vec3d directionToTarget = target.subtract(entityPos);
        double distance = directionToTarget.length();


        Vec3d newVelocity = new Vec3d(0,0,0);//entity.getVelocity().lerp(desiredVelocity, 0.4);

        entity.addVelocity(sphereClamp(newVelocity,10d));

        if (entity instanceof PlayerEntity player) {
            // This flag is often required to force synchronization with the client.
            player.velocityModified = true;
        }
    }

    public static void stayInRadios(Entity entity, Vec3d center, double radius) {
        Vec3d entityPos = entity.getPos();
        Vec3d directionToEntity = entityPos.subtract(center);
        double distance = directionToEntity.length();

        // Only apply physics if the player is outside the boundary
        if (distance > radius) {
            // Calculate the ideal position right at the edge of the radius
            Vec3d directionNormalized = directionToEntity.normalize();
            Vec3d idealPos = center.add(directionNormalized.multiply(radius));

            // Calculate the velocity correction needed
            Vec3d correctionVelocity = idealPos.subtract(entityPos).multiply(0.5);

            // Add the correction impulse to the existing velocity
            Vec3d currentVelocity = entity.getVelocity();
            Vec3d newVelocity = currentVelocity.add(correctionVelocity);

            entity.setVelocity(newVelocity);

            if (entity instanceof PlayerEntity player) {
                player.velocityModified = true;
            }
        }
    }
}
