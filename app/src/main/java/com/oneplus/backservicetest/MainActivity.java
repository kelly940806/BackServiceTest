package com.oneplus.backservicetest;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // permission
    public int OVERLAY_PREMISSION_REQ_CODE = 1;
    // back service
    Intent backService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        Button startBtn = (Button) findViewById(R.id.start);
        Button stopBtn = (Button) findViewById(R.id.stop);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backService != null) {
                    Toast.makeText(getApplicationContext(), "Service already existed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                backService = new Intent(getApplicationContext(), BackService.class);
                getApplicationContext().startService(backService);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backService == null) {
                    Toast.makeText(getApplicationContext(), "Service is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getApplicationContext().stopService(backService);
                backService = null;
            }
        });
    }

    public void checkPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(MainActivity.this, "Please grant overlay permission!", Toast.LENGTH_SHORT).show();
            Intent intentSetting = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intentSetting, OVERLAY_PREMISSION_REQ_CODE);
        }
    }
}
