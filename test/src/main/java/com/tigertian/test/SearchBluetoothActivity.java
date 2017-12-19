package com.tigertian.test;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
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
import com.tigertian.bluetoothprinter.printer.BindHelper;
import com.tigertian.bluetoothprinter.printer.PrintQueue;

import java.lang.reflect.Method;

/**
 * @author tianlu
 */
public class SearchBluetoothActivity extends BtActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private BluetoothAdapter mBluetoothAdapter;
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
        //初始化蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
            mTvTitle.setText("未连接蓝牙打印机");
            mTvSummary.setText("系统蓝牙已关闭,点击开启");

        } else {
            if (!BindHelper.isBondPrinter(this, mBluetoothAdapter)) {
                //未绑定蓝牙打印机器
                mTvTitle.setText("未连接蓝牙打印机");
                mTvSummary.setText("点击后搜索蓝牙打印机");

            } else {
                //已绑定蓝牙设备
                mTvTitle.setText(getPrinterName() + "已连接");
                String blueAddress = BindHelper.getDefaultBluethoothDeviceAddress(this);
                if (TextUtils.isEmpty(blueAddress)) {
                    blueAddress = "点击后搜索蓝牙打印机";
                }
                mTvSummary.setText(blueAddress);
            }
        }
}
    @Override
    public void btStatusChanged(Intent intent) {

        if ( mBluetoothAdapter.getState()==BluetoothAdapter.STATE_OFF ){
            mBluetoothAdapter.enable();
        }
        if ( mBluetoothAdapter.getState()==BluetoothAdapter.STATE_ON ){
            searchDeviceOrOpenBluetooth();
        }
    }
    private String getPrinterName(){
        String dName = BindHelper.getDefaultBluetoothDeviceName(this);
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
        if (null != mBluetoothAdapter && device != null) {
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
                .setTitle("Bind to" + getPrinterName(bluetoothDevice.getName()) + "?")
                .setMessage("To bind bt")
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
                            String name = bluetoothDevice.getName();
                        } catch (Exception e) {
                            e.printStackTrace();
                            BindHelper.setDefaultBluetoothDeviceAddress(getApplicationContext(), "");
                            BindHelper.setDefaultBluetoothDeviceName(getApplicationContext(), "");
                            ToastUtil.showToast(SearchBluetoothActivity.this,"蓝牙绑定失败,请重试");
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
        BindHelper.setDefaultBluetoothDeviceAddress(getApplicationContext(), bluetoothDevice.getAddress());
        BindHelper.setDefaultBluetoothDeviceName(getApplicationContext(), bluetoothDevice.getName());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tvSummary)
            searchDeviceOrOpenBluetooth();
    }
}
