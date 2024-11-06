package frc.friarLib3.Utility;

import dev.doglog.DogLog;

import edu.wpi.first.hal.HALUtil;

import java.util.HashMap;
import java.util.Map;

public class Stopwatch
{
    private static Stopwatch instance;

    public static synchronized Stopwatch GetInstance()
    {
        if (instance == null) { instance = new Stopwatch(); }

        return instance;
    }

    private static double GetTimestamp() { return HALUtil.getFPGATime() / 1e3; }

    private final Map<String, Double> lastTimestamps = new HashMap<>();

    private Stopwatch() {}

    public void Start(String name) { lastTimestamps.put(name, GetTimestamp()); }

    public void Stop(String name)
    {
        double timestamp = GetTimestamp();
        double lastTimestamp = lastTimestamps.get(name);

        DogLog.log(name, timestamp - lastTimestamp);
    }

    public void Skip(String name) { DogLog.log(name, -1.0); }

}
