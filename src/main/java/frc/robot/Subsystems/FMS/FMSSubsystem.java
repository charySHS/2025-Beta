package frc.robot.Subsystems.FMS;

import dev.doglog.DogLog;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import frc.friarLib3.Scheduling.GameSubsystem;
import frc.friarLib3.Scheduling.SubsystemPriority;

public class FMSSubsystem extends GameSubsystem
{
    public FMSSubsystem()
    {
        super(SubsystemPriority.FMS);
    }

    public static boolean IsRedAlliance()
    {
        Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Red);

        return alliance == Alliance.Red;
    }

    @Override
    public void robotPeriodic()
    {
        DogLog.log("FMS/Alliance", IsRedAlliance() ? "Red" : "Blue");
    }
}
