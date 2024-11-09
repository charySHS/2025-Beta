package frc.friarLib3.Scheduling;

import dev.doglog.DogLog;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameSubsystemManager
{
    private static GameSubsystemManager instance;

    public static GameSubsystemManager GetInstance()
    {
        if (instance == null) { instance = new GameSubsystemManager(); }

        return instance;
    }

    public static GameStage GetStage()
    {
        if (DriverStation.isTeleopEnabled()) { return GameStage.TELEOP; }
        else if (DriverStation.isAutonomousEnabled()) { return GameStage.AUTONOMOUS; }
        else if (DriverStation.isTestEnabled()) { return GameStage.TEST; }
        else { return GameStage.DISABLED; }
    }

    private final List<GameSubsystem> subsystems = new ArrayList<>();
    private final CommandScheduler commandScheduler = CommandScheduler.getInstance();

    private GameSubsystemManager() {}

    public void Ready()
    {
        Collections.sort(
            subsystems,
            Comparator.comparingInt((GameSubsystem subsystem) -> subsystem.priority.value)
                .reversed()
        );

        for (GameSubsystem gameSubsystem : subsystems) { commandScheduler.registerSubsystem(gameSubsystem); }
    }

    public void Log()
    {
        DogLog.log("Scheduler/Stage", GetStage().toString());
    }

    void RegisterSubsystem(GameSubsystem subsystem)
    {
        subsystems.add(subsystem);
        commandScheduler.unregisterSubsystem(subsystem);
    }

}
