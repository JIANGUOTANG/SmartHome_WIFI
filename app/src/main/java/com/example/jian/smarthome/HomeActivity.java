package com.example.jian.smarthome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.jian.smarthome.Task.BaseActivity;
import com.example.jian.smarthome.Task.MainAsyncTask;
import com.example.jian.smarthome.Task.OpenCurternAsyncTask;
import com.example.jian.smarthome.Task.PMAsyncTask;
import com.example.jian.smarthome.Task.SunAsyncTask;
import com.example.jian.smarthome.Task.TemandHumAsyncTask;

/**
 * Created by jian on 2016/12/19.
 */

public class HomeActivity extends BaseActivity {
    MainAsyncTask sunAsyncTask;
    MainAsyncTask pmasyAsyncTask;
    MainAsyncTask temHumAsyncTask;
    MainAsyncTask openCurtainAsyncTask;
    private TextView tvSun;
    private TextView tvHum;
    private TextView tvTem;
    private TextView tvPm;
    private boolean curtainIsOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        initView();
        sunAsyncTask = new SunAsyncTask();
        pmasyAsyncTask = new PMAsyncTask();
        temHumAsyncTask = new TemandHumAsyncTask();
        sunAsyncTask.setmOnDataUpdate(new MainAsyncTask.onDataUpdate<Integer>() {
            @Override
            public void updateValues(Integer integer) {
                tvSun.setText(integer + "");
                CurtainControl(integer);
            }
        });
        pmasyAsyncTask.setmOnDataUpdate(new MainAsyncTask.onDataUpdate<Integer>() {

            @Override
            public void updateValues(Integer integer) {
                if (integer > 60000) {
                    integer = integer % 11110;
                }
                tvPm.setText(integer + "");
            }
        });
        temHumAsyncTask.setmOnDataUpdate(new MainAsyncTask.onDataUpdate<TemaHum>() {

            @Override
            public void updateValues(TemaHum temaHum) {
                tvHum.setText(temaHum.getHum() + "");
                tvTem.setText(temaHum.getTem() + "");
            }
        });
    }

    private void initView() {
        tvSun = (TextView) findViewById(R.id.home_tvSun);
        tvHum = (TextView) findViewById(R.id.home_tvHum);
        tvTem = (TextView) findViewById(R.id.home_tvTem);
        tvPm = (TextView) findViewById(R.id.home_tvPm);
    }

    @Override
    public void finish() {
        super.finish();
        try {
            // 取消任务
            if (sunAsyncTask != null && sunAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                sunAsyncTask.setCIRCLE(false);
                Thread.sleep(100);
                // 如果Task还在运行，则先取消它
                sunAsyncTask.cancel(true);
            }
            if (pmasyAsyncTask != null && pmasyAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                pmasyAsyncTask.setCIRCLE(false);
                Thread.sleep(100);
                // 如果Task还在运行，则先取消它
                pmasyAsyncTask.cancel(true);
            }
            if (temHumAsyncTask != null && temHumAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                temHumAsyncTask.setCIRCLE(false);
                Thread.sleep(100);
                // 如果Task还在运行，则先取消它
                temHumAsyncTask.cancel(true);
            }
            if (openCurtainAsyncTask != null && openCurtainAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                openCurtainAsyncTask.setCIRCLE(false);
                Thread.sleep(100);
                // 如果Task还在运行，则先取消它
                openCurtainAsyncTask.cancel(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
  public void CurtainControl(int sun){
      if (curtainIsOpen && sun > 500) {
          openCurtainAsyncTask = new OpenCurternAsyncTask(HomeActivity.this, false);
          openCurtainAsyncTask.setOnCurtainStateChangeListener(new MainAsyncTask.OnCurtainStateListener() {
              @Override
              public void state(boolean state) {
                      curtainIsOpen = false;
              }
          });
      } else if (sun < 200 && !curtainIsOpen) {
          openCurtainAsyncTask = new OpenCurternAsyncTask(HomeActivity.this, true);
          openCurtainAsyncTask.setOnCurtainStateChangeListener(new MainAsyncTask.OnCurtainStateListener() {
              @Override
              public void state(boolean state) {

                      curtainIsOpen = true;
//                                bt_OpenCurtain.setText("关闭");

              }
          });
      }

  }
}
