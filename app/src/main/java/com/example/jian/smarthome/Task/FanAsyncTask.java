package com.example.jian.smarthome.Task;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROIOControl;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/19.
 */

public class FanAsyncTask extends MainAsyncTask {
    @Override
    public void onBaseOnProgressUpdate(Void... values) {
        if (statu == true) {

        }else{

        }
    }
    Boolean statu;
    @Override
    void connet() {
        Socket = getSocket(Constant.FAN_IP, Constant.SUN_PORT);
        try {
            while(CIRCLE){
                if(Socket!=null) {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.PM25_CHK);
                    Thread.sleep(200);
                     Boolean statu= FROIOControl.isSuccess(Constant.FAN_LEN, Constant.FAN_NUM, read_buff);
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
