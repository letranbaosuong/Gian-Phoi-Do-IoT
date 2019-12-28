package com.suong99.gianphoidoandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class BieuDoActivity extends AppCompatActivity {

    Toolbar toolbarBieuDo;
    private Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> series;
    private double lastXPoint = 2;
    private Random rnd = new Random();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bieu_do);
        AnhXa();

        // Nhiet Do
        GraphView graphLine = findViewById(R.id.graphNhietDo);
        series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 2)
        });
        graphLine.addSeries(series);

        graphLine.getViewport().setMinX(0);
//        graphLine.getViewport().setMaxX(10);
        graphLine.getViewport().setXAxisBoundsManual(true);

        graphLine.getViewport().setMinY(0);
//        graphLine.getViewport().setMaxY(50);
        graphLine.getViewport().setYAxisBoundsManual(true);

        graphLine.getViewport().setScrollable(true);
        graphLine.getViewport().setScrollableY(true);
        graphLine.getViewport().setScalable(true);
        graphLine.getViewport().setScalableY(true);

        addRandomDataPoint();

        // Do am
        CustomGauge gaugeDoAm = findViewById(R.id.gaugeDoAm);
        gaugeDoAm.setEndValue(800);
        gaugeDoAm.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        gaugeDoAm.setValue(500);
        TextView txtPhanTramDoAm = findViewById(R.id.txtPhanTramDoAm);
        txtPhanTramDoAm.setText(gaugeDoAm.getValue() + "%");
    }

    private void addRandomDataPoint() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastXPoint++;
                series.appendData(new DataPoint(lastXPoint, rnd.nextInt(80)), false, 100);
                addRandomDataPoint();
            }
        }, 1000);
    }

    private void AnhXa() {
        toolbarBieuDo = findViewById(R.id.toolbarBieuDo);
        setSupportActionBar(toolbarBieuDo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarBieuDo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
