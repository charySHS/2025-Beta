package frc.robot.Configs;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.swerve.utility.PhoenixPIDController;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import com.revrobotics.jni.CANSparkJNI;
import com.revrobotics.*;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.BaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkFlexConfigAccessor;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;

import frc.robot.Configs.RobotConfig.ClimbConfig;
import frc.robot.Configs.RobotConfig.IMUConfig;
import frc.robot.Configs.RobotConfig.IntakeConfig;
import frc.robot.Configs.RobotConfig.PerfToggles;
import frc.robot.Configs.RobotConfig.QueuerConfig;
import frc.robot.Configs.RobotConfig.ShooterConfig;
import frc.robot.Configs.RobotConfig.SwerveConfig;
import frc.robot.Configs.RobotConfig.VisionConfig;
import frc.robot.Vision.Interpolation.InterpolatedVisionDataset;

class CompConfig
{
    private static final ClosedLoopRampsConfigs ClosedLoopRamp =
        new ClosedLoopRampsConfigs()
            .withDutyCycleClosedLoopRampPeriod(0.04)
            .withTorqueClosedLoopRampPeriod(0.04)
            .withVoltageClosedLoopRampPeriod(0.04);

    private static final OpenLoopRampsConfigs OpenLoopRamp =
        new OpenLoopRampsConfigs()
            .withDutyCycleOpenLoopRampPeriod(0.04)
            .withTorqueOpenLoopRampPeriod(0.04)
            .withVoltageOpenLoopRampPeriod(0.04);

    public static final RobotConfig competitionBot =
        new RobotConfig(
            "competition",
            "3309-CANivore",
            new ShooterConfig(
                23,
                new TalonFXConfiguration()
                    .withFeedback(new FeedbackConfigs().withSensorToMechanismRatio(1))
                    .withCurrentLimits(
                        new CurrentLimitsConfigs()
                            .withSupplyCurrentLimit(80)
                            .withSupplyCurrentLimitEnable(true)
                    )
                    .withTorqueCurrent(
                        new TorqueCurrentConfigs()
                            .withPeakForwardTorqueCurrent(200)
                            .withPeakReverseTorqueCurrent(0)
                    )
                    .withSlot0(new Slot0Configs().withKP(10).withKI(0).withKD(0).withKS(40).withKA(0).withKV(0.25).withKG(0))
                    .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake))
                    .withClosedLoopRamps(ClosedLoopRamp)
                    .withOpenLoopRamps(OpenLoopRamp),
                speakerDistanceToRPM -> {
                    // TODO: Test these
                    speakerDistanceToRPM.put(1.38, 3000.0);
                    speakerDistanceToRPM.put(2.16, 3000.0);
                    speakerDistanceToRPM.put(2.5, 4000.0);
                    speakerDistanceToRPM.put(3.5, 4000.0);
                    speakerDistanceToRPM.put(4.5, 4500.0);
                    speakerDistanceToRPM.put(5.5, 4500.0);
                    speakerDistanceToRPM.put(6.5, 4800.0);
                    speakerDistanceToRPM.put(7.5, 4800.0);
                    speakerDistanceToRPM.put(9.0, 4800.0);
                },
                floorSpotDistanceToRPM -> {
                    floorSpotDistanceToRPM.put(0.0, 1000.0);
                    floorSpotDistanceToRPM.put(1.0, 1000.0);
                    floorSpotDistanceToRPM.put(1.2, 1500.0);
                    floorSpotDistanceToRPM.put(3.0, 1800.0);
                    floorSpotDistanceToRPM.put(5.8, 2700.0);
                    floorSpotDistanceToRPM.put(6.5, 2700.0);
                    floorSpotDistanceToRPM.put(500.0, 2700.0);
                    floorSpotDistanceToRPM.put(581.0, 3200.0);
                    // Evil hacky way to have alternate shooter RPM for the amp area shot
                }
            ),
            new ClimbConfig(
                1,
                2,
                10,
                -0.5,
                -0.12,
                0.28,
                0.04166666667,
                0.01,
                new TalonFXConfiguration()
                    .withSlot1(new Slot1Configs().withKP(2000).withKI(0).withKD(65).withKS(8).withKA(0).withKV(0).withKG(27))
                    .withFeedback(new FeedbackConfigs().withSensorToMechanismRatio(117.6))
                    .withCurrentLimits(
                        new CurrentLimitsConfigs()
                            .withSupplyCurrentLimit(80)
                            .withSupplyCurrentLimitEnable(true)
                    )
                    .withClosedLoopRamps(ClosedLoopRamp)
                    .withOpenLoopRamps(OpenLoopRamp)
                    .withMotorOutput(
                        new MotorOutputConfigs()
                            .withInverted(InvertedValue.Clockwise_Positive)
                            .withNeutralMode(NeutralModeValue.Brake)
                    )
                    .withMotionMagic(
                        new MotionMagicConfigs()
                            .withMotionMagicAcceleration(0)
                            .withMotionMagicCruiseVelocity(5)
                            .withMotionMagicExpo_kA(3)
                            .withMotionMagicExpo_kV(5)
                            .withMotionMagicJerk(1000)
                    )
                    .withSoftwareLimitSwitch(
                        new SoftwareLimitSwitchConfigs()
                            .withForwardSoftLimitEnable(true)
                            .withForwardSoftLimitThreshold(0.28)
                            .withReverseSoftLimitEnable(true)
                            .withReverseSoftLimitThreshold(-0.12)
                    ),
                new TalonFXConfiguration()
                    .withSlot1(new Slot1Configs().withKP(2000).withKI(0).withKD(65).withKS(8).withKA(0).withKV(0).withKG(27))
                    .withFeedback(new FeedbackConfigs().withSensorToMechanismRatio(117.6))
                    .withCurrentLimits(
                        new CurrentLimitsConfigs()
                            .withSupplyCurrentLimit(80)
                            .withSupplyCurrentLimitEnable(true)
                    )
                    .withClosedLoopRamps(ClosedLoopRamp)
                    .withOpenLoopRamps(OpenLoopRamp)
                    .withMotorOutput(
                        new MotorOutputConfigs()
                            .withInverted(InvertedValue.CounterClockwise_Positive)
                            .withNeutralMode(NeutralModeValue.Brake)
                    )
                    .withMotionMagic(
                        new MotionMagicConfigs()
                            .withMotionMagicAcceleration(0)
                            .withMotionMagicCruiseVelocity(5)
                            .withMotionMagicExpo_kA(3)
                            .withMotionMagicExpo_kV(5)
                            .withMotionMagicJerk(1000)
                    )
                    .withSoftwareLimitSwitch(
                        new SoftwareLimitSwitchConfigs()
                            .withForwardSoftLimitEnable(true)
                            .withForwardSoftLimitThreshold(0.28)
                            .withReverseSoftLimitEnable(true)
                            .withReverseSoftLimitThreshold(-0.12)
                    )
            ),
            new IntakeConfig(
                23,
                new Debouncer(0.025, DebounceType.kBoth),
                0,
                new TalonFXConfiguration()
                    .withSlot0(
                        new Slot0Configs()
                            .withKP(10)
                            .withKI(0)
                            .withKD(0)
                            .withKS(40)
                            .withKA(0)
                            .withKV(0.25)
                            .withKG(0)
                    )
                    .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake))
                    .withCurrentLimits(
                        new CurrentLimitsConfigs()
                            .withSupplyCurrentLimit(50)
                            .withSupplyCurrentLimitEnable(true)
                    )
                    .withOpenLoopRamps(OpenLoopRamp)
                    .withClosedLoopRamps(ClosedLoopRamp)
            ),
            new QueuerConfig(
                2,
                new Debouncer(0.0, DebounceType.kBoth),
                0
            ),
            new SwerveConfig(
                new PhoenixPIDController(20, 0, 2),
                true,
                true,
                true,
                new CurrentLimitsConfigs()
                    .withStatorCurrentLimit(40)
                    .withSupplyCurrentLimit(40)
                    .withSupplyCurrentLimitEnable(true)
                    .withStatorCurrentLimitEnable(true),
                new TorqueCurrentConfigs()
                    .withPeakForwardTorqueCurrent(80)
                    .withPeakReverseTorqueCurrent(-80)
            ),
            new IMUConfig(
                1,
                distanceToAngleTolerance -> {
                    distanceToAngleTolerance.put(1.0, 2.5);
                    distanceToAngleTolerance.put(1.0, 2.5);
                }
            ),
            new VisionConfig(4, 0.4, 0.4, InterpolatedVisionDataset.MTTD),
            new PerfToggles(true, false, false)
        );

    private CompConfig() {}
}
