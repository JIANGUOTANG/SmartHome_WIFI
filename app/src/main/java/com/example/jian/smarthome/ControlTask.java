package com.example.jian.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.jian.smarthome.util.HexStrConvertUtil;
import com.example.jian.smarthome.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jorble on 2016/3/4.
 */
public class ControlTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = "ControlTask";
	private Context context;
	private TextView infoTv;
	private Boolean statu = false;
	private String cmd;
	private String err;
	private byte[] read_buff;
	private String readStr;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Button button;
	public ControlTask(Context context, TextView infoTv, InputStream inputStream, OutputStream outputStream, String cmd,
					   String err, Button button) {
		this.context = context;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.cmd = cmd;
		this.err = err;
		this.infoTv = infoTv;
		this.button = button;
	}
	private callBack mCallBack;
	public void setmCallBack(callBack mCallBack) {
		this.mCallBack = mCallBack;
	}
    public  static final int READCARD = 1;
	public static final int INTENT = 2;
	public interface callBack{
		void mStartActivity(int optision);
	}
	public void mCancel() throws IOException {
		this.cancel(true);
		inputStream.close();
		outputStream.close();
	}
	/**
	 * 更新界面
	 */
	private String mCardID = "c6cb19bf";
	@Override
	protected void onProgressUpdate(Void... values) {
		switch (cmd) {
		// 读卡
		case Constant.READ_CARD_CMD:
			if (statu&&readStr.length()>0) {
				Constant.CARD_ID = readStr.substring(readStr.length() - 10, readStr.length() - 2);
				infoTv.setText("卡号为" + Constant.CARD_ID);
				infoTv.setTextColor(context.getResources().getColor(R.color.green));
				statu = false;
				if(mCallBack!=null&&Constant.CARD_ID.equals(mCardID)){
					mCallBack.mStartActivity(INTENT);
				}
			} else {
				infoTv.setText("异常");
				button.setEnabled(true);
				infoTv.setTextColor(context.getResources().getColor(R.color.red));

			}
			break;
		default:
			if (statu) {
				//连接正常
				infoTv.setTextColor(context.getResources().getColor(R.color.green));
				statu = false;
				if(mCallBack!=null){
					mCallBack.mStartActivity(READCARD);
				}
			} else {
				infoTv.setText("连接异常");
				button.setEnabled(true);
				infoTv.setTextColor(context.getResources().getColor(R.color.red));
			}
			break;
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
			// 读取返回字符串
			readStr = HexStrConvertUtil.bytesToHexString(read_buff);
			Log.i(TAG, "readStr=" + readStr);
			// 判断是否正常
			statu = !(readStr.equals(err.replace(" ", "").toLowerCase()));
			// 更新界面
			publishProgress();
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		return null;
	}

}
