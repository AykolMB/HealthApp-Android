package com.example.bitirme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;

public class BarChartPage extends AppCompatActivity {

    static String username, graph;
    static double[] array;
    static String[] arrayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        showData();

        final String[] domainLabels = arrayDate;
        Number[] series1Numbers = new Number[domainLabels.length];

        for (int i = 0 ; i<series1Numbers.length ; i++){
            series1Numbers[i] = array[i];
        }

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0 ; i < series1Numbers.length ; i++){
            entries.add(new BarEntry(Float.parseFloat(series1Numbers[i] + ""), i));
        }

        BarDataSet bardataset = new BarDataSet(entries, graph.toUpperCase());

        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0 ; i<domainLabels.length ; i++){
            labels.add(domainLabels[i].substring(0,5));
        }

        BarData data = new BarData(labels, bardataset);
        data.setValueTextSize(18);
        data.setValueTextColor(Color.RED);
        data.setHighlightEnabled(true);

        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Bar graph of " + graph.toUpperCase());  // set the description
        barChart.setDescriptionTextSize(20);
        barChart.setDescriptionColor(Color.RED);


        bardataset.setColors(Collections.singletonList(Color.rgb(102,255,178)));

        barChart.animateY(2000);

    }

    private void showData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        graph = intent.getStringExtra("graph");
        array = intent.getDoubleArrayExtra("array");
        arrayDate = intent.getStringArrayExtra("dateData");
    }
}