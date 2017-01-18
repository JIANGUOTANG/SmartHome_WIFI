package com.example.jian.smarthome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.jian.smarthome.Task.BaseActivity;
import com.example.jian.smarthome.Task.BuzzerAsyncTask;

/**
 * Created by jian on 2016/12/21.
 */

public class BuzzerActivity extends BaseActivity implements View.OnClickListener{

    private Button btRed,btGreen,btBlue,btBuzzer,btClose;
    BuzzerAsyncTask buzzerAsyncTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buzzer_activity);
        buzzerAsyncTask = new BuzzerAsyncTask(this);
        tasks.add(buzzerAsyncTask);
        initView();
        initEvent();
    }

    private void initEvent() {
        btRed.setOnClickListener(this);
        btBlue.setOnClickListener(this);
        btBuzzer.setOnClickListener(this);
        btClose.setOnClickListener(this);
        btGreen.setOnClickListener(this);

    }

    private void initView() {
        btRed = (Button) findViewById(R.id.btRed);
        btBlue = (Button) findViewById(R.id.btBlue);
        btBuzzer = (Button) findViewById(R.id.btBuzzer);
        btClose = (Button) findViewById(R.id.btCloseAll);
        btGreen = (Button) findViewById(R.id.btGreen);
    }

    @Override
    public void finish() {
        buzzerAsyncTask.closeAll();
        super.finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBlue:
                buzzerAsyncTask.openBlueLight();
                break;
            case R.id.btRed:
                buzzerAsyncTask.openRedLight();
                break;
            case R.id.btGreen:
                buzzerAsyncTask.openGreenLight();
                break;
            case R.id.btBuzzer:
                buzzerAsyncTask.openBuzzer();
                break;
            case R.id.btCloseAll:
                buzzerAsyncTask.closeAll();

                break;
        }
    }

}
