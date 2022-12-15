package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import java.text.ParseException;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.msg.mdic.adapter.RecycleAdapterDome;
import com.msg.mdic.databinding.ActivityMaterialBinding;
import com.msg.mdic.tool.LineChartData;
import com.msg.mdic.tool.MysqlCon;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class material extends AppCompatActivity {

    protected ActivityMaterialBinding mBinding;

    private static final String TAG = "material";

    //宣告Handler
    HandlerThread myHandlerThread;
    private Handler mHandler;

    //PHP
    private MysqlCon Mysql;

    //折線圖
    LineChartData lineChartData;
    LineChart lineChart;

    //RecyclerList
    private RecyclerView recyclerView;
    private RecycleAdapterDome adapterDome;

    //用戶
    private String UserCardID;
    private String Name;
    private Map<String,String> UserData;
    int MeasurementTimes = 0, MeasurementTimesDeviant = 0;
    private final List<String> list_date = new ArrayList<>();
    private final List<String> list_sys = new ArrayList<>();
    private final List<String> list_dia = new ArrayList<>();
    private final List<String> list_hr = new ArrayList<>();
    //private final List<String> list_res = new ArrayList<>();
    private final List<String> list_Hypertension = new ArrayList<>();
    private final List<String> list_Medicine = new ArrayList<>();
    private final List<Drawable> list_IV = new ArrayList<>();

    //依時間顯示用列表數據
    private final List<String> list_date_new = new ArrayList<>();
    private final List<String> list_sys_new = new ArrayList<>();
    private final List<String> list_dia_new = new ArrayList<>();
    private final List<String> list_hr_new = new ArrayList<>();
    //private final List<String> list_res_new = new ArrayList<>();
    private final List<String> list_Hypertension_new = new ArrayList<>();
    private final List<String> list_Medicine_new = new ArrayList<>();
    private final List<Drawable> list_IV_new = new ArrayList<>();


    //設定
    private boolean showHR = true;
    private final List<String> setTimeShow = new  ArrayList<>();

    //switch buttons

    //test
    LinearLayout linearLayout;
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

        //心律折線圖顯示(隱藏)
        mBinding.buttonHrShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( showHR ){
                    showHR = false;
                    mBinding.textHr.setVisibility(View.INVISIBLE);
                }
                else{
                    showHR = true;
                    mBinding.textHr.setVisibility(View.VISIBLE);
                }
                mHandler.sendEmptyMessage( 2 ) ;
            }
        });

        //顯示月,日,時數據
        setTimeShow.add(0, "1");
        setTimeShow.add(1, "時");
        setTimeShow.add(2, "日");
        setTimeShow.add(3, "月");

        /*
        mBinding.buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(setTimeShow.get(0)) == setTimeShow.size()-1 )setTimeShow.set(0, "0");
                setTimeShow.set(0, String.valueOf(Integer.parseInt(setTimeShow.get(0))+1));
                mBinding.buttonDate.setText(setTimeShow.get(Integer.parseInt(setTimeShow.get(0))));
                switch (setTimeShow.get(0)){
                    case "1": //時
                        for (int i = 0; i < MeasurementTimes; i++) {
                            list_date_new.add(list_date.get(i));
                            list_sys_new.add(list_sys.get(i));
                            list_dia_new.add(list_dia.get(i));
                            list_hr_new.add(list_hr.get(i));
                            list_Hypertension_new.add(list_Hypertension.get(i));
                            list_Medicine_new.add(list_Medicine.get(i));
                            list_IV_new.add(list_IV.get(i));
                        }
                        break;
                    case "2": //日
                        for (int i = 0; i < MeasurementTimes; i++) {

                        }
                        break;
                    case "3": //月
                        for (int i = 0; i < MeasurementTimes; i++) {

                        }
                        break;
                    default:break;
                }
                mHandler.sendEmptyMessage( 3 ) ;
            }
        });
        */

        //instance all of the switch buttons
        MaterialButtonToggleGroup materialButtonToggleGroup = findViewById(R.id.toggleBtn);
        materialButtonToggleGroup.addOnButtonCheckedListener((group, check, isCheck) -> {

            if(isCheck) {
                if(check == R.id.switchBtnHr) {
                    setTimeShow.set(0,"1");
                } else if(check == R.id.switchBtnDay) {
                    setTimeShow.set(0,"2");
                } else if(check == R.id.switchBtnMonth) {
                    setTimeShow.set(0,"3");
                }
            }
            mBinding.buttonDate.setText(setTimeShow.get(Integer.parseInt(setTimeShow.get(0))));
            switch (setTimeShow.get(0)){
                case "1": //時
                    for (int i = 0; i < MeasurementTimes; i++) {
                        list_date_new.add(list_date.get(i));
                        list_sys_new.add(list_sys.get(i));
                        list_dia_new.add(list_dia.get(i));
                        list_hr_new.add(list_hr.get(i));
                        list_Hypertension_new.add(list_Hypertension.get(i));
                        list_Medicine_new.add(list_Medicine.get(i));
                        list_IV_new.add(list_IV.get(i));
                    }
                    break;
                case "2": //日
                    for (int i = 0; i < MeasurementTimes; i++) {

                    }
                    break;
                case "3": //月
                    for (int i = 0; i < MeasurementTimes; i++) {

                    }
                    break;
                default:break;
            }
            mHandler.sendEmptyMessage( 3 ) ;
        });
    }

    /**數據列表**/
    public void RecycleView(List<String> date, List<String> sys, List<String> dia, List<String> hr, List<String> hypertension, List<String> medicine, List<Drawable> iv) throws ParseException {
        recyclerView = mBinding.rvData;
        ArrayList<String> oldDate = new ArrayList<>();
        if(MeasurementTimes != 0) {
            switch (setTimeShow.get(0)) {
                case "1":
                    for (int i = MeasurementTimes - 1; i >= 0; i--) {
                        oldDate.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM/dd HH:mm:ss"));
                    }
                    break;
                case "2":
                    for (int i = MeasurementTimes - 1; i >= 0; i--)
                        oldDate.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM/dd"));
                    break;
                case "3":
                    for (int i = MeasurementTimes - 1; i >= 0; i--)
                        oldDate.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM月"));
                    break;
                default:
                    for (int i = MeasurementTimes - 1; i >= 0; i--)
                        oldDate.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM/dd HH:mm:ss"));
                    break;
            }
        }
        ArrayList<String> newDate = new ArrayList<>();
        ArrayList<String> newSys = new ArrayList<>();
        ArrayList<String> newDia = new ArrayList<>();
        ArrayList<String> newHr = new ArrayList<>();
        ArrayList<String> newHypertension = new ArrayList<>();
        ArrayList<String> newMedicine = new ArrayList<>();
        ArrayList<Drawable> newIv = new ArrayList<>();
        for (int i = MeasurementTimes-1; i >= 0; i--){
            newDate.add(oldDate.get(i));
            newSys.add(sys.get(i));
            newDia.add(dia.get(i));
            newHr.add(hr.get(i));
            newHypertension.add(hypertension.get(i));
            newMedicine.add(medicine.get(i));
            newIv.add(iv.get(i));
        }
        Log.i(TAG + " RecycleView", "List num = " + MeasurementTimes);
        adapterDome = new RecycleAdapterDome(this, newDate, newSys, newDia, newHr, newHypertension, newMedicine, newIv);
        //adapterDome.notifyItemInserted(10);
        //StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(stagger);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapterDome);
    }

    /**折線圖**/
    public void MPAndroidChart(List<String> date, List<String> sys, List<String> dia, List<String> hr, List<String> hypertension, List<String> medicine)throws ParseException{
        int hi, mx;
        lineChart = mBinding.lineChart;
        lineChart.setDrawGridBackground(false);//後臺繪製
        lineChart.setScaleYEnabled(false);//禁用Y軸縮放
        lineChart.setScaleXEnabled(false);//禁用X軸縮放
        //這個三個屬性是設置LineChar間距的
        lineChart.setExtraBottomOffset(20f);
        lineChart.setExtraRightOffset(5f);
        lineChart.setExtraLeftOffset(5f);//間距
        lineChart.setDoubleTapToZoomEnabled(false);//禁用雙擊放大

        lineChartData = new LineChartData(lineChart,this);

        //有無高血壓
        if(hypertension.get(MeasurementTimes-1).equals("有")){
            //有無藥物控制
            if(medicine.get(MeasurementTimes-1).equals("有")){
                hi = 160;
                mx = 60;
                lineChartData.LimitLine(Color.YELLOW, 140, "mmHg");
                lineChartData.LimitLine(Color.GREEN, 90, "mmHg");
                if ( showHR ) lineChartData.LimitLine(Color.argb(255,255,136,0) , 100, "Bpm");
            }else{
                hi = 200;
                mx = 60;
                lineChartData.LimitLine(Color.YELLOW, 180, "mmHg");
                lineChartData.LimitLine(Color.GREEN, 110, "mmHg");
                if ( showHR ) lineChartData.LimitLine(Color.argb(255,255,136,0), 100, "Bpm");
            }
        }else{
            hi = 160;
            mx = 60;
            lineChartData.LimitLine(Color.YELLOW, 140, "mmHg");
            lineChartData.LimitLine(Color.GREEN, 90, "mmHg");
            if ( showHR ) lineChartData.LimitLine(Color.argb(255,255,136,0), 100, "Bpm");
        }

        ArrayList<String> xData = new ArrayList<>();
        ArrayList<Entry> yData_sys = new ArrayList<>();
        ArrayList<Entry> yData_dia = new ArrayList<>();
        ArrayList<Entry> yData_hr = new ArrayList<>();
        ArrayList<Entry> yDataEnd_sys = new ArrayList<>();
        ArrayList<Entry> yDataEnd_dia = new ArrayList<>();
        ArrayList<Entry> yDataEnd_hr = new ArrayList<>();

        xData.add(" ");
        xData.add(" ");

        if(MeasurementTimes != 0){
            switch (setTimeShow.get(0)){
                case "1":
                    for (int i = MeasurementTimes-1; i >= 0; i--)
                        xData.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM/dd HH:mm"));
                    break;
                case "2":
                    for (int i = MeasurementTimes-1; i >= 0; i--)
                        xData.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM/dd"));
                    break;
                case "3":
                    for (int i = MeasurementTimes-1; i >= 0; i--)
                        xData.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "MM月"));
                    break;
                default:
                    for (int i = MeasurementTimes-1; i >= 0; i--)
                        xData.add(DateToNewDate(date.get(i), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss"));
                    break;
            }
            for (int i = MeasurementTimes-1; i >= 0; i--){
                yData_sys.add(new Entry(MeasurementTimes-i+1, Integer.parseInt(sys.get(i))));
                yData_dia.add(new Entry(MeasurementTimes-i+1, Integer.parseInt(dia.get(i))));
                yData_hr.add(new Entry(MeasurementTimes-i+1, Integer.parseInt(hr.get(i))));
                //高血壓上限值設定
                if(hypertension.get(MeasurementTimes-1).equals("有")){
                    if(medicine.get(MeasurementTimes-1).equals("有")){
                        if(Integer.parseInt(sys.get(i)) > hi) hi = Integer.parseInt(sys.get(i)) + 10;
                        if(Integer.parseInt(dia.get(i)) > hi) hi = Integer.parseInt(dia.get(i)) + 10;
                        if(Integer.parseInt(hr.get(i)) > hi) hi = Integer.parseInt(hr.get(i)) + 10;
                        if(Integer.parseInt(sys.get(i)) < mx) mx = Integer.parseInt(sys.get(i)) - 10;
                        if(Integer.parseInt(dia.get(i)) < mx) mx = Integer.parseInt(dia.get(i)) - 10;
                        if(Integer.parseInt(hr.get(i)) < mx) mx = Integer.parseInt(hr.get(i)) - 10;
                    }else{
                        if(Integer.parseInt(sys.get(i)) > hi) hi = Integer.parseInt(sys.get(i)) + 10;
                        if(Integer.parseInt(dia.get(i)) > hi) hi = Integer.parseInt(dia.get(i)) + 10;
                        if(Integer.parseInt(hr.get(i)) > hi) hi = Integer.parseInt(hr.get(i)) + 10;
                        if(Integer.parseInt(sys.get(i)) < mx) mx = Integer.parseInt(sys.get(i)) - 10;
                        if(Integer.parseInt(dia.get(i)) < mx) mx = Integer.parseInt(dia.get(i)) - 10;
                        if(Integer.parseInt(hr.get(i)) < mx) mx = Integer.parseInt(hr.get(i)) - 10;
                    }
                }else{
                    if(Integer.parseInt(sys.get(i)) > hi) hi = Integer.parseInt(sys.get(i)) + 10;
                    if(Integer.parseInt(dia.get(i)) > hi) hi = Integer.parseInt(dia.get(i)) + 10;
                    if(Integer.parseInt(hr.get(i)) > hi) hi = Integer.parseInt(hr.get(i)) + 10;
                    if(Integer.parseInt(sys.get(i)) < mx) mx = Integer.parseInt(sys.get(i)) - 10;
                    if(Integer.parseInt(dia.get(i)) < mx) mx = Integer.parseInt(dia.get(i)) - 10;
                    if(Integer.parseInt(hr.get(i)) < mx) mx = Integer.parseInt(hr.get(i)) - 10;
                }
            }

            yDataEnd_sys.add(new Entry(MeasurementTimes+1, Integer.parseInt(sys.get(0))));
            yDataEnd_dia.add(new Entry(MeasurementTimes+1, Integer.parseInt(dia.get(0))));
            yDataEnd_hr.add(new Entry(MeasurementTimes+1, Integer.parseInt(hr.get(0))));
        }

        lineChartData.initX(xData);
        lineChartData.initY(mx, hi);
        lineChartData.initDataSet(yData_sys, yData_dia, yData_hr, yDataEnd_sys, yDataEnd_dia, yDataEnd_hr, showHR);
        lineChart.moveViewToX(date.size()-1);
    }

    /**HandlerThread**/
    public void UpdateChart(){
        mHandler = new Handler( myHandlerThread.getLooper() ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        try {
                            //抓取姓名
                            Name = Mysql.getData("Login", "CardID", UserCardID, "cname");
                            //抓取用戶資料
                            UserData = Mysql.getDataArray("Field_GetUserData", UserCardID);
                        }catch (Exception e){
                            Log.d(TAG, "Mysql Delay...");
                        }
                        try {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //解出陣列資料
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
                                                list_Hypertension.add(detailed[4]);
                                                list_Medicine.add(detailed[5]);
                                                if(detailed[3].equals("0")) {
                                                    MeasurementTimesDeviant++;
                                                    list_IV.add(getResources().getDrawable(R.drawable.no));
                                                }
                                                else
                                                    list_IV.add(getResources().getDrawable(R.drawable.ok));
                                            }

                                            //氣泡排序(日期)
                                            invertOrderList();

                                            //預載入資料
                                            for (int i = 0; i < MeasurementTimes; i++) {
                                                list_date_new.add(list_date.get(i));
                                                list_sys_new.add(list_sys.get(i));
                                                list_dia_new.add(list_dia.get(i));
                                                list_hr_new.add(list_hr.get(i));
                                                list_Hypertension_new.add(list_Hypertension.get(i));
                                                list_Medicine_new.add(list_Medicine.get(i));
                                                list_IV_new.add(list_IV.get(i));
                                            }

                                            try {
                                                mBinding.name.setText(Name); //用戶姓名
                                                mBinding.materialMeasurementNum.setText(String.valueOf(MeasurementTimes)); //測量次數
                                                mBinding.materialAbnormalNum.setText(String.valueOf(MeasurementTimesDeviant)); //測量異常次數
                                                RecycleView(list_date, list_sys, list_dia, list_hr, list_Hypertension, list_Medicine, list_IV);
                                                MPAndroidChart(list_date, list_sys, list_dia, list_hr, list_Hypertension, list_Medicine);
                                                mBinding.rlShow.setVisibility(View.VISIBLE); //顯示選單
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                //延遲時間(ms)
                            }, 2000);
                        }catch (Exception e){
                            Log.d(TAG, "Layout Error...");
                        }
                        break;
                    case 2:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MPAndroidChart(list_date_new, list_sys_new, list_dia_new, list_hr_new, list_Hypertension_new, list_Medicine_new);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case 3:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    RecycleView(list_date_new, list_sys_new, list_dia_new, list_hr_new, list_Hypertension_new, list_Medicine_new, list_IV_new);
                                    MPAndroidChart(list_date_new, list_sys_new, list_dia_new, list_hr_new, list_Hypertension_new, list_Medicine_new);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        };

        mHandler.sendEmptyMessage( 1 ) ;
    }

    /**時間格式轉換**/
    private String DateToNewDate(String date, String oldFormat, String newFormat) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf_ot = new SimpleDateFormat(newFormat);
        if (date.length() == 0)
            return null;
        //Log.i(TAG + " sdf gatTime", list_date.get(i));
        Date NewDate = sdf.parse(date);
        assert NewDate != null;
        return sdf_ot.format(NewDate);
        //Log.i(TAG + " sdf Time", sdf_ot.format(date));
    }

    /**資料進行氣泡排序**/
    private void invertOrderList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1;
        Date d2;
        String temp_r;
        Drawable iv;
        //这是一个冒泡排序，将大的放在数组前面
        for(int i=0; i<list_date.size()-1; i++){
            for(int j=i+1; j<list_date.size();j++){
                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                d1 = sdf.parse(list_date.get(i), pos1);
                d2 = sdf.parse(list_date.get(j), pos2);
                if(d1.before(d2)){//如果日期靠前，则换顺序
                    //date
                    temp_r = list_date.get(i);
                    list_date.set(i, list_date.get(j));
                    list_date.set(j, temp_r);
                    //sys
                    temp_r = list_sys.get(i);
                    list_sys.set(i, list_sys.get(j));
                    list_sys.set(j, temp_r);
                    //dia
                    temp_r = list_dia.get(i);
                    list_dia.set(i, list_dia.get(j));
                    list_dia.set(j, temp_r);
                    //hr
                    temp_r = list_hr.get(i);
                    list_hr.set(i, list_hr.get(j));
                    list_hr.set(j, temp_r);
                    //Hypertension
                    temp_r = list_Hypertension.get(i);
                    list_Hypertension.set(i, list_Hypertension.get(j));
                    list_Hypertension.set(j, temp_r);
                    //Medicine
                    temp_r = list_Medicine.get(i);
                    list_Medicine.set(i, list_Medicine.get(j));
                    list_Medicine.set(j, temp_r);
                    //iv
                    iv = list_IV.get(i);
                    list_IV.set(i, list_IV.get(j));
                    list_IV.set(j, iv);
                }
            }
        }
    }  

    /**隱藏狀態列和導航欄**/
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