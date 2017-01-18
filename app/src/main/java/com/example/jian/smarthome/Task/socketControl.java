package com.example.jian.smarthome.Task;

import android.util.Log;

import com.example.jian.smarthome.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by jian on 2016/11/22.
 */

public class socketControl {
    /**
     * 建立连接并返回socket，若连接失败返回null
     *
     * @param ip
     * @param port
     * @return
     */
    public static Socket getSocket(String ip, int port) {
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
}
