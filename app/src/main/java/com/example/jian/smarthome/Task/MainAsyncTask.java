package com.example.jian.smarthome.Task;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;


/**
 * Created by jian on 2016/12/18.
 */

public abstract class MainAsyncTask extends AsyncTask<Void,Void,Void> {

     onDataUpdate mOnDataUpdate;

    byte[] read_buff;
    public void setmOnDataUpdate(onDataUpdate mOnDataUpdate) {
        this.mOnDataUpdate = mOnDataUpdate;
    }
     MainAsyncTask.OnCurtainStateListener onCurtainStateChangeListener;
    public void setOnCurtainStateChangeListener(MainAsyncTask.OnCurtainStateListener onCurtainStateChangeListener) {
        this.onCurtainStateChangeListener = onCurtainStateChangeListener;
    }
    public interface OnCurtainStateListener{
        void state(boolean state);
    }
     boolean CIRCLE = false;

    public void setCIRCLE(boolean CIRCLE) {
        this.CIRCLE = CIRCLE;
    }

    public interface onDataUpdate<T>{
        void updateValues(T t);
    }
    public Socket Socket;
    public Socket mTubeSocket;
    @Override
    protected Void doInBackground(Void... params) {
        connet();
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {

        onBaseOnProgressUpdate(values);
    }
//    public abstract Void onBaseDoInBackgroud();
    public abstract void onBaseOnProgressUpdate(Void... values);
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
    void mExecuteOnExecutor(){
        CIRCLE = true;
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    abstract void connet();

}
