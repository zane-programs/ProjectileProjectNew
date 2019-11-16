public class Projectile {
    private final double GRAVITY_ACCELERATION = -9.81;

    private double launchVelocity, launchAngle, launchVelocityX, launchVelocityY;

    // constructor
    public Projectile(double launchVelocity, double launchAngle) {
        this.launchVelocity = launchVelocity;
        this.launchAngle = launchAngle;

        // Math.PI / 180 converts degrees to radians for Math.sin and Math.cos
        launchVelocityX = launchVelocity * Math.cos(launchAngle * (Math.PI / 180));
        launchVelocityY = launchVelocity * Math.sin(launchAngle * (Math.PI / 180));
    }

    // methods
    public double getXPosAtTime(double time) {
        double flightTime = this.getFlightTime();
        if (time <= flightTime) {
            return launchVelocityX * time;
        } else {
            return this.getXPosAtTime(this.getFlightTime());
        }
    }

    public double getYPosAtTime(double time) {
        double flightTime = this.getFlightTime();
        if (time < flightTime) {
            return (launchVelocityY * time) + 0.5 * GRAVITY_ACCELERATION * time * time;
        } else {
            return 0;
        }
    }

    public double getFlightTime() {
        return -2 * launchVelocityY / GRAVITY_ACCELERATION;
    }

    public double getPeakHeight() {
        return this.getYPosAtTime(this.getFlightTime() / 2);
    }
}