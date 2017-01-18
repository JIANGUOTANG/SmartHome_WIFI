package com.example.jian.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.jian.smarthome.util.FROPm25;
import com.example.jian.smarthome.util.FROSun;
import com.example.jian.smarthome.util.FROTemHum;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Jorble on 2016/3/4.
 */
public class ControlConnectTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	TextView sun_tv;
	TextView tem_tv;
	TextView hum_tv;
	TextView pm25_tv;
	private Float sun;
	private Float tem;
	private Float hum;
	private Float pm25;
	private byte[] read_buff;
	private Socket sunSocket;
	private Socket temHumSocket;
	private Socket pm25Socket;

	private boolean CIRCLE = false;
	public ControlConnectTask(Context context, TextView tem_tv, TextView hum_tv, TextView sun_tv, TextView pm25_tv) {
		this.context = context;
		this.sun_tv = sun_tv;
		this.tem_tv = tem_tv;
		this.hum_tv = hum_tv;
		this.pm25_tv = pm25_tv;
	}
	/**
	 * 更新界面
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		//显示数据
		if (Constant.sun != null && sun_tv!=null) {
			sun_tv.setText(String.valueOf(Constant.sun)+"Lux");
		}
		if (Constant.tem != null && tem_tv!=null) {
			tem_tv.setText(String.valueOf(Constant.tem)+"°C");
		}
		if (Constant.hum != null&& hum_tv!=null) {
			hum_tv.setText(String.valueOf(Constant.hum)+"%");
		}
		if (Constant.pm25 != null&& pm25_tv!=null) {
			pm25_tv.setText(String.valueOf(Constant.pm25)+"μg/m3");
		}
		
	}

	/**
	 * 子线程任务
	 * 
	 * @param params
	 * @return
	 */
	@Override
	protected Void doInBackground(Void... params) {
		// 连接
		sunSocket = getSocket(Constant.SUN_IP, Constant.SUN_PORT);
		temHumSocket = getSocket(Constant.TEMHUM_IP, Constant.TEMHUM_PORT);
		pm25Socket = getSocket(Constant.PM25_IP, Constant.PM25_PORT);
		// 循环读取数据
		while (CIRCLE) {
			// 如果全部连接成功
			if (sunSocket!=null && temHumSocket!=null && pm25Socket!=null) {
//			if (temHumSocket!=null) {
				try {
					// 查询光照度
					StreamUtil.writeCommand(sunSocket.getOutputStream(), Constant.SUN_CHK);
					Thread.sleep(Constant.time/3);
					read_buff = StreamUtil.readData(sunSocket.getInputStream());
					sun = FROSun.getData(Constant.SUN_LEN, Constant.SUN_NUM, read_buff);
					if (sun != null) {
						Constant.sun = (int) (float) sun;
					}
					Log.i(Constant.TAG, "Constant.sun="+Constant.sun);
					// 查询温湿度
					StreamUtil.writeCommand(temHumSocket.getOutputStream(), Constant.TEMHUM_CHK);
					Thread.sleep(Constant.time/3);
					read_buff = StreamUtil.readData(temHumSocket.getInputStream());
					tem = FROTemHum.getTemData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
					hum = FROTemHum.getHumData(Constant.TEMHUM_LEN, Constant.TEMHUM_NUM, read_buff);
					if (tem != null && hum != null) {
						Constant.tem = (int) (float) tem;
						Constant.hum = (int) (float) hum;
					}
					Log.i(Constant.TAG, "Constant.tem="+Constant.tem);
					Log.i(Constant.TAG, "Constant.hum="+Constant.hum);

					// 查询PM2.5
					StreamUtil.writeCommand(pm25Socket.getOutputStream(), Constant.PM25_CHK);
					Thread.sleep(Constant.time/3);
					read_buff = StreamUtil.readData(pm25Socket.getInputStream());
					pm25 = FROPm25.getData(Constant.PM25_LEN, Constant.PM25_NUM, read_buff);
					if (pm25 != null) {
						Constant.pm25 = (int) (float) pm25;
					}
					Log.i(Constant.TAG, "Constant.pm25="+Constant.pm25);

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
		return null;
	}

	/**
	 * 建立连接并返回socket，若连接失败返回null
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	private Socket getSocket(String ip, int port) {
		Socket mSocket = new Socket();
		InetSocketAddress mSocketAddress = new InetSocketAddress(ip, port);
		// socket连接
		try {
			// 设置连接超时时间为3秒
			mSocket.connect(mSocketAddress, 3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 检查是否连接成功
		if (mSocket.isConnected()) {
			Log.i(Constant.TAG, ip+"连接成功！");
			return mSocket;
		} else {
			Log.i(Constant.TAG, ip+"连接失败！");
			return null;
		}
	}

	public void setCIRCLE(boolean cIRCLE) {
		CIRCLE = cIRCLE;
	}

	@Override
	protected void onCancelled() {

	}
	
	

}
