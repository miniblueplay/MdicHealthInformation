package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class material extends AppCompatActivity {

    protected ActivityMaterialBinding mBinding;

    //宣告Handler
    HandlerThread myHandlerThread;
    private Handler mHandler;

    //PHP
    private MysqlCon Mysql;

    //折線圖
    LineChartData lineChartData;
    LineChart lineChart;
    ArrayList<String> xData = new ArrayList<>();
    ArrayList<Entry> yData = new ArrayList<>();

    //RecyclerList
    private RecyclerView recyclerView;
    private RecycleAdapterDome adapterDome;

    //用戶
    private String UserCardID;
    private Map<String,String> UserData;
    int MeasurementTimes = 0, MeasurementTimesDeviant = 0;
    private final List<String> list_date = new ArrayList<>();
    private final List<String> list_sys = new ArrayList<>();
    private final List<String> list_dia = new ArrayList<>();
    private final List<String> list_hr = new ArrayList<>();
    private final List<String> list_res = new ArrayList<>();
    private final List<Drawable> list_IV = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNav();
        setContentView(R.layout.activity_material);

        //使用ViewBinding後的方法
        mBinding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        UserCardID = intent.getStringExtra("USER_CARDID");

        //建立HandlerThread
        myHandlerThread = new HandlerThread( "PHP-SQL") ;
        myHandlerThread.start();

        Mysql = new MysqlCon();

        UpdateChart();

    }

    public void RecycleView(){
        recyclerView = findViewById(R.id.rv_data);
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

    public void MPAndroidChart(){
        lineChart = mBinding.lineChart;
        lineChartData = new LineChartData(lineChart,this);
        for (int i = 0; i < MeasurementTimes; i++) {
            xData.add("11" + "/" + i);
            //yData.add(new Entry(i, list_sys));
        }

        lineChartData.initX(xData);
        lineChartData.initY(90F,140F);
        lineChartData.initDataSet(yData);
    }

    public void UpdateChart(){
        mHandler = new Handler( myHandlerThread.getLooper() ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        //抓取姓名
                        mBinding.name.setText(Mysql.getData("Login", "CardID", UserCardID, "cname"));
                        //抓取用戶資料
                        UserData = Mysql.getDataArray("Field_GetUserData", UserCardID);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = findViewById(R.id.rv_data);
                                //獲取map集合中的所有鍵的Set集合, keySet()
                                Set<String> keySet = UserData.keySet();
                                //有了set集合就可以獲取迭代器
                                for (String key : keySet) {
                                    MeasurementTimes++;
                                    //有了鍵就可以通過map集合的get方法獲取其對應的値
                                    String value = UserData.get(key);
                                    list_date.add(key);
                                    String[] detailed = value.split("\\s+");
                                    list_sys.add(detailed[0]);
                                    list_dia.add(detailed[1]);
                                    list_hr.add(detailed[2]);
                                    //list_res.add(detailed[3]);
                                    if(detailed[3].equals("0")) {
                                        MeasurementTimesDeviant++;
                                        list_IV.add(getResources().getDrawable(R.drawable.no));
                                    }
                                    else
                                        list_IV.add(getResources().getDrawable(R.drawable.ok));
                                }
                                mBinding.materialMeasurementNum.setText(String.valueOf(MeasurementTimes));
                                mBinding.materialAbnormalNum.setText(String.valueOf(MeasurementTimesDeviant));
                                RecycleView();
                                MPAndroidChart();
                            }
                        });
                        //initialAdapter(Mysql.getDataArray("Field_id", ""));
                        break;
                    case 2:
                        //NameList = Mysql.getDataArray("GetData", field_ID);
                        //mHandler.post(runnable);
                        break;
                    default:
                        break;
                }
            }
        };

        mHandler.sendEmptyMessage( 1 ) ;
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