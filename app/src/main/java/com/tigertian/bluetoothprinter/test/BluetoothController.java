package com.tigertian.bluetoothprinter.test;

import android.bluetooth.BluetoothAdapter;
import android.text.TextUtils;
import android.util.Log;

import com.tigertian.bluetoothprinter.MainActivity;
import com.tigertian.bluetoothprinter.printer.BindHelper;

/**
 * @author tianlu
 */
public class BluetoothController {

    public static void init(MainActivity activity) {
        if (null == activity.mAdapter) {
            activity.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (null == activity.mAdapter) {
            activity.mTvBluename.setText("No bt module");
            return;
        }
        if (!activity.mAdapter.isEnabled()) {
            if ( activity.mAdapter.getState()==BluetoothAdapter.STATE_OFF ){
                 activity.mAdapter.enable();

            }else {
                activity.mTvBluename.setText("bt disabled");
                return;
            }
        }
        String address = BindHelper.getDefaultBluethoothDeviceAddress(activity.getApplicationContext());
        if (TextUtils.isEmpty(address)) {
            activity.mTvBluename.setText("bt unbinded");
            return;
        }
        String name = BindHelper.getDefaultBluetoothDeviceName(activity.getApplicationContext());
        activity.mTvBluename.setText("Bound bt：" + name);
        activity.mTvBlueAddress.setText(address);

    }

    public static boolean turnOnBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null)
        {
            return bluetoothAdapter.enable();
        }
        return false;
    }
}
