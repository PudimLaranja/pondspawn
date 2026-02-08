package com.origin.pondspawn.util;

import com.origin.pondspawn.debug.DebugArguments;
import net.minecraft.util.math.Vec3d;

public class VecMath {
    public static Vec3d vecClamp(Vec3d vec, double max){

        if (vec.lengthSquared() > max * max) {
            return vec.normalize().multiply(max);
        }

        return vec;
    }

    public static Vec3d vecToSphere(Vec3d velocity, Vec3d origin, Vec3d sphereOrigin, double sphereRadius) {

        Vec3d vec = velocity == Vec3d.ZERO ? sphereOrigin.subtract(origin).normalize().multiply(0.4) : velocity;

        Vec3d vecPos = vec.add(origin);

        Vec3d vecInSphere = vecPos.subtract(sphereOrigin).normalize().multiply(sphereRadius);

        Vec3d vecInSpherePos = sphereOrigin.add(vecInSphere);

        return vecInSpherePos.subtract(origin).normalize().multiply(velocity.length() + 0.05);


    }

}
