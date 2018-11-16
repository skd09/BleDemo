package com.sharvari.bluetoothdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharvari.bluetoothdemo.pojos.BLE_Device;
import com.sharvari.bluetoothdemo.R;

import java.util.ArrayList;

public class BLEDevicesRecyclerviewAdapter extends RecyclerView.Adapter<BLEDevicesRecyclerviewAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<BLE_Device> deviceList;

    public BLEDevicesRecyclerviewAdapter(Context context, ArrayList<BLE_Device> deviceList){
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public BLEDevicesRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ble_device_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BLEDevicesRecyclerviewAdapter.MyViewHolder holder, int position) {
        BLE_Device device = deviceList.get(position);
        holder.txtDeviceName.setText(device.getName() != null && !device.getName().isEmpty()?
                    device.getName() : device.getUuid() !=  null? device.getUuid() : "No name");
        holder.txtDeviceRssi.setText(device.getRssi()+"");
        holder.txtDeviceId.setText(device.getAddress() != null && device.getAddress().length() > 0 ?
                device.getAddress() : "No address");
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDeviceName, txtDeviceRssi, txtDeviceId;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtDeviceName = itemView.findViewById(R.id.txtDeviceName);
            txtDeviceRssi = itemView.findViewById(R.id.txtDeviceRssi);
            txtDeviceId = itemView.findViewById(R.id.txtDeviceId);
        }
    }
}
