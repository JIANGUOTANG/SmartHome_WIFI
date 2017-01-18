package com.example.jian.smarthome.Task;

import android.content.Context;
import android.widget.Toast;

import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROIOControl;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;

import static com.example.jian.smarthome.Task.socketControl.getSocket;

/**
 * Created by jian on 2016/12/19.
 */
public class OpenCurternAsyncTask extends MainAsyncTask{
    private Context mContext;
    private Boolean statu;
    private boolean opsition ; //true代表打开,false 代表关闭
    public OpenCurternAsyncTask(Context mContext,boolean opsition) {
        this.mContext = mContext;
        this.opsition =  opsition;
        mExecuteOnExecutor();
    }
    @Override
    public void onBaseOnProgressUpdate(Void... values) {
        if (statu == true) {
            onCurtainStateChangeListener.state(opsition);
            Toast.makeText(mContext, "操作成功！", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "操作失败！", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    void connet() {
        Socket = getSocket(Constant.CURTAINIP, Constant.CURTAINPort);
        try {
            if(Socket!=null) {
                if (opsition) {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.CURTAIN_ON);
                } else {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.CURTAIN_OFF);
                }
                Thread.sleep(200);
                read_buff = StreamUtil.readData(Socket.getInputStream());
                statu = FROIOControl.isSuccess(Constant.CURTAIN_LEN, Constant.CURTAIN_NUM, read_buff);
                // 更新界面
                publishProgress();
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
