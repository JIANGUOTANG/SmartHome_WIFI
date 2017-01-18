package com.example.jian.smarthome.Task;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROSmoke;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/21.
 */

public class SmokeAsyncTask extends MainAsyncTask {
    public SmokeAsyncTask() {

        mExecuteOnExecutor();
    }

    @Override
    public void onBaseOnProgressUpdate(Void... values) {
        if(Constant.SMOKE!=null&&mOnDataUpdate!=null)
            mOnDataUpdate.updateValues(Constant.SMOKE);
    }
    private Float smoke;

    @Override
    void connet() {
        Socket = getSocket(Constant.SMOKE_IP, Constant.SUN_PORT);
        try {
            while(CIRCLE){
                if(Socket!=null) {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.SMOKE_CHK);
                    Thread.sleep(200);
                    read_buff = StreamUtil.readData(Socket.getInputStream());
                    smoke = FROSmoke.getData(Constant.SMOKE_LEN, Constant.SMOKE_NUM, read_buff);
                    if (smoke!= null) {
                        Constant.SMOKE=((int)(float)smoke);
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
