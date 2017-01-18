package com.example.jian.smarthome.Task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian on 2016/12/19.
 */
public class BaseActivity extends AppCompatActivity {
    public MainAsyncTask mAsyncTask;
    public MainAsyncTask mTubeAsynctask;
    public List<MainAsyncTask> tasks = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void finish() {
        super.finish();
        cancell();
    }
    void cancell(){
        try {
            for(MainAsyncTask mainAsyncTask:tasks) {
                // 取消任务
                if (mainAsyncTask != null && mainAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    mainAsyncTask.setCIRCLE(false);
                }
            }
            Thread.sleep(200);
            for(MainAsyncTask mainAsyncTask:tasks) {
                // 取消任务
                if (mainAsyncTask != null && mainAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    // 如果Task还在运行，则先取消它
                    mAsyncTask.cancel(true);
                }
            }
//            // 取消任务
//            if (mAsyncTask != null && mAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
//                mAsyncTask.setCIRCLE(false);
//                Thread.sleep(200);
//                // 如果Task还在运行，则先取消它
//                mAsyncTask.cancel(true);
//            }
//            // 取消任务
//            if (openCurtainAsyncTask != null && openCurtainAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
//                openCurtainAsyncTask.setCIRCLE(false);
//                Thread.sleep(200);
//                // 如果Task还在运行，则先取消它
//                openCurtainAsyncTask.cancel(true);
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public OpenCurternAsyncTask openCurtainAsyncTask;
    boolean curtainIsOpen;
    void CurtainControl(int sun){
        if (curtainIsOpen && sun > 500) {
            openCurtainAsyncTask = new OpenCurternAsyncTask(this, false);
            openCurtainAsyncTask.setOnCurtainStateChangeListener(new MainAsyncTask.OnCurtainStateListener() {
                @Override
                public void state(boolean state) {
                    curtainIsOpen = false;
                }
            });
        } else if (sun < 200 && !curtainIsOpen) {
            openCurtainAsyncTask = new OpenCurternAsyncTask(this, true);
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
