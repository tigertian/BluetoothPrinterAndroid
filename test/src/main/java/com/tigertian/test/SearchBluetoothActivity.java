package com.tigertian.test;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tigertian.bluetoothprinter.bluetooth.BtActivity;
import com.tigertian.bluetoothprinter.bluetooth.BtUtil;
import com.tigertian.bluetoothprinter.printer.BondHelper;
import com.tigertian.bluetoothprinter.printer.PrintQueue;

import java.lang.reflect.Method;

/**
 * @author tianlu
 */
public class SearchBluetoothActivity extends BtActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView mLvSearchblt;
    private TextView mTvTitle;
    private TextView mTvSummary;
    private SearchBleAdapter mSearchBleAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbooth);
        mLvSearchblt = (ListView) findViewById(R.id.lvSearchblt);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvSummary = (TextView) findViewById(R.id.tvSummary);
        mSearchBleAdapter = new SearchBleAdapter(SearchBluetoothActivity.this, null);
        mLvSearchblt.setAdapter(mSearchBleAdapter);
        init();
        searchDeviceOrOpenBluetooth();
        mLvSearchblt.setOnItemClickListener(this);
        mTvTitle.setOnClickListener(this);
        mTvSummary.setOnClickListener(this);
    }

    private void init() {
        if (!BtUtil.isOpened()) {
            mTvTitle.setText("no bt printer connected");
            mTvSummary.setText("Click to switch on");

        } else {
            if (!BondHelper.isBondPrinter(this)) {
                mTvTitle.setText("no bt printer connected");
                mTvSummary.setText("Click to search");

            } else {
                mTvTitle.setText(getPrinterName() + " connected");
                String blueAddress = BondHelper.getDefaultBluethoothDeviceAddress(this);
                if (TextUtils.isEmpty(blueAddress)) {
                    blueAddress = "Click to search";
                }
                mTvSummary.setText(blueAddress);
            }
        }
}
    @Override
    public void btStatusChanged(Intent intent) {

        if (BtUtil.isClosed()){
            BtUtil.enable();
        }
        if (BtUtil.isOpened()){
            searchDeviceOrOpenBluetooth();
        }
    }
    private String getPrinterName(){
        String dName = BondHelper.getDefaultBluetoothDeviceName(this);
        if (TextUtils.isEmpty(dName)) {
            dName = "Unknown Device";
        }
        return dName;
    }

    private String getPrinterName(String dName) {
        if (TextUtils.isEmpty(dName)) {
            dName = "Unknown Device";
        }
        return dName;
    }


    private void searchDeviceOrOpenBluetooth() {
        if (BtUtil.isOpened()) {
            BtUtil.searchDevices();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BtUtil.cancelDiscovery();
    }
    @Override
    public void btStartDiscovery(Intent intent) {
        mTvTitle.setText("Searching…");
        mTvSummary.setText("");
    }

    @Override
    public void btFinishDiscovery(Intent intent) {
        mTvTitle.setText("Search done");
        mTvSummary.setText("Click to search");
    }
    @Override
    public void btFoundDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.d("1","!");
        if (device != null) {
            mSearchBleAdapter.addDevices(device);
            String dName = device.getName() == null ? "Unknown Device" : device.getName();
            Log.d("Unknown Device",dName);
        }
    }

    @Override
    public void btBondStatusChange(Intent intent) {
        super.btBondStatusChange(intent);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDING:
                Log.d("BlueToothTestActivity", "Bonding......");
                break;
            case BluetoothDevice.BOND_BONDED:
                Log.d("BlueToothTestActivity", "Bonded");
                connectBlt(device);
                break;
            case BluetoothDevice.BOND_NONE:
                Log.d("BlueToothTestActivity", "None Bond");
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (null == mSearchBleAdapter) {
            return;
        }
        final BluetoothDevice bluetoothDevice = mSearchBleAdapter.getItem(position);
        if (null == bluetoothDevice) {
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Bond to" + getPrinterName(bluetoothDevice.getName()) + "?")
                .setMessage("To bond bt")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BtUtil.cancelDiscovery();

                            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                                connectBlt(bluetoothDevice);
                            } else {
                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                createBondMethod.invoke(bluetoothDevice);
                            }
                            PrintQueue.getQueue(getApplicationContext()).disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                            BondHelper.setDefaultBluetoothDeviceAddress(getApplicationContext(), "");
                            BondHelper.setDefaultBluetoothDeviceName(getApplicationContext(), "");
                            ToastUtil.showToast(SearchBluetoothActivity.this,"Bond failed, please try again");
                        }
                    }
                })
                .create()
                .show();

    }

    private void connectBlt(BluetoothDevice bluetoothDevice) {
        if (null != mSearchBleAdapter) {
            mSearchBleAdapter.setConnectedDeviceAddress(bluetoothDevice.getAddress());
        }
        init();
        mSearchBleAdapter.notifyDataSetChanged();
        BondHelper.setDefaultBluetoothDeviceAddress(getApplicationContext(), bluetoothDevice.getAddress());
        BondHelper.setDefaultBluetoothDeviceName(getApplicationContext(), bluetoothDevice.getName());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tvSummary)
            searchDeviceOrOpenBluetooth();
    }
}
