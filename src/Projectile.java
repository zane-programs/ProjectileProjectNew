/**
 * Project: ProjectileMotionNew
 * Class: Projectile
 * Due: November 19, 2019
 * <P>This is my projectile motion project. Please see README.md for more information.</P>
 * @author Zane St. John
 * @version 1.0
 */

public class Projectile {
    private final double GRAVITY_ACCELERATION = -9.81;

    private double launchVelocity, launchAngle, launchVelocityX, launchVelocityY;

    // CONSTRUCTOR
    public Projectile(double launchVelocity, double launchAngle) {
        this.launchVelocity = launchVelocity;
        this.launchAngle = launchAngle;

        // Math.PI / 180 converts degrees to radians for Math.sin and Math.cos
        launchVelocityX = launchVelocity * Math.cos(launchAngle * (Math.PI / 180)); // x component of launch velocity
        launchVelocityY = launchVelocity * Math.sin(launchAngle * (Math.PI / 180)); // y component of launch velocity
    }

    // METHODS

    // get x position at given time
    public double getXPosAtTime(double time) {
        double flightTime = this.getFlightTime();
        if (time <= flightTime) {
            return launchVelocityX * time;
        } else {
            return this.getXPosAtTime(this.getFlightTime());
        }
    }

    // get y position at given time
    public double getYPosAtTime(double time) {
        double flightTime = this.getFlightTime();
        if (time < flightTime) {
            return (launchVelocityY * time) + 0.5 * GRAVITY_ACCELERATION * time * time;
        } else {
            return 0;
        }
    }

    // get projectile at flight time
    public double getFlightTime() {
        return -2 * launchVelocityY / GRAVITY_ACCELERATION;
    }
}