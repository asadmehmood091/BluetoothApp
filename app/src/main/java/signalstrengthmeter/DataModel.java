package signalstrengthmeter;

import android.content.ContentValues;

import java.util.UUID;

import signalstrengthmeter.database.MacAddressTable;

public class DataModel {
    String deviceName;
    String macAddress;
    String signal;
    String macId;
    String mac;
    String type;

    public DataModel() {
    }

    public DataModel(String macId, String mac, String type) {

        if (macId == null) {
            macId = UUID.randomUUID().toString();
        }
    }

    public DataModel(String deviceName, String macAddress, String signal, String macId, String mac, String type) {
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.signal = signal;
        this.macId = macId;
        this.mac = mac;
        this.type = type;
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

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getsignal() {
        return signal;
    }

    public String getmacAddress() {
        return macAddress;
    }

    public ContentValues getValues() {
        ContentValues values = new ContentValues(3);

        values.put(MacAddressTable.COLUMN_ID, this.macId);
        values.put(MacAddressTable.COLUMN_MAC, this.mac);
        values.put(MacAddressTable.COLUMN_TYPYE, this.type);

        return values;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "macId='" + macId + '\'' +
                ", mac='" + mac + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
