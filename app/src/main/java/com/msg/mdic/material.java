package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.msg.mdic.adapter.RecycleAdapterDome;
import com.msg.mdic.databinding.ActivityMaterialBinding;
import com.msg.mdic.tool.LineChartData;
import com.msg.mdic.tool.MysqlCon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class material extends AppCompatActivity {

    protected ActivityMaterialBinding mBinding;

    LineChartData lineChartData;
    LineChart lineChart;
    ArrayList<String> xData = new ArrayList<>();
    ArrayList<Entry> yData = new ArrayList<>();

    private RecyclerView recyclerView;
    private List<String> list_date;
    private List<String> list_sys;
    private List<String> list_dia;
    private List<String> list_hr;
    private List<Drawable> list_IV;
    private RecycleAdapterDome adapterDome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNav();
        setContentView(R.layout.activity_material);
        //使用ViewBinding後的方法
        mBinding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        UpdateChart();

        list_sys = new ArrayList<>();

        lineChart = mBinding.lineChart;
        lineChartData = new LineChartData(lineChart,this);

        for (int i = 1; i < 30; i++) {
            int sys = new Random().nextInt(15) + 130;
            list_sys.add(String.valueOf(sys));
            xData.add("11" + "/" + i);
            yData.add(new Entry(i-1, sys));
        }

        lineChartData.initX(xData);
        lineChartData.initY(90F,140F);
        lineChartData.initDataSet(yData);

        RecycleView(list_sys);
    }

    public void RecycleView(List<String> list_sys){
        recyclerView = findViewById(R.id.rv_data);
        list_date = new ArrayList<>();
        //list_sys = new ArrayList<>();
        list_dia = new ArrayList<>();
        list_hr = new ArrayList<>();
        list_IV = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            if(i < 10)
                list_date.add("2022/11/0"+i);
            else
                list_date.add("2022/11/"+i);
            //int sys = new Random().nextInt(50) + 100;
            int dia = new Random().nextInt(35) + 60;
            int hr = new Random().nextInt(45) + 60;
            //list_sys.add(String.valueOf(sys));
            list_dia.add(String.valueOf(dia));
            list_hr.add(String.valueOf(hr));
            //Log.d("sys", list_sys.get(i-1));
            if(Integer.parseInt(list_sys.get(i-1)) > 140 || dia > 90 || hr > 100)
                list_IV.add(getResources().getDrawable(R.drawable.no));
            else
                list_IV.add(getResources().getDrawable(R.drawable.ok));
        }
        adapterDome = new RecycleAdapterDome(this,list_date, list_sys, list_dia, list_hr, list_IV);
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(stagger);
        //添加自定义分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapterDome);
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

    public void UpdateChart(){
        new Thread(() -> {
            MysqlCon Mysql = new MysqlCon();
            Mysql.getData("A0000002");
            //Mysql.run();
        }).start();
    }
}