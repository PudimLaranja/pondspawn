package com.origin.pondspawn.entity.renderer;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.include.com.google.common.collect.Iterables;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class Common {
    public static Vec3d getPlayerMouthPosition(PlayerEntity player , float tickDelta) {
        Vec3d eyePos = player.getLerpedPos(tickDelta).add(
                0,
                player.getEyeHeight(player.getPose()) - 0.4,
                0
        );

        float headYaw = (float) Math.toRadians(player.getYaw(tickDelta));
        float headPitch = (float) Math.toRadians(player.getPitch(tickDelta));

        Quaternionf headRotation = new Quaternionf();

        headRotation.rotateY(-headYaw);

        headRotation.rotateX(headPitch);


        Vector3f neckOffset = new Vector3f(0f,0.15f,0f);
        Vector3f mouthOffset = new Vector3f(0f,0f,0.24f);

        neckOffset.rotate(headRotation);
        mouthOffset.rotate(headRotation);

        return eyePos.add(new Vec3d(neckOffset)).add(new Vec3d(mouthOffset));
    }

    public static Entity getEntityByUuid(ClientWorld world, UUID uuid) {

        for (Entity entity : world.getEntities()) {
           if (entity != null && entity.getUuid().equals(uuid)) return entity;
        }

        return null;
    }
}
