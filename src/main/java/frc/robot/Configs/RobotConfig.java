package frc.robot.Configs;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TorqueCurrentConfigs;
import com.ctre.phoenix6.swerve.utility.PhoenixPIDController;

import com.revrobotics.*;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.BaseConfig;

import com.revrobotics.spark.config.SparkBaseConfig;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

import frc.robot.Vision.Interpolation.InterpolatedVisionDataset;

import java.util.function.Consumer;

public record RobotConfig(
    String robotName,
    String CANivoreName,
    ShooterConfig shooter,
    ClimbConfig climber,
    IntakeConfig intake,
    QueuerConfig queuer,
    SwerveConfig swerve,
    IMUConfig imu,
    VisionConfig vision,
    PerfToggles perfToggles)
{
    public record ShooterConfig(
        int MotorID,
        TalonFXConfiguration MotorConfig,
        Consumer<InterpolatingDoubleTreeMap> speakerShotRPMS,
        Consumer<InterpolatingDoubleTreeMap> floorShotRMPS
    ) {}

    public record ClimbConfig(
        int LeftMotorID,
        int RightMotorID,
        double HomingCurrentThreshold, // TODO: Figure out what this is
        double HomingVoltage, // TODO: Figure out what this is
        double MinDistance,
        double MaxDistance,
        double RotationsToDistance,
        double DistanceTolerance,
        TalonFXConfiguration LeftMotorConfig,
        TalonFXConfiguration RightMotorConfig
    ) {}

    public record IntakeConfig(
        int MotorID,
        Debouncer Debouncer,
        int CurrentSpikeCount,
        TalonFXConfiguration MotorConfig
    ) {}

    public record QueuerConfig( int MotorID, Debouncer Debouncer, int CurrentSpikeCount ) {}

    public record IMUConfig(
        int DeviceID, Consumer<InterpolatingDoubleTreeMap> distanceToAngleTolerance
    ) {}

    public record SwerveConfig(
        PhoenixPIDController SnapController,
        boolean InvertRotation,
        boolean InvertX,
        boolean InvertY,
        CurrentLimitsConfigs DriveMotorCurrentLimits,
        TorqueCurrentConfigs DriveMotorTorqueCurrentLimits
    ) {}

    public record VisionConfig(
        int TranslationHistoryArraySize,
        double XYStdDev,
        double ThetaStdDev,
        Consumer<InterpolatingDoubleTreeMap> TYToNoteDistance,
        InterpolatedVisionDataset interpolatedVisionDataset
    ) {}

    public record PerfToggles(
        boolean InterpolatedVision, boolean NoteMapInTeleop, boolean NoteMapBoundingBox
    ) {}

    // TODO: Change to false during events
    public static final boolean IsDevelopment = true;

    // TODO: Change to false when not practicing at home
    public static final boolean IsHome = true;

    private static final String PracticeBotSerialNumber = "01234D";
    public static final String SerialNumber = System.getenv("serialnum");
    public static final boolean IsPracticeBot =
        SerialNumber != null && SerialNumber.equals(PracticeBotSerialNumber);

    public static RobotConfig Get() { return CompConfig.competitionBot; }

}
