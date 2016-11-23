package com.app.kitchen.jar.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.app.kitchen.jar.R;
import com.app.kitchen.jar.beans.BlueToothInfo;
import com.app.kitchen.jar.commons.AppLogs;
import com.app.kitchen.jar.commons.AppUtils;
import com.app.kitchen.jar.commons.BluetoothConnector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lalit T. Poptani on 10/24/2016.
 */

public class DeviceListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = DeviceListActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE = 100;
    private BluetoothAdapter mBluetoothAdapter;
    private TextView textViewNoPairedDevices;
    private List<BlueToothInfo> listPairedDevices = new ArrayList<>();
    private ListView listViewPairedDevices;
    private ArrayAdapter<BlueToothInfo> adapterPairedDevices;
    private DeviceListActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        activity = this;

        initToolBar();
        initComponents();
        addListeners();
        initBlueTooth();
        enableBlueTooth();
    }

    @Override
    void initComponents() {
        textViewNoPairedDevices = (TextView) findViewById(R.id.text_view_no_paired_devices);
        listViewPairedDevices = (ListView) findViewById(R.id.list_paired_devices);

    }

    @Override
    void addListeners() {
        adapterPairedDevices = new ArrayAdapter<BlueToothInfo>(this, android.R.layout.simple_list_item_1, listPairedDevices);
        listViewPairedDevices.setAdapter(adapterPairedDevices);
        listViewPairedDevices.setOnItemClickListener(this);
    }

    private void initToolBar() {
        Toolbar toolbarDeviceList = (Toolbar) findViewById(R.id.toolbar_device_list);
        setSupportActionBar(toolbarDeviceList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void enableBlueTooth() {

        if (mBluetoothAdapter == null) {
            Log.e(getClass().getSimpleName(), "Bluetooth not supported");
        } else if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE);
        } else {
            AppLogs.i(TAG, "Bluetooth is already enabled");
            getListOfPairedDevices();
        }
    }

    private void disableBluetooth() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            AppLogs.i(TAG, "Bluetooth is disabled");
        } else if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            AppLogs.i(TAG, "Bluetooth is already disabled");
        }
    }

    private void getListOfPairedDevices() {
        if (mBluetoothAdapter != null) {

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                listPairedDevices.add(new BlueToothInfo(device.getName(), device.getAddress()));
            }

            if (listPairedDevices.size() > 0) {
                textViewNoPairedDevices.setVisibility(View.GONE);
                listViewPairedDevices.setVisibility(View.VISIBLE);
                adapterPairedDevices.notifyDataSetChanged();
            } else {
                listViewPairedDevices.setVisibility(View.GONE);
                textViewNoPairedDevices.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ENABLE) {
            AppLogs.i(TAG, "Bluetooth enabled");
            getListOfPairedDevices();
        } else {
            AppLogs.i(TAG, "Bluetooth disabled");
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            BlueToothInfo blueToothInfo = (BlueToothInfo) adapterView.getAdapter().getItem(position);
            addFoodItem(blueToothInfo);
        }
    }

    private void addFoodItem(final BlueToothInfo blueToothInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.message_title_enter_item_name);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_food_item, null);
        final TextInputEditText editTextFoodItem = (TextInputEditText) view.findViewById(R.id.edit_text_food_item);

        builder.setView(view);

        builder.setPositiveButton(R.string.message_add, null);
        builder.setNegativeButton(R.string.message_cancel, null);

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        buttonPositive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(editTextFoodItem.getText().toString())) {
                    String foodItemName = editTextFoodItem.getText().toString();
                    AppLogs.e(TAG, foodItemName);
                    dialog.dismiss();

                    new ConnectBlueToothTask(activity, blueToothInfo, foodItemName).execute();
                }
                else{
                    editTextFoodItem.setError(getString(R.string.message_enter_item_name));
                }
            }
        });
    }

    private void retryBluetoothConnection(final BlueToothInfo blueToothInfo, final String foodItemName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Connection failed!");

        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new ConnectBlueToothTask(activity, blueToothInfo, foodItemName).execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
    }

    private static class ConnectBlueToothTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<DeviceListActivity> activityReference;
        private BlueToothInfo blueToothInfo;
        private String foodItemName;
        private String currentWeight;
        private boolean isConnected;

        private ConnectBlueToothTask(DeviceListActivity activity, BlueToothInfo blueToothInfo, String foodItemName){
            activityReference = new WeakReference(activity);
            this.blueToothInfo = blueToothInfo;
            this.foodItemName = foodItemName;
        }

        @Override
        protected void onPreExecute() {
            AppUtils.showProgressDialog(activityReference.get(), "Please wait...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BluetoothDevice blueToothDevice = activityReference.get().mBluetoothAdapter.getRemoteDevice(blueToothInfo.getAddress());
            if(!BluetoothConnector.getInstance().isConnected()){
                isConnected = BluetoothConnector.getInstance().bluetoothConnect(blueToothDevice);
            }
            else{
                publishProgress();
                isConnected = true;
            }

            currentWeight = getCurrentWeight(isConnected);

            return isConnected;
        }

        private String getCurrentWeight(boolean isConnected) {
            if(isConnected){
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                currentWeight = BluetoothConnector.getInstance().bulkRead();
            }
            return "0.0";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            AppLogs.i(TAG, "Bluetooth is already connected");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            AppUtils.hideProgressDialog();
            if(result){
                AppLogs.i(TAG, "Bluetooth connected successfully to "+blueToothInfo.getAddress());
                AppUtils.showToast(activityReference.get(), "Bluetooth connected successfully to "+blueToothInfo.getAddress()+" with current weight: "+currentWeight);
                activityReference.get().setResult(Activity.RESULT_OK, new Intent()
                        .putExtra(activityReference.get().getString(R.string.INTENT_EXTRA_CURRENT_WEIGHT), currentWeight)
                        .putExtra(activityReference.get().getString(R.string.INTENT_EXTRA_ITEM_NAME), foodItemName)
                        .putExtra(activityReference.get().getString(R.string.INTENT_EXTRA_MACADDRESS), blueToothInfo.getAddress()));
                activityReference.get().finish();
            }
            else{
                AppUtils.showToast(activityReference.get(), "Bluetooth connection failed. Please try again.");
                activityReference.get().retryBluetoothConnection(blueToothInfo, foodItemName);
            }
        }
    }
}
