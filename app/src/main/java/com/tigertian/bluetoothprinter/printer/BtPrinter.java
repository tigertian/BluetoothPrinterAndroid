package com.tigertian.bluetoothprinter.printer;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.tigertian.bluetoothprinter.bluetooth.BtService;
import com.tigertian.bluetoothprinter.bluetooth.BtUtil;
import com.tigertian.bluetoothprinter.bluetooth.StateType;

import java.util.ArrayList;

/**
 * This is PRINT queue. You can simple add PRINT bytes to queue. and this class will send those bytes to bluetooth device
 *
 * @author tianlu
 */
public class BtPrinter {

    private static BtPrinter mInstance = null;

    /**
     * print queue
     */
    private ArrayList<byte[]> mQueue;

    /**
     * bluetooth service
     */
    private BtService mBtService;

    /**
     * bt printer address
     */
    private String mBtAddress;

    private BtPrinter(String btAddress) {
        mBtAddress = btAddress;
    }

    /**
     * get the bt printer
     * @param btAddress
     * @return
     */
    public static BtPrinter getBtPrinter(String btAddress) {
        if(mInstance == null) {
            mInstance = new BtPrinter(btAddress);
        }
        if(!mInstance.mBtAddress.equals(btAddress)){
            mInstance.disconnect();
            mInstance = new BtPrinter(btAddress);
        }
        return mInstance;
    }


    /**
     * add print bytes to queue. and call PRINT
     *
     * @param bytes bytes
     */
    public synchronized void add(byte[] bytes) {
        if (mQueue == null) {
            mQueue = new ArrayList<byte[]>();
        }
        if (bytes != null) {
            mQueue.add(bytes);
        }
        print();
    }

    /**
     * add print job to queue. and call PRINT
     */
    public synchronized void add(ArrayList<byte[]> bytesList) {
        if (mQueue == null) {
            mQueue = new ArrayList<byte[]>();
        }
        if (bytesList != null) {
            mQueue.addAll(bytesList);
        }
        print();
    }

    /**
     * Print queue
     */
    public synchronized void print() {
        try {
            if (mQueue == null || mQueue.size() <= 0) {
                return;
            }
            connect();
            while (mQueue.size() > 0) {
                mBtService.write(mQueue.get(0));
                mQueue.remove(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * when bluetooth status is changed, if the printer is in use,
     * connect it,else do nothing
     */
    public void connect() {
        try {
            if (mBtService == null) {
                mBtService = new BtService();
            }
            if (mBtService.getState() != StateType.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(mBtAddress)) {
                    BluetoothDevice device = BtUtil.getRemoteDevice(mBtAddress);
                    mBtService.connect(device);
                    return;
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * disconnect from printer
     */
    public void disconnect(){
        try {
            if (mBtService != null) {
                mBtService.stop();
                mBtService = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
