package frc.friarLib3.Helpers;

public class ControllerHelpers
{
    public static double GetDeadBanded(double joystickVal, double threshold)
    {
        double newJoystickVal = joystickVal;

        if(Math.abs(joystickVal) < threshold)
        {
            newJoystickVal = 0;
        }
        else
        {
            newJoystickVal = GetSign(joystickVal) * (1 / (1 - threshold)) * (Math.abs(joystickVal) - threshold);
        }

        return newJoystickVal;
    }

    private static double GetSign(double num) { return num >= 0 ? 1 : -1; }

    public static double GetExponent(double joystickVal, double exponent)
    {
        return GetSign(joystickVal) * Math.abs(Math.pow(Math.abs(joystickVal), exponent));
    }
}
