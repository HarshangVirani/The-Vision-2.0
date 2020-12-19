package com.example.thevision2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thevision2.Messages.MessageActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView namePlate;
    private String tag ="MainActivity";
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        setReference();
        setSplashScreen();
        changeStatusBarColor();
    }

    private void checkPermission(){
        int contextCompat = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (contextCompat == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"PERMISSION_GRANTED",Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{(Manifest.permission.READ_SMS)},REQUEST_CODE);
            Log.d(tag,"Error in permission");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (requestCode == REQUEST_CODE){
                if (grantResults.length>=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"PERMISSION_GRANTED",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"You do not have permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setReference() {
        logo = findViewById(R.id.logo);
        namePlate = findViewById(R.id.text_view);
    }

    private void setSplashScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SignUpAndLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
    }

}