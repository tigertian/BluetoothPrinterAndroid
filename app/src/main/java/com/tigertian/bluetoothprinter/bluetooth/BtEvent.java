package com.tigertian.bluetoothprinter.bluetooth;

import android.content.Intent;

/**
 * Created by tianlu on 2017/12/19.
 */
public class BtEvent {
    /**
     * BluetoothAdapter.ACTION_DISCOVERY_STARTED
     * BluetoothAdapter.ACTION_DISCOVERY_FINISHED
     * BluetoothAdapter.ACTION_STATE_CHANGED
     * BluetoothDevice.ACTION_FOUND
     * BluetoothDevice.ACTION_BOND_STATE_CHANGED
     * BluetoothDevice.ACTION_PAIRING_REQUEST
     */
    public String action;

    /**
     * The received intent
     */
    public Intent intent;

    public BtEvent(Intent in){
        action = intent.getAction();
        intent = in;
    }
}
