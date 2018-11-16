package com.sharvari.bluetoothdemo.pojos;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

public class BLE_Device {

    private BluetoothDevice bluetoothDevice;
    private int rssi;
    private String uuid;

    public BLE_Device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }


    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public String getAddress(){
        return bluetoothDevice.getAddress();
    }

    public String getName(){
        return bluetoothDevice.getName();
    }

    public String getUuid(){
        /*return bluetoothDevice.getUuids();*/
        return this.uuid;
    }

    public void setRSSI(int rssi){
        this.rssi = rssi;
    }

    public int getRssi(){
        return rssi;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
