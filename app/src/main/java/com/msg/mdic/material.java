package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.msg.mdic.tool.LineChartData;

import java.util.ArrayList;

public class material extends AppCompatActivity {

    LineChartData lineChartData;
    LineChart lineChart;
    ArrayList<String> xData = new ArrayList<>();
    ArrayList<Entry> yData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNav();
        setContentView(R.layout.activity_material);

        lineChart = findViewById(R.id.lineChart);
        lineChartData = new LineChartData(lineChart,this);

        xData.add("10" + "/" + 1);
        yData.add(new Entry(1-1, 120));

        xData.add("10" + "/" + 2);
        yData.add(new Entry(2-1, 125));

        xData.add("10" + "/" + 3);
        yData.add(new Entry(3-1, 114));

        xData.add("10" + "/" + 4);
        yData.add(new Entry(4-1, 121));

        xData.add("10" + "/" + 5);
        yData.add(new Entry(5-1, 119));

        xData.add("10" + "/" + 6);
        yData.add(new Entry(6-1, 135));

        xData.add("10" + "/" + 7);
        yData.add(new Entry(7-1, 126));

        xData.add("10" + "/" + 8);
        yData.add(new Entry(8-1, 120));


        lineChartData.initX(xData);
        lineChartData.initY(90F,140F);
        lineChartData.initDataSet(yData);
    }

    /**隱藏狀態列和導航欄*/
    public void hideNav() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //設定隱藏標題
        getSupportActionBar().hide();
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}