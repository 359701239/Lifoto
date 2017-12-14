package com.external.lifoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    public int REQUEST_CODE = 0;
    public String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Handler handler = new Handler();
    private Runnable start = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    Runnable checkPermissionRunnable = new Runnable() {
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && lacksPermissions(permissions)) {
                requestPermissions(permissions, REQUEST_CODE);
            } else {
                handler.postDelayed(start, 1000);
            }
        }
    };
    Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarsTranslucent();
        setContentView(R.layout.activity_splash);

        handler.postDelayed(checkPermissionRunnable, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            handler.postDelayed(start, 1000);
        } else {
            error();
        }
    }

    public void error() {
        Toast.makeText(this, "需要授予读写文件权限才能正常使用", Toast.LENGTH_SHORT).show();
        handler.postDelayed(finishRunnable, 2000);
    }

    public void setBarsTranslucent() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(start);
        super.onBackPressed();
    }
}
