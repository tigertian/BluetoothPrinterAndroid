package com.tigertian.bluetoothprinter.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import de.greenrobot.event.EventBus;

/**
 * Bluetooth tools
 * @author tianlu
 */
public class BtUtil {

    /**
     * bluetooth broadcast receiver
     */
    private static BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null ) {
                return;
            }
            EventBus.getDefault().post(new BtEvent(intent));
        }
    };

    /**
     * Whether the phone has bt module
     * @return
     */
    public static boolean hasBtModule(){
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    /**
     * enable the bluetooth
     * @return
     */
    public static boolean enable(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return false;
        return adapter.enable();
    }

    /**
     * disable the bluetooth
     * @return
     */
    public static boolean disable(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return false;
        return adapter.disable();
    }

    /**
     * check if the bluetooth switch is opened
     * @return
     */
    public static boolean isOpened() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return false;
        return adapter.isEnabled();
    }

    /**
     * check if the bluetooth switch is closed, omit the intermediate state
     * @return
     */
    public static boolean isClosed() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return false;
        return !adapter.isEnabled() && adapter.getState() == BluetoothAdapter.STATE_OFF;
    }

    /**
     * scan the bluetooth devices
     */
    public static void searchDevices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return;
        adapter.startDiscovery();

    }

    /**
     * cancel the scan
     */
    public static void cancelDiscovery() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null)
            return;
        adapter.cancelDiscovery();

    }

    /**
     * Register the global receiver, using EventBus with BtEvent
     * @param context
     */
    public static void registerGlobalBluetoothReceiver(Context context){
        registerBluetoothReceiver(mBtReceiver, context);
    }

    /**
     * Unregister the global receiver
     * @param context
     */
    public static void unregisterGlobalBluetoothReceiver(Context context){
        unregisterBluetoothReceiver(mBtReceiver, context);
    }


    /**
     * register bluetooth receiver
     *
     * @param receiver bluetooth broadcast receiver
     * @param context context
     */
    public static void registerBluetoothReceiver(BroadcastReceiver receiver, Context context) {
        if (receiver == null || context == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        //start discovery
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //finish discovery
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //bluetooth status change
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //found device
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        //bond status change
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //pairing device
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * unregister bluetooth receiver
     *
     * @param receiver bluetooth broadcast receiver
     * @param context context
     */
    public static void unregisterBluetoothReceiver(BroadcastReceiver receiver, Context context) {
        if (null == receiver || null == context) {
            return;
        }
        context.unregisterReceiver(receiver);
    }

}
