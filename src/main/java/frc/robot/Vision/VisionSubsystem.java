package frc.robot.Vision;

import dev.doglog.DogLog;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.Configs.RobotConfig;
import frc.robot.Subsystems.FMS.FMSSubsystem;
import frc.robot.Subsystems.IMU.IMUSubsystem;
import frc.robot.Vision.Interpolation.InterpolatedVision;

import frc.friarLib3.Scheduling.GameSubsystem;
import frc.friarLib3.Scheduling.SubsystemPriority;

import java.util.Optional;

public class VisionSubsystem extends GameSubsystem
{
    private static final double FloorSpotMaxDistanceSubwoofer = 14.0;
    private static final boolean ShootToSideEnabled = true;
    public static final boolean LimelightUpsideDown = false;

    public static final Pose2d OgRedSpeaker =
        new Pose2d(
            Units.inchesToMeters(652.73), Units.inchesToMeters(218.42), Rotation2d.fromDegrees(180));
    public static final Pose2d OgBlueSpeaker =
        new Pose2d(Units.inchesToMeters(0), Units.inchesToMeters(218.42), Rotation2d.fromDegrees(0));

    public static final Pose2d RedFloorSpotSubwoofer =
        new Pose2d(15.9, 7.9, Rotation2d.fromDegrees(180));
    public static final Pose2d BlueFloorSpotSubwoofer =
        new Pose2d(0.6, 7.9, Rotation2d.fromDegrees(0));

    public static final Pose2d RedFloorSpotAmpArea =
        new Pose2d(9.5, 7, Rotation2d.fromDegrees(180));
    public static final Pose2d BlueFloorSpotAmpArea = new Pose2d(7, 7, Rotation2d.fromDegrees(0));

    private static final Pose2d RedFloorShotFallback =
        new Pose2d(new Translation2d(16.54 / 2.0 - 0.5, 1.25), new Rotation2d());
    private static final Pose2d BlueFloorShootFallback =
        new Pose2d(new Translation2d(16.54 / 2.0 + 0.5, 1.25), new Rotation2d());

    private final Timer limelightTimer = new Timer();
    private double limelightHeartbeat = -1;

    private final InterpolatingDoubleTreeMap distanceToDev = new InterpolatingDoubleTreeMap();
    private final InterpolatingDoubleTreeMap angleToSideOffset = new InterpolatingDoubleTreeMap();

    private final IMUSubsystem imu;

    private Optional<VisionResult> getRawVisionResult()
    {
        var estimatePose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("");

        if (estimatePose == null) { return Optional.empty(); }

        if (estimatePose.tagCount == 0) { return Optional.empty(); }

        // Prevents crazy posing if Limelight power is lost
        if (estimatePose.pose.getX() == 0.0 && estimatePose.pose.getY() == 0.0) { return Optional.empty(); }

        return Optional.of(new VisionResult(estimatePose.pose, estimatePose.timestampSeconds));
    }

    /**
     * @return an interpolated vision pose, ready to be added to estimator
     */
    public Optional<VisionResult> getVisionResults()
    {
        var maybeRawDate = getRawVisionResult();

        if (RobotConfig.Get().perfToggles().InterpolatedVision() && maybeRawDate.isPresent())
        {
            var rawData = maybeRawDate.get();
            Pose2d interpolatedPose = InterpolatedVision.InterpolatePose(rawData.pose());

            DogLog.log("Localization/InterpolatedPose", interpolatedPose);

            return Optional.of(new VisionResult(interpolatedPose, rawData.timestamp()));
        }

        return maybeRawDate;
    }

    public static DistanceAngle distanceAngleToTarget(Pose2d target, Pose2d current)
    {
        double distance = target.getTranslation().getDistance(current.getTranslation());
        double angle =
            Units.radiansToDegrees(
                Math.atan2(target.getY() - current.getY(), target.getX() - current.getX())
            );

        return new DistanceAngle(distance, angle, false);
    }

    public static double angleToTarget(Translation2d current, Translation2d target)
    {
        return Units.radiansToDegrees(
            Math.atan2(target.getY() - current.getY(), target.getX() - current.getX())
        );
    }

    private Pose2d robotPose = new Pose2d();

    public VisionSubsystem(IMUSubsystem imu)
    {
        super(SubsystemPriority.VISION);
        this.imu = imu;

        distanceToDev.put(1.0, 0.4);
        distanceToDev.put(5.0, 6.0);
        distanceToDev.put(3.8, 2.6);
        distanceToDev.put(4.5, 3.0);
        distanceToDev.put(3.37, 2.45);
        distanceToDev.put(7.0, 10.0);

        angleToSideOffset.put(Units.degreesToRadians(100), Units.degreesToRadians(5.0));
        angleToSideOffset.put(Units.degreesToRadians(40), Units.degreesToRadians(1.75));
        angleToSideOffset.put(Units.degreesToRadians(30), Units.degreesToRadians(0.0));
        angleToSideOffset.put(Units.degreesToRadians(0.0), Units.degreesToRadians(0.0));

        limelightTimer.start();
    }

    private static Pose2d getSpeaker()
    {
        if (FMSSubsystem.IsRedAlliance())
        {
            return OgRedSpeaker;
        }
        else
        {
            return OgBlueSpeaker;
        }
    }

    public DistanceAngle getDistanceAngleSpeaker()
    {
        Pose2d speakerPose = getSpeaker();
        DistanceAngle distanceAngleToSpeaker = distanceAngleToTarget(speakerPose, robotPose);
        DistanceAngle adjustForSideDistanceAngle = adjustForSidShot(distanceAngleToSpeaker);

        DogLog.log("Localization/DistanceToSpeaker", adjustForSideDistanceAngle.distance());

        return adjustForSideDistanceAngle;
    }

    private DistanceAngle adjustForSidShot(DistanceAngle originalPosition)
    {
        if (!ShootToSideEnabled) { return originalPosition; }
        double rawAngleDegrees = originalPosition.targetAngle();

        if (!FMSSubsystem.IsRedAlliance()) { rawAngleDegrees += 180; }
        double angleDegrees = MathUtil.inputModulus(rawAngleDegrees, -180.0, 180.0);

        double absoluteOffsettRadians =
            (angleToSideOffset.get(Units.degreesToRadians(Math.abs(angleDegrees))));

        double offsetRadians = Math.copySign(absoluteOffsettRadians, angleDegrees);
        DogLog.log("Vision/ShootToSide/Offset", Units.radiansToDegrees(offsetRadians));

        var adjustedAngle =
            Units.radiansToDegrees(
                Units.degreesToRadians(originalPosition.targetAngle()) + offsetRadians
            );

        return new DistanceAngle(
            originalPosition.distance(), adjustedAngle, originalPosition.seesSpeakerTag()
        );
    }

    public DistanceAngle getDistanceAngleFloorShot()
    {
        Pose2d goalPoseSubwoofer;
        Pose2d goalPoseAmpArea;
        Pose2d fallbackPose;

        if (FMSSubsystem.IsRedAlliance())
        {
            goalPoseSubwoofer = RedFloorSpotSubwoofer;
            goalPoseAmpArea = RedFloorSpotAmpArea;
            fallbackPose = RedFloorShotFallback;
        } else
        {
            goalPoseSubwoofer = BlueFloorSpotSubwoofer;
            goalPoseAmpArea = BlueFloorSpotAmpArea;
            fallbackPose =BlueFloorShootFallback;
        }

        fallbackPose = new Pose2d(fallbackPose.getTranslation(), robotPose.getRotation());
        var usedRobotPose = getState() == VisionState.Offline ? fallbackPose : robotPose;

        var subwooferSpotDistance = distanceAngleToTarget(goalPoseSubwoofer, usedRobotPose);
        var usedGoalPose = goalPoseSubwoofer;
        var result = subwooferSpotDistance;

        if (subwooferSpotDistance.distance() > FloorSpotMaxDistanceSubwoofer)
        {
            result = distanceAngleToTarget(goalPoseAmpArea, usedRobotPose);
            usedGoalPose = goalPoseAmpArea;
            result = new DistanceAngle(581.0, result.targetAngle(), false);
        }

        if (RobotConfig.IsDevelopment)
        {
            DogLog.log("Vision/FloorShot/SubwooferPose", goalPoseSubwoofer);
            DogLog.log("Vision/FloorShot/AmpAreaPose", goalPoseAmpArea);
        }

        DogLog.log("Vision/FloorShot/UsedTargetPose", usedGoalPose);

        return result;
    }

    public double getStandardDeviation(double distance)
    {
        return distanceToDev.get(distance);
    }

    public void setRobotPose(Pose2d pose) { robotPose = pose; }

    @Override
    public void robotPeriodic()
    {
        if (FMSSubsystem.IsRedAlliance()) { LimelightHelpers.setPriorityTagID("", 4); }
        else { LimelightHelpers.setPriorityTagID("", 7); }

        DogLog.log("Vision/State", getState());

        var newHeartbeat = LimelightHelpers.getLimelightNTDouble("", "hb");

        if (limelightHeartbeat == newHeartbeat)
        {// No new data, Limelight dead?
        } else {
            limelightTimer.restart();
        }

        limelightHeartbeat = newHeartbeat;

        LimelightHelpers.SetRobotOrientation(
            "",
            imu.getRobotHeading(),
            imu.getRobotAngularVelocity(),
            imu.getPitch(),
            imu.getPitchRate(),
            imu.getRoll(),
            imu.getRollRate()
        );
    }

    public VisionState getState()
    {
        if (limelightTimer.hasElapsed(5))
        {
            // Heartbeat hasn't updated
            return VisionState.Offline;
        }

        if (getVisionResults().isPresent())
        {
            return VisionState.SeesTags;
        }
        return VisionState.OnlineNoTags;
    }

}
