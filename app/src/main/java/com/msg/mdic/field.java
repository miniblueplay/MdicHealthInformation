package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.msg.mdic.adapter.RecycleAdapterDomeUser;
import com.msg.mdic.tool.MysqlCon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class field extends AppCompatActivity  implements RecycleAdapterDomeUser.OnItemClickHandler{

    //宣告Handler
    HandlerThread myHandlerThread;
    private Handler mHandler;

    private MysqlCon Mysql;

    private Context context;
    private Spinner spinner;
    private List<String> list_name;
    private List<String> list_id;
    private ArrayAdapter<String> adapter;

    private RecyclerView recyclerView;
    private RecycleAdapterDomeUser adapterDome;

    private String field_ID; //院內肌少症IbCrGImgeD
    private Map<String,String> NameList;
    private Map<String,String> FieldList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        recyclerView = findViewById(R.id.rv_data);

        //建立HandlerThread
        myHandlerThread = new HandlerThread( "PHP-SQL") ;
        myHandlerThread.start();

        Mysql = new MysqlCon();

        hideNav();

        findID();

        mHandler = new Handler( myHandlerThread.getLooper() ){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        initialAdapter(Mysql.getDataArray("Field_id", ""));
                        break;
                    case 2:
                        NameList = Mysql.getDataArray("Field_GetUserName", field_ID);
                        mHandler.post(runnable);
                        break;
                    default:
                        break;
                }
            }
        };

        mHandler.sendEmptyMessage( 1 ) ;

    }

    // 點擊事件
    @Override
    public void onItemClick(String text) {
        // text即為點擊的內容，可在此顯示Toast或其他處理
        //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(field.this,material.class);
        intent.putExtra("USER_CARDID", text);
        startActivity(intent);
    }

    // 移除事件
    @Override
    public void onItemRemove(int position, String text) {
        // do something...
    }

    private Runnable runnable=new Runnable(){
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecycleView(NameList);
                }
            });
        }
    };

    private void findID() {
        spinner = (Spinner) findViewById(R.id.spinner_field);
        context = this;
    }

    /**(場域)下拉式選單*/
    private void initialAdapter(Map<String,String> field_list) {
        FieldList = field_list;
        list_name = new ArrayList<String>();
        list_id = new ArrayList<String>();
        //獲取map集合中的所有鍵的Set集合, keySet()
        Set<String> keySet = field_list.keySet();
        //有了set集合就可以獲取迭代器
        for (String key : keySet) {
            //有了鍵就可以通過map集合的get方法獲取其對應的値
            String value = field_list.get(key);
            list_id.add(key);
            list_name.add(value);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //R.layout.選擇你新增的Spinner外觀的xml檔名
                adapter = new ArrayAdapter<String>(context, R.layout.my_selected_item, list_name);
                //R.layout.選擇你新增的Spinner選項列外觀的xml檔名
                adapter.setDropDownViewResource(R.layout.my_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(context, list_name.get(position).toString(), Toast.LENGTH_SHORT).show();
                        field_ID = list_id.get(position);
                        mHandler.sendEmptyMessage( 2 ) ;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
        });

    }

    /**人員名單*/
    public void RecycleView(Map<String,String> field){
        Context context = this;
        List<String> CardID = new ArrayList<>();
        List<String> cname = new ArrayList<>();
        //獲取map集合中的所有鍵的Set集合, keySet()
        Set<String> keySet = field.keySet();
        //有了set集合就可以獲取迭代器
        for (String key : keySet) {
            //有了鍵就可以通過map集合的get方法獲取其對應的値
            String value = field.get(key);
            CardID.add(key);
            cname.add(value);
        }
        adapterDome = new RecycleAdapterDomeUser(context, CardID, cname, this);
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(stagger);
        //添加自定义分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
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

    /**結束時*/
    protected void onDestroy() {
        //將執行緒銷毀掉
        super.onDestroy();
        if ( mHandler != null ) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        myHandlerThread.quit() ;
    }
}