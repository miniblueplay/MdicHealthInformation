package com.msg.mdic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.msg.mdic.databinding.ActivityLoginBinding;
import com.msg.mdic.databinding.ActivityMainBinding;

public class login extends AppCompatActivity {

    protected ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNav();
        setContentView(R.layout.activity_login);
        //使用ViewBinding後的方法
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //取得應用版本號
        try{
            int vCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            String vName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mBinding.loginVersion.setText("V "+ vName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //點擊登入
        mBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //開發暫時跳過驗證
                Intent intent = new Intent();
                intent.setClass(login.this, field.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open, 0);
                finish();


                if(false)
                if(mBinding.loginAn.length() != 0 && mBinding.loginPs.length() != 0)
                {
                    if(mBinding.loginAn.getText().toString().equals("admin") && mBinding.loginPs.getText().toString().equals("1234"))
                    {
                        //Intent intent = new Intent();
                        intent.setClass(login.this, field.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_activity_open, 0);
                        finish();
                    }else{
                        AlertDialog.Builder alertDialog =
                                new AlertDialog.Builder(login.this);
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("帳號或密碼錯誤");
                        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBinding.loginAn.setText("");
                                mBinding.loginPs.setText("");
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }else{
                    AlertDialog.Builder alertDialog =
                            new AlertDialog.Builder(login.this);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("請輸入帳號密碼");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });
    }


    /**點擊空白區域*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**檢測用戶點擊區域，來判斷是否隱藏軟鍵盤*/
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ( v != null && (v instanceof EditText) ) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if ( event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom ) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**隱藏軟鍵盤*/
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_activity_close);
    }
}