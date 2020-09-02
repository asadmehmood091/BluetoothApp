package signalstrengthmeter;

public class DataModel {
    String deviceName;
    String macAddress;
    String signal;

    DataModel(String deviceName, String macAddress, String signal){
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.signal = signal;
    }

    public void setdeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setsignal(String signal) {
        this.signal = signal;
    }


    public void setmacAddress(String macAddress) {
        this.macAddress = macAddress;
    }


    public String getdeviceName() {
        return deviceName;
    }

    public String getsignal() {
        return signal;
    }

    public String getmacAddress() {
        return macAddress;
    }
}
