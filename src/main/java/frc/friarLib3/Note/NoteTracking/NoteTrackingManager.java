package frc.friarLib3.Note.NoteTracking;

import dev.doglog.DogLog;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.Configs.RobotConfig;
import frc.robot.Subsystems.Localization.LocalizationSubsystem;
import frc.robot.Subsystems.Swerve.SwerveSubsystem;
import frc.robot.Vision.LimelightHelpers;

import frc.friarLib3.Note.NoteMapping.AutoNoteDropped;
import frc.friarLib3.Scheduling.GameSubsystem;
import frc.friarLib3.Scheduling.SubsystemPriority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NoteTrackingManager extends GameSubsystem
{
    private static final double CameraImageHeight = 960.0; // TODO: Tune
    private static final double CameraImageWidth = 1280.0; // TODO: Tune!
    private static final double NoteLimelightPitch = 0.41; // TODO: Tune!!
    private static final double NoteLimelightHeight = 0.41; // TODO: Tune!!!
    private static final double NoteLimelightForwardDistanceFromCenter = 0.36; // TODO: Tune!!!!

    private static final double NoteMapLifetimeSeconds = 10.0; // TODO: TUNE
    private final LocalizationSubsystem localizationSubsystem;
    private final SwerveSubsystem swerve;

    private static final String LimelightName = "limelight-note";
}
