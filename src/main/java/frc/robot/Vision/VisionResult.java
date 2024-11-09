package frc.robot.Vision;

import edu.wpi.first.math.geometry.Pose2d;

public record VisionResult(Pose2d pose, double timestamp) {}
