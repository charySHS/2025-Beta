package frc.friarLib3.Helpers;

import dev.doglog.DogLog;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

// Class to validify use and input from controllers

public class XboxStalker
{
    public static void Stalk(CommandXboxController driver, CommandXboxController operator)
    {
        DogLog.log("DriverStatus", driver.getHID().getButtonCount() > 0);
        DogLog.log("OperatorStatus", operator.getHID().getButtonCount() > 0 );
    }
}
