package com.tigertian.bluetoothprinter.bluetooth;

import android.content.Intent;

/**
 * Callback for bluetooth events
 *
 * @author tianlu
 */
public interface BtInterface {
    /**
     * start discovery bt device
     *
     * @param intent BluetoothAdapter.ACTION_DISCOVERY_STARTED
     */
    void btStartDiscovery(Intent intent);

    /**
     * finish discovery bt device
     *
     * @param intent BluetoothAdapter.ACTION_DISCOVERY_FINISHED
     */
    void btFinishDiscovery(Intent intent);

    /**
     * bluetooth status changed
     *
     * @param intent BluetoothAdapter.ACTION_STATE_CHANGED
     */
    void btStatusChanged(Intent intent);

    /**
     * found bt device
     *
     * @param intent BluetoothDevice.ACTION_FOUND
     */
    void btFoundDevice(Intent intent);

    /**
     * device bond status change
     *
     * @param intent BluetoothDevice.ACTION_BOND_STATE_CHANGED
     */
    void btBondStatusChange(Intent intent);

    /**
     * pairing bluetooth request
     *
     * @param intent BluetoothDevice.ACTION_PAIRING_REQUEST
     */
    void btPairingRequest(Intent intent);
}
