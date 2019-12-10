package com.oneplus.backservicetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BackService extends Service {
    /*private int connectId;
    public BackService(int connectId) {
        this.connectId = connectId;
    }*/
    private String TAG = "BackService";
    private WindowManager wm;
    WindowManager.LayoutParams params;
    //private View view;
    private LinearLayout layout;
    private Button btn;
    private Button sBtn;
    private int cnt = 0;
    private boolean doClick = true;

    private View.OnTouchListener btnOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return btnTouched(v, event);
        }
    };

    private View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnClicked();
        }
    };

    float initX;
    float initY;
    float offsetX;
    float offsetY;
    private boolean moving;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        }

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        btn = new Button(this);
        //btn.setText("test");
        btn.setBackground(getDrawable(R.drawable.start_icon));
        //btn.getBackground().setAlpha(30);
        btn.setOnTouchListener(btnOnTouchListener);
        btn.setOnClickListener(btnOnClickListener);

        sBtn = new Button(this);
        //btn.setText("test");
        sBtn.setBackground(getDrawable(R.drawable.stop_icon));
        sBtn.setHeight(10);
        sBtn.setWidth(10);
        sBtn.getBackground().setAlpha(95);
        //btn.getBackground().setAlpha(30);
        sBtn.setOnTouchListener(btnOnTouchListener);

        layout = new LinearLayout(this);
        layout.addView(btn);
        layout.addView(sBtn);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG);
        lparams.gravity = Gravity.LEFT | Gravity.TOP;

        //WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        //params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        wm.addView(layout, params);

        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wm != null) {
            wm.removeView(layout);
            wm = null;
        }
    }

    public void btnClicked() {
        cnt++;
        Toast.makeText(this,"click" + cnt + "!", Toast.LENGTH_SHORT).show();
    }

    public boolean btnTouched(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                doClick = true;
                int[] pos = new int[2];
                layout.getLocationOnScreen(pos);
                initX = pos[0];
                initY = pos[1];
                offsetX = X - initX;
                offsetY = Y - initY;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP-> " + doClick);
                if (doClick)
                    view.performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                doClick = false;
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) layout.getLayoutParams();
                float newX = event.getRawX() - offsetX;
                float newY = event.getRawY() - offsetY;
                layoutParams.x = (int)newX;
                layoutParams.y = (int)newY;
                //view.setLayoutParams(layoutParams);
                wm.updateViewLayout(layout, layoutParams);
                break;
        }
        return true;
    }
}
