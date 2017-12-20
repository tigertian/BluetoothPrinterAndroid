package com.tigertian.test;

import android.text.TextUtils;

import com.tigertian.bluetoothprinter.bluetooth.BtUtil;
import com.tigertian.bluetoothprinter.printer.BondHelper;

/**
 * @author tianlu
 */
public class BluetoothController {

    public static void init(MainActivity activity) {
        if (!BtUtil.supportBt()) {
            activity.mTvBluename.setText("No bt module");
            return;
        }
        if (BtUtil.isClosed()) {
            BtUtil.enable();
        }
        String address = BondHelper.getDefaultBluethoothDeviceAddress(activity.getApplicationContext());
        if (TextUtils.isEmpty(address)) {
            activity.mTvBluename.setText("bt bonded");
            return;
        }
        String name = BondHelper.getDefaultBluetoothDeviceName(activity.getApplicationContext());
        activity.mTvBluename.setText("bond bt：" + name);
        activity.mTvBlueAddress.setText(address);
    }
}
