package com.example.jian.smarthome.Task;

import android.util.Log;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FRODigTube;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/20.
 */

public class TubeAsyncTask extends MainAsyncTask {

    public TubeAsyncTask() {
        mExecuteOnExecutor();
    }
    @Override
    public void onBaseOnProgressUpdate(Void... values) {

    }
    @Override
    void connet(){
            Socket = getSocket(Constant.TUBE_IP, Constant.TUBE_PORT);
         boolean isc =true;
        while (isc){
            if (Socket!=null){
                isc = false;
            try {
                try {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.TUBE_CMD);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.sleep(200);

                // 输出客流数

                // 更新界面
                publishProgress();

                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }}
        }

//        // 数码管显示
//        try {
//            Constant.TUBE_CMD = FRODigTube.intToCmdString(Constant.TUBE_VALUSE);
//            StreamUtil.writeCommand(Socket.getOutputStream() , Constant.TUBE_CMD);
//            Thread.sleep(200);
//            publishProgress();
//            Thread.sleep(200);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
   public void update(Integer valuse){
       // 数码管显示
       try {
           if(Socket!=null) {
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
