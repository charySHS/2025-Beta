package frc.friarLib3.Scheduling;

import edu.wpi.first.wpilibj.IterativeRobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Robot;
import frc.friarLib3.Utility.Stopwatch;

/**
 * Extends {@link SubsystemBase} by adding in lifecycle methods for robotInit, teleopPeriodic, etc.,
 * similar to {@link Robot}.
 */
public class GameSubsystem extends SubsystemBase
{
    final SubsystemPriority priority;

    private final Stopwatch stopwatch = Stopwatch.GetInstance();
    protected final String subsystemName;

    private GameStage previousStage = null;

    public GameSubsystem(SubsystemPriority priority)
    {
        this.priority = priority;

        GameSubsystemManager.GetInstance().RegisterSubsystem(this);

        String name = this.getClass().getSimpleName();
        name = name.substring(name.lastIndexOf('.') + 1);
        // First string concat causes lag spike. This shifts lag spike to code init.
        subsystemName = "Scheduler/GameSubystem" + name + ".periodic()";

        robotInit();
    }

    /** {@link edu.wpi.first.wpilibj.IterativeRobotBase#robotInit()} */
    public void robotInit() {}

    /** {@link IterativeRobotBase#robotPeriodic()} */
    public void robotPeriodic() {}

    public void enabledInit() {}

    public void enabledPeriodic() {}

    /** {@link IterativeRobotBase#autonomousInit()} */
    public void autonomousInit() {}

    /** {@link IterativeRobotBase#autonomousPeriodic()} */
    public void autonomousPeriodic() {}

    /** {@link IterativeRobotBase#teleopInit()} */
    public void teleopInit() {}

    /** {@link IterativeRobotBase#teleopPeriodic()} */
    public void teleopPeriodic() {}

    /** {@link IterativeRobotBase#disabledInit()} */
    public void disabledInit() {}

    /** {@link IterativeRobotBase#disabledPeriodic()} */
    public void disabledPeriodic() {}

    @Override
    public void periodic()
    {
        stopwatch.Start(subsystemName);
        GameStage stage;

        stage = GameSubsystemManager.GetStage();

        boolean isInit = previousStage != stage;

        robotPeriodic();

        if (stage == GameStage.DISABLED) {
            if (isInit) {
                disabledInit();
            }

            disabledPeriodic();
        } else {
            if (isInit) {
                enabledInit();
            }

            enabledPeriodic();

            if (stage == GameStage.TELEOP) {
                if (isInit) {
                    teleopInit();
                }

                teleopPeriodic();
            } else if (stage == GameStage.AUTONOMOUS) {
                if (isInit) {
                    autonomousInit();
                }

                autonomousPeriodic();
            } else if (stage == GameStage.TEST) {
                if (isInit) {
                    testInit();
                }

                testPeriodic();
            }
        }

        stopwatch.Stop(subsystemName);

        previousStage = stage;
    }

    /** {@link IterativeRobotBase#testInit()} */
    public void testInit() {}

    /** {@link IterativeRobotBase#testPeriodic()} */
    public void testPeriodic() {}
}
