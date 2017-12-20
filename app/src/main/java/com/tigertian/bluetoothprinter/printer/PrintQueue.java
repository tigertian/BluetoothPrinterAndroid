package com.tigertian.bluetoothprinter.printer;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
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
public class PrintQueue {

    private static PrintQueue mInstance;
    private Context mContext;
    /**
     * PRINT queue
     */
    private ArrayList<byte[]> mQueue;
    /**
     * bluetooth service
     */
    private BtService mBtService;

    private String mBtAddress;

    private PrintQueue(Context context) {
        if (null == mContext) {
            mContext = context;
            mBtAddress = BondHelper.getDefaultBluethoothDeviceAddress(mContext);
        }
    }

    public static PrintQueue getQueue(Context context) {
        if (null == mInstance) {
            mInstance = new PrintQueue(context);
        }

        return mInstance;
    }

    /**
     * add PRINT bytes to queue. and call PRINT
     *
     * @param bytes bytes
     */
    public synchronized void add(byte[] bytes) {
        if (null == mQueue) {
            mQueue = new ArrayList<byte[]>();
        }
        if (null != bytes) {
            mQueue.add(bytes);
        }
        print();
    }

    /**
     * add PRINT bytes to queue. and call PRINT
     */
    public synchronized void add(ArrayList<byte[]> bytesList) {
        if (null == mQueue) {
            mQueue = new ArrayList<byte[]>();
        }
        if (null != bytesList) {
            mQueue.addAll(bytesList);
        }
        print();
    }

    /**
     * PRINT queue
     */
    public synchronized void print() {
        try {
            if (null == mQueue || mQueue.size() <= 0) {
                return;
            }
            if (!BtUtil.supportBt())
                return;
            if (null == mBtService) {
                mBtService = new BtService(mContext);
            }
            if (mBtService.getState() != StateType.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(mBtAddress)) {
                    BluetoothDevice device = BtUtil.getRemoteDevice(mBtAddress);
                    mBtService.connect(device);
                    return;
                }
            }
            while (mQueue.size() > 0) {
                mBtService.write(mQueue.get(0));
                mQueue.remove(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * disconnect remote device
     */
    public void disconnect() {
        try {
            if (null != mBtService) {
                mBtService.stop();
                mBtService = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * when bluetooth status is changed, if the printer is in use,
     * connect it,else do nothing
     */
    public void tryConnect() {
        try {
            if (TextUtils.isEmpty(mBtAddress)) {
                return;
            }
            if (!BtUtil.supportBt())
                return;
            if (null == mBtService) {
                mBtService = new BtService(mContext);
            }
            if (mBtService.getState() != StateType.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(mBtAddress)) {
                    BluetoothDevice device = BtUtil.getRemoteDevice(mBtAddress);
                    mBtService.connect(device);
                    return;
                }
            } else {


            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the commands to printer
     *
     * @param bytes bytes
     */
    public void write(byte[] bytes) {
        try {
            if (null == bytes || bytes.length <= 0) {
                return;
            }
            if (!BtUtil.supportBt())
                return;
            if (null == mBtService) {
                mBtService = new BtService(mContext);
            }
            if (mBtService.getState() != StateType.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(mBtAddress)) {
                    BluetoothDevice device = BtUtil.getRemoteDevice(mBtAddress);
                    mBtService.connect(device);
                    return;
                }
            }
            mBtService.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
