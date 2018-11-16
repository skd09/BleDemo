package com.sharvari.bluetoothdemo.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sharvari.bluetoothdemo.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.logging.LogManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BeaconTransmitterFragment extends Fragment {

    private static final String TAG = "Beacon Transmitter";
    private EditText etUuid, etMajor, etMinor;
    private Spinner spinnerType;
    private Beacon beacon = null;
    private BeaconParser beaconParser;
    private BeaconTransmitter beaconTransmitter;
    private Button btnTransmit;

    public BeaconTransmitterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_beacon_transmitter, container, false);
        etUuid = v.findViewById(R.id.et_uuid);
        etMajor = v.findViewById(R.id.et_major);
        etMinor = v.findViewById(R.id.et_minor);
        spinnerType = v.findViewById(R.id.spinnerType);

        ArrayList<String> type = new ArrayList<>();
        type.add("iBeacon");
        type.add("Alt Beacon");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), R.layout.spinner_header_item, type);
        adapter.setDropDownViewResource( R.layout.spinner_header_item);

        spinnerType.setAdapter(adapter);

        btnTransmit =  v.findViewById(R.id.btn_transmit);
        btnTransmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                transmitter();
            }
        });

        v.findViewById(R.id.btnRandom).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                etUuid.setText(UUID.randomUUID().toString());
            }
        });

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void transmitter() {

        if (beacon != null && beaconTransmitter != null) {
            Toast.makeText(getContext(), "Beacon advertising stopped", Toast.LENGTH_LONG);
            btnTransmit.setText("START ADVERTISING");
            beaconTransmitter.stopAdvertising();
            beacon = null;
            return;
        }

        beacon = new Beacon.Builder()
                .setId1(etUuid.getText().toString().isEmpty() ? "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6" :
                        etUuid.getText().toString())
                .setId2(etMinor.getText().toString().isEmpty() ? "2" : etMinor.getText().toString())
                .setId3(etMajor.getText().toString().isEmpty() ? "8" : etMajor.getText().toString())
                .setManufacturer(spinnerType.getSelectedItemPosition() == 0 ? 0x004C : 0x0118)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[]{0l}))
                .build();

        String type = spinnerType.getSelectedItemPosition() == 0 ? "004C" : "beac";

        beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=" + type + ",i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        beaconTransmitter = new BeaconTransmitter(getContext(), beaconParser);
        beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        beaconTransmitter.setBeacon(beacon);
        beaconTransmitter.setBeaconParser(beaconParser);
        beaconTransmitter.setAdvertiseTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        beaconTransmitter.setConnectable(true);

        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

           @Override
           public void onStartFailure(int errorCode) {
               Log.e(TAG, "Advertisement start failed with code: "+errorCode);
           }

           @Override
           public void onStartSuccess(AdvertiseSettings settingsInEffect) {
               Log.i(TAG, "Advertisement start succeeded.");
               btnTransmit.setText("STOP ADVERTISING");
               Toast.makeText(getContext(), "Transmitting Signals", Toast.LENGTH_SHORT).show();
           }
       });
    }
}
