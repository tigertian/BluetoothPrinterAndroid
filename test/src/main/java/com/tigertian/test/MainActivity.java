package com.tigertian.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tigertian.bluetoothprinter.bluetooth.BtActivity;
import com.tigertian.bluetoothprinter.bluetooth.BtUtil;
import com.tigertian.bluetoothprinter.bluetooth.StateEvent;
import com.tigertian.bluetoothprinter.printer.BondHelper;

import de.greenrobot.event.EventBus;


public class MainActivity extends BtActivity implements View.OnClickListener {

    public TextView mTvBluename;
    public TextView mTvBlueAddress;
    /**
     * bluetooth adapter
     */
    String mBtAddress;
    String mBtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvBluename = findViewById(R.id.tvBluename);
        mTvBlueAddress = findViewById(R.id.tvBlueAddress);
        findViewById(R.id.buttonSearch).setOnClickListener(this);
        findViewById(R.id.buttonPrint).setOnClickListener(this);

        EventBus.getDefault().register(MainActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        BluetoothController.init(this);
    }


    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        BluetoothController.init(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonSearch){
            startActivity(new Intent(MainActivity.this, SearchBluetoothActivity.class));
        } else if (view.getId() == R.id.buttonPrint) {
            mBtAddress = BondHelper.getDefaultBluethoothDeviceAddress(this);
            if (TextUtils.isEmpty(mBtAddress)) {
                ToastUtil.showToast(MainActivity.this, "Please connect to the bt printer");
                startActivity(new Intent(MainActivity.this, SearchBluetoothActivity.class));
            } else {
                //If the bt is off, enable it
                if (BtUtil.isClosed()) {
                    BtUtil.enable();
                    ToastUtil.showToast(MainActivity.this, "Please enable the bt switch");
                } else {
                    ToastUtil.showToast(MainActivity.this, "Printing...");
                    Intent intent = new Intent(getApplicationContext(), BtTestService.class);
                    intent.setAction(BtTestService.ACTION_PRINT_TEST);
                    startService(intent);
                }
            }
        }

    }

    /**
     * handle printer message
     *
     * @param event PRINT msg event
     */
    public void onEventMainThread(StateEvent event) {
        switch(event.type){
            case STATE_NONE:
                //Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
                break;
            case STATE_LISTEN:
                Toast.makeText(this, "Listening", Toast.LENGTH_SHORT).show();
                break;
            case STATE_CONNECTING:
                Toast.makeText(this, "Connecting to printer", Toast.LENGTH_SHORT).show();
                break;
            case STATE_CONNECTED:
                Toast.makeText(this, "Printer connected", Toast.LENGTH_SHORT).show();
                break;
            case STATE_FAILED:
                Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
                break;
            case STATE_LOST:
                Toast.makeText(this, "Lost connection", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(MainActivity.this);
    }
}
