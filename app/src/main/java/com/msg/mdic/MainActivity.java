package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import com.msg.mdic.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding mBinding;
    //宣告Handler
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNav();
        setContentView(R.layout.activity_main);
        //使用ViewBinding後的方法
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //建立Handler
        mHandler = new Handler();

        mHandler.postDelayed(jump, 4000); //4秒跳轉
    }

    private static final int GOTO_MAIN_ACTIVITY = 0;
    /**跳轉頁面(子執行序)*/
    private Runnable jump=new Runnable(){
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, field.class); //之後改登入畫面
            startActivity(intent);
            overridePendingTransition(R.anim.anim_activity_open, 0);
            finish();
        }
    };

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

    protected void onDestroy() {
        //將執行緒銷毀掉
        super.onDestroy();
        if ( mHandler != null ) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_activity_close);
    }
}