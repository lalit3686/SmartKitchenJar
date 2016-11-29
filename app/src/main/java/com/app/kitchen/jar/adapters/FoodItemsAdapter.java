package com.app.kitchen.jar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.kitchen.jar.beans.JarWeightInfo;
import com.app.kitchen.jar.commons.AppLogs;
import com.app.kitchen.jar.databases.TableJarWeightInfo;
import com.app.kitchen.jar.listeners.CustomOnItemClickListener;
import com.app.kitchen.jar.R;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.util.List;
import java.util.Random;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.FoodItemHolder> {

    private static final String TAG = FoodItemHolder.class.getSimpleName();
    private TextView textViewNoFoodItems;
    private List<JarWeightInfo> listJarWeightInfo = null;
    private CustomOnItemClickListener onItemClickListener;
    private Random random = new Random();

    public FoodItemsAdapter(Context context, TextView textViewNoFoodItems, List<JarWeightInfo> listJarWeightInfo) {
        this.onItemClickListener = (CustomOnItemClickListener) context;
        this.textViewNoFoodItems = textViewNoFoodItems;
        this.listJarWeightInfo = listJarWeightInfo;
    }

    @Override
    public FoodItemHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_item, parent, false);

        final FoodItemHolder holder = new FoodItemHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, holder.getAdapterPosition(), listJarWeightInfo.get(holder.getAdapterPosition()));
            }
        });

        AppLogs.e(TAG, "onCreateViewHolder");

        return holder;
    }

    @Override
    public void onBindViewHolder(FoodItemHolder holder, int position) {
        AppLogs.e(TAG, "onBindViewHolder - " + position);
        holder.textViewFoodItem.setText(listJarWeightInfo.get(position).getItemName());

        //JarWeightInfo jarWeightInfo = TableJarWeightInfo.getJarWeightInfoByBluetoothAddress(listJarWeightInfo.get(position).getMacAddress());
        double weight = listJarWeightInfo.get(position).getItemWeight();
        //if (jarWeightInfo != null) {
        //    weight = jarWeightInfo.getItemWeight();
        //}
        holder.circularBarWeight.setProgress((int) weight);
        holder.circularBarWeight.setTextSize(30);
        holder.circularBarWeight.setTitle(weight+"");
        holder.circularBarWeight.setSubTitle("gm");


    }

    @Override
    public int getItemCount() {
        textViewNoFoodItems.setVisibility(listJarWeightInfo.size() > 0 ? View.GONE : View.VISIBLE);
        return listJarWeightInfo.size();
    }

    public static class FoodItemHolder extends RecyclerView.ViewHolder {

        private TextView textViewFoodItem;
        private CircularProgressBar circularBarWeight;

        public FoodItemHolder(View view) {
            super(view);

            circularBarWeight = (CircularProgressBar) view.findViewById(R.id.circular_bar_weight);
            textViewFoodItem = (TextView) view.findViewById(R.id.text_view_food_item);
        }
    }
}