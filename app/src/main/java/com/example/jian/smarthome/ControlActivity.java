package com.example.jian.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jian.smarthome.Task.Bean;
import com.example.jian.smarthome.Task.sunTask;

/**
 * Created by jian on 2016/11/19.
 */

public class ControlActivity extends AppCompatActivity implements sunTask.dataChangeListener, View.OnClickListener {
    private Context context;
    private TextView sun_tv;
    private TextView tem_tv;
    private TextView hum_tv;
    private TextView pm25_tv;

    private Button bt_OpenCurtain;
    private sunTask openCurtainTask;
    private sunTask closeCurtainTask;
    private ControlConnectTask controlConnectTask;
    private sunTask ChkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        context = this;
        // 绑定控件
        bindView();
        // 初始化数据
        initData();
        // 事件监听
        initEvent();
    }
    /**
     * 绑定控件
     */
    private void bindView() {
        sun_tv = (TextView) findViewById(R.id.tv_sun);
        tem_tv = (TextView) findViewById(R.id.tv_tem);
        hum_tv = (TextView) findViewById(R.id.tv_hum);
        pm25_tv = (TextView) findViewById(R.id.tv_pm);
        bt_OpenCurtain = (Button) findViewById(R.id.control_btcurturn);
    }
    /**
     * 初始化数据
     */
    private void initData() {

    }
    /**
     * 按钮监听
     */
    private void initEvent() {
        // 开启任务
        bt_OpenCurtain.setOnClickListener(this);
        ChkTask = new sunTask(this, sunTask.CHKALL);
        ChkTask.setCIRCLE(true);
        ChkTask.setmDataChange(this);
        ChkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        controlConnectTask = new ControlConnectTask(context, tem_tv, hum_tv, sun_tv, pm25_tv);
//        controlConnectTask.setCIRCLE(true);
//        controlConnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @Override
    public void finish() {
        super.finish();
        try {
            // 取消任务
            if (ChkTask != null && ChkTask.getStatus() == AsyncTask.Status.RUNNING) {
                ChkTask.setCIRCLE(false);
                Thread.sleep(2 * Constant.time);
                // 如果Task还在运行，则先取消它
                ChkTask.cancel(true);
                if(openCurtainTask!=null)
                openCurtainTask.cancel(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(Object sun) {
        Bean mbean = (Bean) sun;
        sun_tv.setText(mbean.getSun() + "Lux");
        tem_tv.setText(mbean.getTem() + "°C");
        hum_tv.setText(mbean.getHum() + "%");
        pm25_tv.setText(mbean.getPm25() + "μg/m3");

        if(!curtainIsOpen&&mbean.getSun()<200){
            openCurtainTask = new sunTask(this, sunTask.OPENCURTAIN, true);
            openCurtainTask.setOnCurtainStateChangeListener(new sunTask.OnCurtainStateChangeListener() {
                @Override
                public void state(boolean state) {
                    if (state) {
                        curtainIsOpen = true;
                        bt_OpenCurtain.setText("关闭");
                    } else {
                        bt_OpenCurtain.setText("打开");
                        curtainIsOpen = false;
                    }
                }
            });
                openCurtainTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if(curtainIsOpen&&mbean.getSun()>500){
            openCurtainTask = new sunTask(this, sunTask.OPENCURTAIN, false);
            openCurtainTask.setOnCurtainStateChangeListener(new sunTask.OnCurtainStateChangeListener() {
                @Override
                public void state(boolean state) {
                    if (state) {
                        curtainIsOpen = true;
                        bt_OpenCurtain.setText("关闭");
                    } else {
                        bt_OpenCurtain.setText("打开");
                        curtainIsOpen = false;
                    }
                }
            });
                openCurtainTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    private boolean curtainIsOpen = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_btcurturn:
                if(!curtainIsOpen){
                    openCurtainTask = new sunTask(this, sunTask.OPENCURTAIN, true);

                }else{
                    openCurtainTask = new sunTask(this, sunTask.OPENCURTAIN, false);
                }
                openCurtainTask.setOnCurtainStateChangeListener(new sunTask.OnCurtainStateChangeListener() {
                    @Override
                    public void state(boolean state) {
                        if(state){
                            curtainIsOpen = true;
                            bt_OpenCurtain.setText("关闭");
                        }else{
                            bt_OpenCurtain.setText("打开");
                            curtainIsOpen = false;
                        }
                    }
                });
                openCurtainTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }
}
