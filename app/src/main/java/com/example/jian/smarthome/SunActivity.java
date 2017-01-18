package com.example.jian.smarthome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.jian.smarthome.Task.BaseActivity;
import com.example.jian.smarthome.Task.MainAsyncTask;
import com.example.jian.smarthome.Task.SmokeAsyncTask;
import com.example.jian.smarthome.Task.TubeAsyncTask;

/**
 * Created by jian on 2016/12/11.
 */

public class SunActivity extends BaseActivity implements MainAsyncTask.onDataUpdate<Integer>{
    private TextView tvSun;
    TubeAsyncTask tubeAsyncTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun);
        tvSun  = (TextView) findViewById(R.id.sun_tySun);
        mAsyncTask = new SmokeAsyncTask();
        tubeAsyncTask = new TubeAsyncTask();
        tasks.add(mAsyncTask);
        tasks.add(tubeAsyncTask);
        mAsyncTask.setmOnDataUpdate(this);
    }

    @Override
    public void updateValues(Integer integer) {
         tvSun.setText(integer+"");
        tubeAsyncTask.update(integer);
    }
}
