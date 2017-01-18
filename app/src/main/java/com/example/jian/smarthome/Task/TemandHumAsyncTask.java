package com.example.jian.smarthome.Task;

import android.util.Log;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.TemaHum;
import com.example.jian.smarthome.util.FRODigTube;
import com.example.jian.smarthome.util.FROTemHum;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Constant.hum;
import static com.example.jian.smarthome.Constant.tem;
import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/19.
 */

public class TemandHumAsyncTask extends MainAsyncTask {
    private Float Tem;
    private Float Hum;
    public TemandHumAsyncTask() {
        mExecuteOnExecutor();
    }
    @Override
    public void onBaseOnProgressUpdate(Void... values) {
        if(Constant.tem!=null&&Constant.hum!=null&&mOnDataUpdate!=null) {
            TemaHum temHum = new TemaHum((Integer) tem, (Integer) hum);
            mOnDataUpdate.updateValues(temHum);
        }
    }
    @Override
    void connet() {
        Socket = getSocket(Constant.TEMHUM_IP, Constant.SUN_PORT);
        try {
            while(CIRCLE){
                if(Socket!=null) {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.TEMHUM_CHK);
                    Thread.sleep(Constant.time / 3);
                    read_buff = StreamUtil.readData(Socket.getInputStream());
                    Tem = FROTemHum.getTemData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
                    Hum = FROTemHum.getHumData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
                    if (Tem != null && Hum != null) {
                        tem = (int) (float) Tem;
                        Constant.hum = (int) (float) Hum;
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
    public void update(Integer valuse){
        // 数码管显示
        try {
            if(mTubeSocket!=null) {
                Log.i("jianguotang","tue");
                Constant.TUBE_CMD = FRODigTube.intToCmdString(valuse);
                StreamUtil.writeCommand(Socket.getOutputStream(), Constant.TUBE_CMD);
                Thread.sleep(200);
                publishProgress();
                Thread.sleep(200);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
