package frc.robot.Subsystems.IMU;

import dev.doglog.DogLog;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.Configs.RobotConfig;
import frc.robot.Subsystems.Swerve.SwerveSubsystem;

import frc.friarLib3.Scheduling.GameSubsystem;
import frc.friarLib3.Scheduling.SubsystemPriority;

public class IMUSubsystem extends GameSubsystem
{
    private final Pigeon2 imu;
    private final InterpolatingDoubleTreeMap distanceToAngleTolerance = new InterpolatingDoubleTreeMap();
    private final TimeInterpolatableBuffer<Double> robotHeadingHistory = TimeInterpolatableBuffer.createDoubleBuffer(3);

    public IMUSubsystem(SwerveSubsystem swerve)
    {
        super(SubsystemPriority.IMU);

        this.imu = swerve.drivetrainPigeon;
        RobotConfig.Get().imu().distanceToAngleTolerance().accept(distanceToAngleTolerance);

        Pigeon2Configuration config =
            new Pigeon2Configuration()
                .withMountPose(
                    new MountPoseConfigs()
                        .withMountPosePitch(0)
                        .withMountPoseRoll(0)
                        .withMountPoseYaw(0)
                );

        imu.getConfigurator().apply(config);
    }

    @Override
    public void robotPeriodic()
    {
        double robotHeading = this.getRobotHeading();

        DogLog.log("IMU/RobotHeading", robotHeading);
        DogLog.log("IMU/RobotHeadingModulo", MathUtil.inputModulus(robotHeading, 0, 360));
        DogLog.log("IMU/RobotHeadingRadians", Units.degreesToRadians(robotHeading));

        var yaw = this.imu.getYaw();
        double yawOffset = Utils.getCurrentTimeSeconds() - yaw.getTimestamp().getTime();

        robotHeadingHistory.addSample(
            Timer.getFPGATimestamp() - yawOffset, Units.degreesToRadians(yaw.getValueAsDouble())
        );
    }

    public double getRobotHeading() { return MathUtil.inputModulus(imu.getYaw().getValueAsDouble(), -180, 180); }

    public double getRobotHeading(double timestamp)
    {
        return Units.radiansToDegrees(
            robotHeadingHistory
                .getSample(timestamp)
                .orElseGet(() -> Units.degreesToRadians(getRobotHeading()))
        );
    }

    public double getPitch() { return imu.getPitch().getValueAsDouble(); }

    public double getPitchRate() { return imu.getAngularVelocityYWorld().getValueAsDouble(); }

    public double getRoll() { return imu.getRoll().getValueAsDouble(); }

    public double getRollRate() { return imu.getAngularVelocityXWorld().getValueAsDouble(); }

    public double getRobotAngularVelocity() { return imu.getAngularVelocityZWorld().getValueAsDouble(); }

    public void setAngle(double zeroAngle) { this.imu.setYaw(zeroAngle); }

   private boolean atAngle(double angle, double tolerance)
   {
       return Math.abs(getRobotHeading() - angle) < tolerance;
   }

   public boolean belowVelocityForVision(double distance)
   {
       return getRobotAngularVelocity() < 10;
   }

   public boolean atAngleForSpeaker(double angle, double distance)
   {
       return atAngle(angle, 2.5);
   }

   public boolean atAngleForFloorSpot(double angle)
   {
       return atAngle(angle, 10);
   }

   public double getYAcceleration()
   {
       return imu.getAccelerationY().getValueAsDouble();
   }

   public double getXAcceleration()
   {
       return imu.getAccelerationX().getValueAsDouble();
   }
}
