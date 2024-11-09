package frc.robot.Vision.Interpolation;

import edu.wpi.first.math.geometry.Translation2d;

public record VisionInterpolationData(Translation2d measuredPose, Translation2d visionPose, String label) {}
