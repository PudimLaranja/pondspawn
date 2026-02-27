package com.origin.pondspawn.globalconfig;

import net.minecraft.util.math.Vec3d;

public class PlayerPhysicsConfig {
    public static double pullMultiplier = 0.8;
    public static double lockMultiplier = 1.0;
    public static double tangentMultiplier = 1.03;

    public static Vec3d jumpVector = new Vec3d(0,0.6,0.2);
}
