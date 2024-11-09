package frc.robot.Vision.Interpolation;

import edu.wpi.first.math.geometry.Pose2d;

import frc.robot.Configs.RobotConfig;
import frc.robot.Subsystems.FMS.FMSSubsystem;

public class InterpolatedVision
{
    private static final InterpolatedVisionDataset usedSet =
        RobotConfig.Get().vision().interpolatedVisionSet();

    /**
     * @param visionInput - pose from limelight
     * @return a transformed pose that can be added to pose estimator
     */
    public static Pose2d InterpolatePose(Pose2d visionInput)
    {
        var usedDataPoints = FMSSubsystem.IsRedAlliance() ? usedSet.redSet : usedSet.blueSet;

        return new Pose2d(
            InterpolationUtil.InterpolateTranslation(usedDataPoints, visionInput.getTranslation()),
            visionInput.getRotation()
        );
    }

    private InterpolatedVision() {}
}
