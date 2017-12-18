package com.tigertian.bluetoothprinter.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.tigertian.bluetoothprinter.bluetooth.BtUtil;

import java.util.Set;


/**
 * @author tianlu
 */
public class BindHelper {

    private static final String FILENAME = "BT_CONFIG";
    private static final String DEFAULT_BLUETOOTH_DEVICE_ADDRESS = "DEFAULT_BLUETOOTH_DEVICE_ADDRESS";
    private static final String DEFAULT_BLUETOOTH_DEVICE_NAME = "DEFAULT_BLUETOOTH_DEVICE_NAME";

    public static void setDefaultBluetoothDeviceAddress(Context mContext, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, value);
        editor.apply();
    }

    public static void setDefaultBluetoothDeviceName(Context mContext, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_BLUETOOTH_DEVICE_NAME, value);
        editor.apply();
    }

    /**
     * Check if bind the printer
     * @param mContext
     * @param bluetoothAdapter
     * @return
     */
    public static boolean isBondPrinter(Context mContext, BluetoothAdapter bluetoothAdapter) {
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            return false;
        }
        String defaultBluetoothDeviceAddress = getDefaultBluethoothDeviceAddress(mContext);
        if (TextUtils.isEmpty(defaultBluetoothDeviceAddress)) {
            return false;
        }
        Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
        if (deviceSet == null || deviceSet.isEmpty()) {
            return false;
        }
        for (BluetoothDevice bluetoothDevice : deviceSet) {
            if (bluetoothDevice.getAddress().equals(defaultBluetoothDeviceAddress)) {
                return true;
            }
        }
        return false;

    }

    public static String getDefaultBluethoothDeviceAddress(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, "");
    }

    public static String getDefaultBluetoothDeviceName(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_NAME, "");
    }

    /**
     * use new api to reduce file operate
     *
     * @param editor editor
     */
    public static void apply(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
