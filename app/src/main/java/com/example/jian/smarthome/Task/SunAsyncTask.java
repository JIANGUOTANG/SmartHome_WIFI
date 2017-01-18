package com.example.jian.smarthome.Task;
import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROSun;
import com.example.jian.smarthome.util.StreamUtil;
import java.io.IOException;
import static com.example.jian.smarthome.Task.socketControl.getSocket;
/**
 * Created by jian on 2016/12/18.
 */
public class SunAsyncTask extends MainAsyncTask{
    private Float sun;
    public SunAsyncTask() {
        mExecuteOnExecutor();
    }
    @Override
    public void onBaseOnProgressUpdate(Void... values) {
        if(Constant.sun!=null&&mOnDataUpdate!=null)
        mOnDataUpdate.updateValues(Constant.sun);
    }
    @Override
    void connet() {
        Socket = getSocket(Constant.SUN_IP, Constant.SUN_PORT);
        try {
            while(CIRCLE){
                if(Socket!=null) {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.SUN_CHK);
                    Thread.sleep(Constant.time);
                    read_buff = StreamUtil.readData(Socket.getInputStream());
                    sun = FROSun.getData(Constant.SUN_LEN, Constant.SUN_NUM, read_buff);
                    if (sun != null) {
                        Constant.sun = (int) (float) sun;
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
