package frc.friarLib3.Drivers;

import java.util.Objects;

public class CANDeviceID
{
    private final int DeviceNumber;
    private final String Bus;

    public CANDeviceID(int deviceNumber,String bus)
    {
        DeviceNumber = deviceNumber;
        Bus = bus;
    }

    // Use default bus name (empty String)
    public CANDeviceID(int deviceNumber) { this(deviceNumber, ""); }

    public int GetDeviceNumber() { return DeviceNumber; }

    public String GetBus() { return Bus; }

    public Boolean Equals(CANDeviceID other) { return other.DeviceNumber == DeviceNumber && Objects.equals(other.Bus, Bus); }
}
