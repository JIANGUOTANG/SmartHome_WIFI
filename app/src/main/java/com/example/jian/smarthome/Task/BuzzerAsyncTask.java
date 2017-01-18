package com.example.jian.smarthome.Task;

import android.content.Context;
import android.widget.Toast;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROIOControl;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/20.
 */

public class BuzzerAsyncTask extends MainAsyncTask {

    private Boolean STATU = false;
    private Context mContext;

    public BuzzerAsyncTask( Context mContext) {

        this.mContext = mContext;
        mExecuteOnExecutor();
    }

    @Override
    public void onBaseOnProgressUpdate(Void... values) {

    }
private boolean socketIsConet;

    public boolean isSocketIsConet() {
        return socketIsConet;
    }

    public void setSocketIsConet(boolean socketIsConet) {
        this.socketIsConet = socketIsConet;
    }

    @Override
    void connet() {
         Socket = getSocket(Constant.BUZZER_IP, Constant.SUN_PORT);
        try {
            if(Socket!=null){
                if(Socket.isConnected()) {
                    socketIsConet = true;
                }else{
                    socketIsConet = false;
                }
            }else{
                socketIsConet = false;
            }
            // 更新界面
            publishProgress();
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
   public void openBuzzer(){
       if(isSocketIsConet()){
           control(Constant.BUZZER_CMD);
       }else{
           Toast.makeText(mContext, "请先连接再操作！", Toast.LENGTH_SHORT).show();
       }
   }
    public void openRedLight(){
        if(isSocketIsConet()){
            control(Constant.RED_CMD);
        }else{
            Toast.makeText(mContext, "请先连接再操作！", Toast.LENGTH_SHORT).show();
        }
    }
    public void openGreenLight(){
        if(isSocketIsConet()){
            control(Constant.GREEN_CMD);
        }else{
            Toast.makeText(mContext, "请先连接再操作！", Toast.LENGTH_SHORT).show();
        }
    }
    public void openBlueLight(){
        if(isSocketIsConet()){
            control(Constant.BLUE_CMD);
        }else{
            Toast.makeText(mContext, "请先连接再操作！", Toast.LENGTH_SHORT).show();
        }
    }
public void closeAll(){
    if(isSocketIsConet()){
        control(Constant.CLOSEALL_CMD);
    }else{
        Toast.makeText(mContext, "请先连接再操作！", Toast.LENGTH_SHORT).show();
    }
}
    private void control(String str){
        if(Socket!=null) {
            try {
                StreamUtil.writeCommand(Socket.getOutputStream(), str);
                Thread.sleep(200);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            read_buff = StreamUtil.readData(Socket.getInputStream());
                            STATU = FROIOControl.isSuccess(Constant.BUZZER_LEN, Constant.BUZZER_NUM, read_buff);
                            // 更新界面
                           publishProgress();
                            Thread.sleep(100);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
