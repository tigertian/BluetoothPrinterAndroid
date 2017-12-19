package com.tigertian.test;

import android.text.TextUtils;

import com.tigertian.bluetoothprinter.bluetooth.BtUtil;
import com.tigertian.bluetoothprinter.printer.BindHelper;

/**
 * @author tianlu
 */
public class BluetoothController {

    public static void init(MainActivity activity) {
        if (!BtUtil.hasBtModule()) {
            activity.mTvBluename.setText("No bt module");
            return;
        }
        if (BtUtil.isClosed()) {
            BtUtil.enable();
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
}
