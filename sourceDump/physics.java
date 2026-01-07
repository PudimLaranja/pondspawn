if (player != null){
Vec3d tongueAnchorPos = this.getPos();
Vec3d playerPos = player.getPos();
Vec3d directionToTongue = tongueAnchorPos.subtract(playerPos);
double distance = directionToTongue.length();

                if (distance > TONGUE_LENGTH) {
Vec3d currentVelocity = player.getVelocity();

Vec3d unitDirectionToTongue = directionToTongue.normalize();

double extension = distance - TONGUE_LENGTH;
double springStrength = 0.5; // Tune this
Vec3d pullForce = unitDirectionToTongue.multiply(extension * springStrength * (1d/20d));

//                    double radialVelocityMagnitude = currentVelocity.dotProduct(unitDirectionToTongue);
//
//                    if (radialVelocityMagnitude > 0) {
//                        double dampingFactor = 0.8;
//
//                        Vec3d radialDampingForce = unitDirectionToTongue.multiply(-radialVelocityMagnitude * dampingFactor);
//                        pullForce = pullForce.add(radialDampingForce);
//                    }


//                    Vec3d verticalAxis = new Vec3d(0, 1, 0);
//                    Vec3d swingDirection = directionToTongue.crossProduct(verticalAxis).normalize();
//
//                    double currentTangentialSpeed = currentVelocity.dotProduct(swingDirection);
//
//                    double boostStrength = 0.04;
//                    Vec3d boostForce = swingDirection.multiply(boostStrength * Math.signum(currentTangentialSpeed));
//
//                    pullForce = pullForce.add(boostForce);

double max_speed = 20;

                    if (pullForce.length() > 20d) {
pullForce = pullForce.normalize().multiply(max_speed);
                    }

                            player.addVelocity(pullForce);
player.velocityModified = true;
        }
        }
        }