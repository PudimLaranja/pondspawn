@Inject(method = "travel", at = @At("HEAD"))
private void applyTonguePhysics(Vec3d travelVector, CallbackInfo ci) {
    PlayerEntity player = (PlayerEntity) (Object) this;

    if (this.tongueEntity != null && !this.tongueEntity.isRemoved() && !player.getWorld().isClient()) {

        Vec3d tonguePos = this.tongueEntity.getPos();
        Vec3d playerPos = player.getEyePos(); // Use EyePos for accuracy
        Vec3d vecToTongue = tonguePos.subtract(playerPos);

        double currentDist = vecToTongue.length();
        double maxLength = Tongue.TONGUE_LENGTH;

        // 1. Check if the rope is tight
        if (currentDist > maxLength) {

            Vec3d velocity = player.getVelocity();
            Vec3d ropeDir = vecToTongue.normalize(); // Direction FROM player TO hook

            // --- PART 1: The Swing (Energy Conservation) ---

            // Calculate how much we are moving towards/away from hook
            double radialSpeed = velocity.dotProduct(ropeDir);

            // If radialSpeed < 0, we are moving AWAY from the hook.
            // We need to zero that out so the rope doesn't stretch.
            if (radialSpeed < 0) {
                // Get the radial vector (the movement away)
                Vec3d radialVelocity = ropeDir.multiply(radialSpeed);

                // Get the tangent vector (the swing movement)
                Vec3d tangentVelocity = velocity.subtract(radialVelocity);

                // Boost tangent slightly to fight air drag (1.00 - 1.05 range)
                // This keeps you swinging high
                velocity = tangentVelocity.multiply(1.01);
            }

            // --- PART 2: The Fix (Positional Correction) ---

            // Gravity pulls you down every tick, stretching the rope slightly past maxLength.
            // We need a force to pull you back to the correct radius.
            double stretch = currentDist - maxLength;

            if (stretch > 0) {
                // We apply a velocity bias towards the hook.
                // 0.1 to 0.2 is usually smooth. Higher = snappier/jittery.
                double correctionStrength = 0.15;
                Vec3d correction = ropeDir.multiply(stretch * correctionStrength);

                // Add the correction to the swing velocity
                velocity = velocity.add(correction);
            }

            // Apply final velocity
            player.setVelocity(velocity);
            player.velocityModified = true;

            // Prevent fall damage while swinging
            player.fallDistance = 0f;
        }
    }
}