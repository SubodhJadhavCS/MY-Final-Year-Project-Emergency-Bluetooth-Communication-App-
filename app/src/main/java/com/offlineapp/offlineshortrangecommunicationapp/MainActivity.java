package com.offlineapp.offlineshortrangecommunicationapp;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final String TAG = "main_activity";
    int status = 0;
    ImageView imageView;
    Uri filesorce;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    TextView textlisteddevices;
    ListView listView;

    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textlisteddevices = findViewById(R.id.listeddevices);
        listView = findViewById(R.id.listview);

        imageView = findViewById(R.id.imagepreview);
        Toast.makeText(this, "User Screen loaded", Toast.LENGTH_SHORT).show();

        //1 bluetooth hardware is present or not
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "device not supported ... ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "bluetooth supported... ", Toast.LENGTH_SHORT).show();
        }


        //2 checking whether there already bluetooth is running or need to start
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "bluetooth is not turned on...", Toast.LENGTH_SHORT).show();
        }
    }


    //3 enable bluetooth
    @SuppressLint("MissingPermission")
    public void enable_bluetoth(View view) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    //4 
    @SuppressLint("MissingPermission")
    public void make_discovery(View view) {
//make device discoverable for 120 sec
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(enableBtIntent, REQUEST_DISCOVERABLE_BT);
    }





    @SuppressLint("MissingPermission")
    public void list_paire_devies(View view) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        String[] strings = new String[pairedDevices.size()];
        int index = 0;

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {

                strings[index] = device.getName();
                index++;
                //listeddevices.setText(device.getName() + "\n"+"\n" + device.getAddress());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, strings);
            listView.setAdapter(arrayAdapter);
        } else {
            textlisteddevices.setText(getString(R.string.none_paired));
        }

    }


    public void select_image(View view) {
        Toast.makeText(this,"Opening Gallery...",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }



    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (status) {
            case 0:
                if (requestCode == PICK_IMAGE) {
                    // Get the url of the image from data
                    try {
                        final Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            // update the preview image in the layout
                            imageView.setImageURI(selectedImageUri);
                             filesorce = selectedImageUri;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onActivityResult: Empty  null pointer invoked");

                        Toast.makeText(this, "you didn't choose anything", LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }

    public void snedimage(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, filesorce);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "btourfile"));

    }


}//end