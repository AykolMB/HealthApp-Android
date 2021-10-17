package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph extends AppCompatActivity {


    XYPlot plot;
    static String username, graph;
    static double[] array;
    static String[] arrayDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        plot = findViewById(R.id.plot);

        showData();


        final String[] domainLabels = arrayDate;
        Number[] series1Numbers = new Number[domainLabels.length];

        for (int i = 0 ; i<series1Numbers.length ; i++){
            series1Numbers[i] = array[i];
        }


        XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,""+graph.toUpperCase());


        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED,Color.GREEN,null,null);

        series1Format.setInterpolationParams(new CatmullRomInterpolator.Params(series1Numbers.length,
                CatmullRomInterpolator.Type.Centripetal));

        plot.addSeries(series1,series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round( ((Number)obj).floatValue() );
                return toAppendTo.append(domainLabels[i]);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }


    private void showData() {

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        graph = intent.getStringExtra("graph");
        array = intent.getDoubleArrayExtra("array");
        arrayDate = intent.getStringArrayExtra("date");

    }
}