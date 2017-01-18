package com.example.jian.smarthome.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.jian.smarthome.Constant;
import com.example.jian.smarthome.util.FROIOControl;
import com.example.jian.smarthome.util.FROPm25;
import com.example.jian.smarthome.util.FROSun;
import com.example.jian.smarthome.util.FROTemHum;
import com.example.jian.smarthome.util.StreamUtil;
import java.io.IOException;
import java.net.Socket;
import static com.example.jian.smarthome.Task.socketControl.getSocket;
/**
 * Created by jian on 2016/11/22.
 */
public class sunTask extends AsyncTask<Void, Void, Void> {
    private Float sun;
    private Float tem;
    private Float hum;
    private Float pm25;
    private Socket Socket;
    private Socket sunSocket;
    private Socket temHumSocket;
    private Socket pm25Socket;
    private Boolean statu;
    private byte[] read_buff;
    public static final int CHKSUN = 1;//查询光照度
    public static final int CHKTEMANDHUM = 2;
    public static final int CHKPM = 3;
    public static final int CHKALL = 4;
    public static final int OPENCURTAIN = 5;
    private int dowhat;
    private Context mContext;
    private boolean CIRCLE = false;
    public sunTask(Context context, int dowhat) {
        this.mContext = context;
        this.dowhat = dowhat;
    }
    private boolean opsition ; //true代表打开,false 代表关闭

    public sunTask(Context context, int dowhat,boolean opsition) {
        this.mContext = context;
        this.dowhat = dowhat;
        this.opsition = opsition;
    }
    public Boolean getStatu() {
        return statu;
    }
    public void setStatu(Boolean statu) {
        this.statu = statu;
    }
    @Override
    protected Void doInBackground(Void... params) {
        switch (dowhat) {
            case CHKSUN:
                Socket = getSocket(Constant.SUN_IP, Constant.SUN_PORT);
                try {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.SUN_CHK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chkValues();
                break;
            case CHKTEMANDHUM:
                Socket = getSocket(Constant.TEMHUM_IP, Constant.TEMHUM_PORT);
                try {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.TEMHUM_CHK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chkValues();
                break;
            case CHKPM:
                Socket = getSocket(Constant.PM25_IP, Constant.PM25_PORT);
                try {
                    StreamUtil.writeCommand(Socket.getOutputStream(), Constant.PM25_CHK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chkValues();
                break;
            case CHKALL:
                // 连接
                sunSocket = socketControl.getSocket(Constant.SUN_IP, Constant.SUN_PORT);
                temHumSocket = socketControl.getSocket(Constant.TEMHUM_IP, Constant.TEMHUM_PORT);
                pm25Socket = socketControl.getSocket(Constant.PM25_IP, Constant.PM25_PORT);
                // 循环读取数据
                while (CIRCLE) {
                    // 如果全部连接成功
                    if (sunSocket != null && temHumSocket != null && pm25Socket != null) {
                        try {
                            // 查询光照度
                            StreamUtil.writeCommand(sunSocket.getOutputStream(), Constant.SUN_CHK);
                            Thread.sleep(Constant.time / 3);
                            read_buff = StreamUtil.readData(sunSocket.getInputStream());
                            sun = FROSun.getData(Constant.SUN_LEN, Constant.SUN_NUM, read_buff);
                            if (sun != null) {
                                Constant.sun = (int) (float) sun;
                            }
                            Log.i(Constant.TAG, "Constant.sun=" + Constant.sun);
                            // 查询温湿度
                            StreamUtil.writeCommand(temHumSocket.getOutputStream(), Constant.TEMHUM_CHK);
                            Thread.sleep(Constant.time / 3);
                            read_buff = StreamUtil.readData(temHumSocket.getInputStream());
                            tem = FROTemHum.getTemData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
                            hum = FROTemHum.getHumData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
                            if (tem != null && hum != null) {
                                Constant.tem = (int) (float) tem;
                                Constant.hum = (int) (float) hum;
                            }
                            Log.i(Constant.TAG, "Constant.tem=" + Constant.tem);
                            Log.i(Constant.TAG, "Constant.hum=" + Constant.hum);

                            // 查询PM2.5
                            StreamUtil.writeCommand(pm25Socket.getOutputStream(), Constant.PM25_CHK);
                            Thread.sleep(Constant.time / 3);
                            read_buff = StreamUtil.readData(pm25Socket.getInputStream());
                            pm25 = FROPm25.getData(Constant.PM25_LEN, Constant.PM25_NUM, read_buff);
                            if (pm25 != null) {
                                Constant.pm25 = (int) (float) pm25;
                            }
                            Log.i(Constant.TAG, "Constant.pm25=" + Constant.pm25);

                            // 更新界面
                            publishProgress();
                            Thread.sleep(200);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case OPENCURTAIN:
                Socket = getSocket(Constant.CURTAINIP, Constant.CURTAINPort);
                try {
                      if(opsition) {
                          StreamUtil.writeCommand(Socket.getOutputStream(), Constant.CURTAIN_ON);
                      }else{
                          StreamUtil.writeCommand(Socket.getOutputStream(), Constant.CURTAIN_OFF);
                      }
                    Thread.sleep(200);
                    read_buff = StreamUtil.readData(Socket.getInputStream());
                    statu = FROIOControl.isSuccess(Constant.CURTAIN_LEN, Constant.CURTAIN_NUM, read_buff);
                    // 更新界面
                    publishProgress();
                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
    private void chkValues() {
        try {
            while (CIRCLE) {
                if (Socket!=null) {
                    Thread.sleep(Constant.time);
                    read_buff = StreamUtil.readData(Socket.getInputStream());
                    switch (dowhat) {
                        case CHKSUN:

                            sun = FROSun.getData(Constant.SUN_LEN, Constant.SUN_NUM, read_buff);
                            Log.i(Constant.TAG, "Constant.sun=" + Constant.sun);
                            if (sun != null) {
                                Constant.sun = (int) (float) sun;
                            }
                            // 更新界面
                            publishProgress();
                            Thread.sleep(200);
                            break;
                        case CHKTEMANDHUM:
                            tem = FROTemHum.getTemData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
                            hum = FROTemHum.getHumData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
                            if (tem != null && hum != null) {
                                Constant.tem = (int) (float) tem;
                                Constant.hum = (int) (float) hum;
                            }
                            // 更新界面
                            publishProgress();
                            Thread.sleep(200);
                            break;
                        case CHKPM:
                            pm25 = FROPm25.getData(Constant.PM25_LEN, Constant.PM25_NUM, read_buff);
                            if (pm25 != null) {
                                Constant.pm25 = (int) (float) pm25;
                            }
                            // 更新界面
                            publishProgress();
                            Thread.sleep(200);
                            break;
                    }
                }
                Thread.sleep(Constant.time);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private dataChangeListener mDataChange;

    public void setmDataChange(dataChangeListener mDataChange) {
        this.mDataChange = mDataChange;
    }

    public interface dataChangeListener {
        void update(Object sun);
    }
    private OnCurtainStateChangeListener onCurtainStateChangeListener;
    public void setOnCurtainStateChangeListener(OnCurtainStateChangeListener onCurtainStateChangeListener) {
        this.onCurtainStateChangeListener = onCurtainStateChangeListener;
    }

    public interface OnCurtainStateChangeListener{
        void state(boolean state);
    }
    public void setCIRCLE(boolean cIRCLE) {
        CIRCLE = cIRCLE;
    }
     /**
     * 更新界面
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        //显示数据
        switch (dowhat) {
            case CHKSUN:
                if (Constant.sun != null && mDataChange != null) {
                    mDataChange.update(Constant.sun);
                }
                break;
            case CHKTEMANDHUM:
                if (Constant.tem != null && mDataChange != null && Constant.hum != null) {
                    TemHum TemHum = new TemHum(Constant.tem, Constant.hum);
                    mDataChange.update(TemHum);
                }
                break;
            case CHKPM:
                if (Constant.pm25 != null && mDataChange != null) {
                    mDataChange.update(Constant.pm25);
                }
                break;
            case CHKALL:
                //显示数据
                if (Constant.sun != null&&Constant.tem != null&&Constant.hum != null&&Constant.pm25 != null&&mDataChange!=null ) {
                    Bean bean = new Bean(Constant.sun,Constant.tem,Constant.hum,Constant.pm25);
                    mDataChange.update(bean);
                }
                break;
            case OPENCURTAIN:
                if (statu == true) {
                    onCurtainStateChangeListener.state(opsition);
                    Toast.makeText(mContext, "操作成功！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "操作失败！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    protected void onCancelled() {
        if (Socket!=null){
            try {
                Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

class TemHum {
    private Integer tem;
    private Integer hum;

    public Integer getTem() {
        return tem;
    }

    public void setTem(Integer tem) {
        this.tem = tem;
    }

    public Integer getHum() {
        return hum;
    }

    public void setHum(Integer hum) {
        this.hum = hum;
    }

    public TemHum(Integer tem, Integer hum) {
        this.tem = tem;
        this.hum = hum;
    }
}