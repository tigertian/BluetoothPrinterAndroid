package com.tigertian.test;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SearchBleAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> mDevices;
    private LayoutInflater mInflater;
    private String mConnectedDeviceAddress;
    private float mDensity;

    public SearchBleAdapter(Context mContext, ArrayList<BluetoothDevice> mDevices) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mDevices = null == mDevices ? new ArrayList<BluetoothDevice>() : mDevices;
        mConnectedDeviceAddress = BondHelper.getDefaultBluethoothDeviceAddress(mContext);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mDensity = metrics.density;
    }

    public ArrayList<BluetoothDevice> getDevices() {
        return mDevices;
    }


    public void setDevices(ArrayList<BluetoothDevice> mDevices) {
        if (null == mDevices) {
            mDevices = new ArrayList<BluetoothDevice>();
        }
        this.mDevices = mDevices;
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        if (null != this.mDevices) {
            this.mDevices = sortByBond(this.mDevices);
        }
        super.notifyDataSetChanged();
    }

    private ArrayList<BluetoothDevice> sortByBond(ArrayList<BluetoothDevice> mDevices) {
        if (null == mDevices) {
            return null;
        }
        if (mDevices.size() < 2) {
            return mDevices;
        }
        ArrayList<BluetoothDevice> bondDevices = new ArrayList<BluetoothDevice>();
        ArrayList<BluetoothDevice> unBondDevices = new ArrayList<BluetoothDevice>();
        int size = mDevices.size();
        for (int i = 0; i < size; i++) {
            BluetoothDevice bluetoothDevice = mDevices.get(i);
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                bondDevices.add(bluetoothDevice);
            } else {
                unBondDevices.add(bluetoothDevice);
            }
        }
        mDevices.clear();
        mDevices.addAll(bondDevices);
        mDevices.addAll(unBondDevices);
        bondDevices.clear();
        bondDevices = null;
        unBondDevices.clear();
        unBondDevices = null;
        return mDevices;
    }

    public void setConnectedDeviceAddress(String macAddress) {
        this.mConnectedDeviceAddress = macAddress;
    }

    public void addDevices(ArrayList<BluetoothDevice> mDevices) {
        if (null == mDevices) {
            return;
        }
        for (BluetoothDevice bluetoothDevice : mDevices) {
            addDevices(bluetoothDevice);
        }
    }

    public void addDevices(BluetoothDevice mDevice) {
        if (null == mDevice) {
            return;
        }
        if (!this.mDevices.contains(mDevice)) {
            this.mDevices.add(mDevice);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return mDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.adapter_bt_device, parent, false);
            holder = new ViewHolder();
            if (null != convertView) {
                convertView.setTag(holder);
            }
        }
        holder.name = (TextView) convertView.findViewById(R.id.txt_adapter_bt_name);
        holder.address = (TextView) convertView.findViewById(R.id.txt_adapter_bt_address);
        holder.bond = (TextView) convertView.findViewById(R.id.btn_adapter_bt_has_bond);

        BluetoothDevice bluetoothDevice = mDevices.get(position);
        String dName = bluetoothDevice.getName() == null ? "Unknown Device" : bluetoothDevice.getName();
        if (TextUtils.isEmpty(dName)) {
            dName = "Unknown Device";
        }
        holder.name.setText(dName);
        String dAddress = bluetoothDevice.getAddress() == null ? "Unknown Address" : bluetoothDevice.getAddress();
        if (TextUtils.isEmpty(dAddress)) {
            dAddress = "Unknown Address";
        }
        holder.address.setText(dAddress);
        int paddingVertical = 8;
        int paddingHorizontal = 16;
        if (mDensity != 0) {
            paddingVertical = (int) (paddingVertical * mDensity);
            paddingHorizontal = (int) (paddingHorizontal * mDensity);
        }
        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            if (dAddress.equals(mConnectedDeviceAddress)) {
                holder.bond.setText("Connected");
                holder.bond.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            } else {
                holder.bond.setText("Paired");
                holder.bond.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
            }
        } else {
            holder.bond.setText("Unpaired");
            holder.bond.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView address;
        TextView bond;
    }
}
