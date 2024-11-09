package frc.friarLib3.Scheduling;

public enum SubsystemPriority
{
    // Vision should run before **everything** to ensure fresh cashe data
    VISION(50),

    NOTETRACKING(41),
    AUTOS(40),

    NOTEMANAGER(31),
    ROBOTMANAGER(30),

    CLIMBER(10),
    SWERVE(10),
    IMU(10),
    SHOOTER(10),
    LOCALIZATION(10),
    INTAKE(10),
    QUEUER(10),

    FMS(0),
    RUMBLECONTROLLER(0);

    final int value;

    private SubsystemPriority(int priority) { this.value = priority; }
}
