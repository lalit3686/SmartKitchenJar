package com.app.kitchen.jar.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.kitchen.jar.R;
import com.app.kitchen.jar.beans.JarWeightInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DayWiseChartFragment extends BaseFragment implements View.OnClickListener {

    private BarChart barChart;
    private JarWeightInfo weightInfo;
    private TextView textViewDailyConsumption, textViewMonthly;
    private Button buttonOrderOnline;

    public DayWiseChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_wise_chart, container, false);

        initComponents(view);
        addListeners();
        getExtras();
        prepareBarChart();

        return view;
    }

    private void getExtras() {
        Bundle bundle = getActivity().getIntent().getExtras();
        weightInfo = bundle.getParcelable(getString(R.string.INTENT_EXTRA_JAR_WEIGHT_INFO));

        textViewDailyConsumption.setText(String.format(getString(R.string.text_daily_consumption), weightInfo.getItemName()));
        textViewMonthly.setText(String.format(getString(R.string.text_monthly), weightInfo.getItemWeight(), weightInfo.getItemName()));
    }

    @Override
    void initComponents(View view) {
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        textViewDailyConsumption = (TextView) view.findViewById(R.id.text_view_daily_consumption);
        textViewMonthly = (TextView) view.findViewById(R.id.text_view_monthly);
        buttonOrderOnline = (Button) view.findViewById(R.id.button_order_online);
    }

    @Override
    void addListeners() {
        buttonOrderOnline.setOnClickListener(this);
    }

    private BarDataSet getDataSet() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));
        entries.add(new BarEntry(7f, 40f));

        BarDataSet dataSet = new BarDataSet(entries, String.format(getString(R.string.text_daily_consumption_in_grams), weightInfo.getItemName()));
        dataSet.setColor(Color.BLUE);

        return dataSet;
    }

    private ArrayList<String> getXAxisValues(){
        ArrayList<String> values = new ArrayList<>();
        values.add("Sun");
        values.add("Mon");
        values.add("Tue");
        values.add("Wed");
        values.add("Thu");
        values.add("Fri");
        values.add("Sat");

        return values;
    }

    private XAxis prepareXAxis() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        //xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        return xAxis;
    }

    private void prepareLeftYAxis() {
        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setTextColor(Color.BLACK);
        //leftYAxis.setDrawGridLines(false);
    }

    private void prepareRightYAxis() {
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setTextColor(Color.BLACK);
        //rightYAxis.setDrawGridLines(false);
    }

    private void prepareBarChart() {

        prepareXAxis();
        prepareLeftYAxis();
        prepareRightYAxis();

        BarData data = new BarData(getDataSet());
        data.setBarWidth(0.8f); // set custom bar width
        //data.setValueFormatter(new MyValueFormatter());

        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.animateX(1000);
        barChart.setDescription(null);
        barChart.setPinchZoom(true);
        barChart.invalidate(); // refresh

        //barChart.setTouchEnabled(false);
        //barChart.getAxisRight().setEnabled(false);
        //barChart.getAxisLeft().setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_order_online:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.in/s/field-keywords="+weightInfo.getItemName())));
                break;
        }
    }

    /*private class MyValueFormatter implements IValueFormatter {


        public MyValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return value + "value";
        }
    }*/
}