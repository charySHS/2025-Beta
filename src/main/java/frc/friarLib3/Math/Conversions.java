package frc.friarLib3.Math;

public class Conversions
{
    /**
     * @param encoderRot Encoder Position: (in Rotations)
     * @param gearRatio Gear Ratio between Encoder and Mechanism
     * @return Mechanism Position: (in Degrees)
     */
    public static double EncoderToDegrees(double encoderRot, double gearRatio)
    {
        double mechRot = encoderRot / gearRatio;

        return mechRot * 360;
    }

    /**
     * @param mechDeg Mechanism Position: (in Degrees)
     * @param gearRatio Gear Ratio between Encoder and Mechanism
     * @return Encoder Rotation: (in Rotations)
     */
    public static double DegreesToEncoder(double mechDeg, double gearRatio)
    {
        double mechRot = mechDeg / 360;

        return mechRot * gearRatio;
    }

    /**
     * @param motorRot Motor Position: (in Rotations)
     * @param gearRatio Gear Ratio between Motor and Mechanism
     * @return Mechanism Position: (in Degrees)
     */
    public static double TalonToDegrees(double motorRot, double gearRatio)
    {
        double motorDeg = motorRot * 360;

        return motorDeg / gearRatio;
    }

    /**
     * @param degrees Mechanism Position: (in Degrees)
     * @param gearRatio Gear Ratio between Motor and Mechanism
     * @return Motor Rotation: (in Rotations)
     */
    public static double DegreesToTalon(double degrees, double gearRatio)
    {
        double motorDeg = degrees * gearRatio;

        return motorDeg / 360;
    }

    /**
     * @param motorRPS Motor Velocity: (in Rotations per Second)
     * @param gearRatio Gear Ratio between Motor and Mechanism
     * @return Mechanism Velocity: (in Rotations per Minute)
     */
    public static double TalonToRPM(double motorRPS, double gearRatio)
    {
        double motorRPM = motorRPS * 60.0;

        return motorRPM /gearRatio;
    }

    /**
     * @param mechRPM Mechanism Velocity: (in Rotations per Minute)
     * @param gearRatio Gear Ratio between Motor and Mechanism
     * @return Motor Velocity: (in Rotations per Second)
     */
    public static double RPMToTalon(double mechRPM, double gearRatio)
    {
        double motorRPM = mechRPM * gearRatio;

        return motorRPM / 60.0;
    }

    /**
     * @param motorRPS Motor Velocity: (in Rotations per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @param gearRatio Gear Ratio between Motor and Mechanism
     * @return Wheel Velocity: (in Meters per Second)
     */
    public static double talonToMPS(double motorRPS, double circumference, double gearRatio)
    {
        double wheelRPS = motorRPS / gearRatio;

        return wheelRPS * circumference;
    }

    /**
     * @param wheelRPM Wheel Velocity: (in Meters per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @param gearRatio Gear Ratio between Motor and Wheel
     * @return Motor Velocity: (in Rotations per Second)
     */
    public static double MPSToTalon(double wheelRPM, double circumference, double gearRatio)
    {
        double wheelRPS = wheelRPM / circumference;

        return wheelRPS * gearRatio;
    }

    /**
     * @param motorRot Motor Position: (in Rotations)
     * @param circumference Wheel Circumference: (in Meters)
     * @param gearRatio Gear Ratio between Motor and Wheel
     * @return Wheel Distance: (in Meters)
     */
    public static double talonToMeters(double motorRot, double circumference, double gearRatio)
    {
        double wheelRotations = motorRot / gearRatio;

        return wheelRotations * circumference;
    }

    /**
     * @param wheelMeters Wheel Distance: (in Meters)
     * @param circumference Wheel Circumference: (in Meters)
     * @param gearRatio Gear Ratio between Motor and Wheel
     * @return Motor Position: (in Rotations)
     */
    public static double MetersToTalon(double wheelMeters, double circumference, double gearRatio)
    {
        double wheelRotations = wheelMeters / circumference;

        return wheelRotations * gearRatio;
    }

    public static double radsToRotations(double rads) {
        return rads * 2 * Math.PI;
    }
    /**
     * @param wheelMPS Wheel Velocity: (in Meters per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Velocity: (in Rotations per Second)
     */
    public static double MPSToRPS(double wheelMPS, double circumference)
    {
        return wheelMPS / circumference;
    }
    /**
     * @param wheelRPS Wheel Velocity: (in Rotations per Second)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Velocity: (in Meters per Second)
     */
    public static double RPSToMPS(double wheelRPS, double circumference)
    {
        return wheelRPS * circumference;
    }
    /**
     * @param wheelRotations Wheel Position: (in Rotations)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Distance: (in Meters)
     */
    public static double rotationsToMeters(double wheelRotations, double circumference)
    {
        return wheelRotations * circumference;
    }

    /**
     * @param wheelMeters Wheel Distance: (in Meters)
     * @param circumference Wheel Circumference: (in Meters)
     * @return Wheel Position: (in Rotations)
     */
    public static double metersToRotations(double wheelMeters, double circumference)
    {
        return wheelMeters / circumference;
    }

}
