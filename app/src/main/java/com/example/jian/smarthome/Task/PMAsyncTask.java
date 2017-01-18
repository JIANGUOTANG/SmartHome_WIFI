package com.example.jian.smarthome.Task;

import android.util.Log;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROSun;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/19.
 */
public class PMAsyncTask extends MainAsyncTask {
    private Float Pm_25;
    public PMAsyncTask() {
        mExecuteOnExecutor();
    }
    @Override
    public void onBaseOnProgressUpdate(Void... values) {
        if(Constant.pm25!=null&&mOnDataUpdate!=null)
            mOnDataUpdate.updateValues(Constant.pm25);
    }
    @Override
    void connet() {
        Socket = getSocket(Constant.PM25_IP, Constant.SUN_PORT);
        try {
            while(CIRCLE){
                if(Socket!=null) {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.PM25_CHK);
                    Thread.sleep(Constant.time);
                    read_buff = StreamUtil.readData(Socket.getInputStream());
                    Pm_25 = FROSun.getData(Constant.PM25_LEN, Constant.PM25_NUM, read_buff);
                    if (Pm_25 != null) {
                        Log.i("jian",Pm_25+"pom");
                        Constant.pm25 = (int) (float) Pm_25;
                    }
                    // 更新界面
                    publishProgress();
                    Thread.sleep(200);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
