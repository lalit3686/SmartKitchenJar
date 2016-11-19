package com.app.kitchen.jar.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.kitchen.jar.R;
import com.app.kitchen.jar.adapters.FoodItemsAdapter;
import com.app.kitchen.jar.beans.JarWeightInfo;
import com.app.kitchen.jar.databases.TableJarWeightInfo;
import com.app.kitchen.jar.listeners.CustomOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lalit T. Poptani on 10/25/2016.
 */

public class FoodItemsListActivity extends BaseActivity implements CustomOnItemClickListener, View.OnClickListener {

    private static final int DEVICE_REQUEST = 100;
    private FoodItemsListActivity activity;
    private TextView textViewNoFoodItems;
    private RecyclerView recyclerViewFoodItems;
    private FloatingActionButton fabAddItem;
    private FoodItemsAdapter adapter;
    private List<JarWeightInfo> listJarWeightInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_item_list);

        activity = this;

        initToolBar();
        initComponents();
        addListeners();
    }

    private void initToolBar(){
        Toolbar toolbarFoodItems = (Toolbar) findViewById(R.id.toolbar_food_items);
        setSupportActionBar(toolbarFoodItems);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    void initComponents() {
        textViewNoFoodItems = (TextView) findViewById(R.id.text_view_no_food_items);

        recyclerViewFoodItems = (RecyclerView) findViewById(R.id.list_food_items);
        recyclerViewFoodItems.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewFoodItems.setLayoutManager(linearLayoutManager);

        fabAddItem = (FloatingActionButton) findViewById(R.id.fab_add_item);
    }

    @Override
    void addListeners() {
        adapter = new FoodItemsAdapter(activity, textViewNoFoodItems, getJarWeightInfo());
        recyclerViewFoodItems.setAdapter(adapter);

        fabAddItem.setOnClickListener(this);
    }

    private void getExtra(Intent data){
        double currentWeight = Double.parseDouble(data.getExtras().getString(getString(R.string.INTENT_EXTRA_CURRENT_WEIGHT)));
        String itemName = data.getExtras().getString(getString(R.string.INTENT_EXTRA_ITEM_NAME));
        String macAddress = data.getExtras().getString(getString(R.string.INTENT_EXTRA_MACADDRESS));
        long timestamp = System.currentTimeMillis();

        insertJarWeightInfo(macAddress, itemName, currentWeight, timestamp);

        listJarWeightInfo.add(TableJarWeightInfo.getJarWeightInfoByBluetoothAddress(macAddress));
        adapter.notifyDataSetChanged();
    }

    private void insertJarWeightInfo(String macAddress, String itemName, double itemWeight, long timestamp){
        TableJarWeightInfo.insertIntoTable(macAddress, itemName, itemWeight, timestamp);
    }

    private List<JarWeightInfo> getJarWeightInfo() {
        listJarWeightInfo = TableJarWeightInfo.getAllJarWeightInfo();
        return listJarWeightInfo;
    }

    @Override
    public void onItemClick(View v, int position, Object item) {

        JarWeightInfo jarWeightInfo = (JarWeightInfo) item;
        Intent intent = new Intent(v.getContext(), TabLayoutActivity.class);
        intent.putExtra(getString(R.string.INTENT_EXTRA_JAR_WEIGHT_INFO), jarWeightInfo);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_item:
                startActivityForResult(new Intent(activity, DeviceListActivity.class), DEVICE_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DEVICE_REQUEST) {
            if (resultCode == RESULT_OK) {
                getExtra(data);
            }
        }
    }
}
