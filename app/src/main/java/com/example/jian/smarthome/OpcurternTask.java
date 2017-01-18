package com.example.jian.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.jian.smarthome.util.FROIOControl;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jorble on 2016/3/4.
 */
public class OpcurternTask extends AsyncTask<Void, Void, Void> {
	private Context context;
	private Boolean statu;
	private String cmd;
	private byte[] read_buff;
	private InputStream inputStream;
	private OutputStream outputStream;
	public OpcurternTask(Context context, InputStream inputStream, OutputStream outputStream, String cmd) {
		this.context = context;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.cmd = cmd;
	}
	/**
	 * 更新界面
	 */
	@Override
	protected void onProgressUpdate(Void... values) {
		if (statu == true) {
			Toast.makeText(context, "操作成功！", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "操作失败！", Toast.LENGTH_SHORT).show();
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
		try {
			// 发送命令
			StreamUtil.writeCommand(outputStream, cmd);
			Thread.sleep(200);
			read_buff = StreamUtil.readData(inputStream);
			statu = FROIOControl.isSuccess(Constant.CURTAIN_LEN, Constant.CURTAIN_NUM, read_buff);
			// 更新界面
			publishProgress();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
