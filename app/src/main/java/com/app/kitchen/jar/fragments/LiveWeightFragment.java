package com.app.kitchen.jar.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.kitchen.jar.R;
import com.app.kitchen.jar.application.MyApplication;
import com.app.kitchen.jar.beans.JarWeightInfo;
import com.app.kitchen.jar.commons.AppLogs;
import com.app.kitchen.jar.commons.BluetoothConnector;
import com.app.kitchen.jar.databases.TableJarInfo;
import com.app.kitchen.jar.databases.TableJarWeightInfo;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LiveWeightFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = LiveWeightFragment.class.getSimpleName();
    private Button buttonTar, buttonRemoveJar;
    private CircularProgressBar circularBarLiveWeight;
    private TextView textViewLiveWeight;
    private JarWeightInfo jarWeightInfo;
    private double liveWeight;

    public LiveWeightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_weight, container, false);

        initComponents(view);
        addListeners();
        getExtras();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getEventBusInstance().register(this);
        new LiveWeightListenTask().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        BluetoothConnector.getInstance().stopListening();
        MyApplication.getEventBusInstance().unregister(this);
    }

    private class LiveWeightListenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            BluetoothConnector.getInstance().listen();
            return null;
        }
    }

    private void getExtras() {
        Bundle bundle = getActivity().getIntent().getExtras();
        jarWeightInfo = bundle.getParcelable(getString(R.string.INTENT_EXTRA_JAR_WEIGHT_INFO));

        textViewLiveWeight.setText(String.format(getString(R.string.text_live_weight), jarWeightInfo.getItemName()));
    }

    @Override
    void initComponents(View view) {
        buttonTar = (Button) view.findViewById(R.id.button_tar);
        buttonRemoveJar = (Button) view.findViewById(R.id.button_remmove_jar);

        circularBarLiveWeight = (CircularProgressBar) view.findViewById(R.id.circular_bar_live_weight);
        circularBarLiveWeight.setProgress(0);
        circularBarLiveWeight.setTitle(0 + " gms");

        textViewLiveWeight = (TextView) view.findViewById(R.id.text_view_live_weight);
    }

    @Override
    void addListeners() {
        buttonTar.setOnClickListener(this);
        buttonRemoveJar.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLiveWeightEvent(final String[] weightInfo) {

        double resetWeight = TableJarInfo.getResetWeight(jarWeightInfo.getMacAddress());

        final double lastReceivedWeight = Double.valueOf(weightInfo[0]) - resetWeight;
        liveWeight = Double.valueOf(weightInfo[1]) - resetWeight;

        circularBarLiveWeight.setTitle(liveWeight + "");
        circularBarLiveWeight.setSubTitle("gm");
        circularBarLiveWeight.setProgress((int) liveWeight);
        new Thread(new Runnable() {
            @Override
            public void run() {
                double consumed = 0;
                if (lastReceivedWeight > liveWeight) { // Item consumed
                    consumed = lastReceivedWeight - liveWeight;
                }
                TableJarWeightInfo.insertIntoTable(jarWeightInfo.getMacAddress(), jarWeightInfo.getItemName(), liveWeight, System.currentTimeMillis(), consumed);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_tar:
                resetJar();
                break;
            case R.id.button_remmove_jar:
                break;
        }
    }

    public void resetJar() {
        AppLogs.e(TAG,"Reseting Jar");
        TableJarInfo.updateResetWeight(jarWeightInfo.getMacAddress(), liveWeight);
        circularBarLiveWeight.setTitle("0.0");
        circularBarLiveWeight.setSubTitle("gm");
        circularBarLiveWeight.setProgress(0);
    }
}