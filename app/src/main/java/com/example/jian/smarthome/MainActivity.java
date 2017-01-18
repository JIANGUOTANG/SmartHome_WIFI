package com.example.jian.smarthome;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ControlTask.callBack,ConnectTask.suCallBack,View.OnClickListener{
private Button btCommitp;
    private EditText edtIp;
    private EditText edtPort;
    private TextView tv_Info;
    private TextView tv_CardId;
    private ConnectTask connectTask;
    private ControlTask controlTask;
    private Button btHome;
    Button btSun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }
    private void initEvent() {
        // 连接
        btHome.setOnClickListener(this);
        btSun.setOnClickListener(this);
        btCommitp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取IP和端口
                btCommitp.setEnabled(false);
                Constant.IP = edtIp.getText().toString().trim();
                Constant.port = Integer.parseInt(edtPort.getText().toString().trim());
                // Log.i(TAG, "ip=" + Constant.IP + " port=" +

                // 开启任务
                connectTask = new ConnectTask(MainActivity.this, tv_Info,  btCommitp);
                connectTask.execute();
                connectTask.setmCallback(MainActivity.this);

            }
        });
    }
    private void initData() {
        edtIp.setText(Constant.IP);
        edtPort.setText(Constant.port+"");
    }
    @Override
    public void finish() {
        super.finish();
        try {
            // 取消任务
            if (connectTask != null && connectTask.getStatus() == AsyncTask.Status.RUNNING) {
                Thread.sleep(500);
                // 如果Task还在运行，则先取消它
                connectTask.cancel(true);
                controlTask.cancel(true);
                connectTask.getmSocket().close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        edtIp = (EditText) findViewById(R.id.edtFidIp);
        edtPort = (EditText) findViewById(R.id.edtFidPort);
        tv_Info= (TextView) findViewById(R.id.tvInfo);
        tv_CardId = (TextView) findViewById(R.id.tvCardID);
        btCommitp = (Button) findViewById(R.id.btCommit);
        btHome = (Button) findViewById(R.id.btHome);
        btSun = (Button) findViewById(R.id.btSun);
        
    }

    @Override
    public void mStartActivity(int opsition) {
        if(opsition==ControlTask.READCARD){
            if (connectTask.getSTATU()) {
                controlTask = new ControlTask(MainActivity.this, tv_CardId, connectTask.getInputStream(),
                        connectTask.getOutputStream(), Constant.READ_CARD_CMD, Constant.READ_CARD_ERROR,btCommitp);
                controlTask.setmCallBack(MainActivity.this);
                controlTask.execute();
            } else {
                Toast.makeText(MainActivity.this, "请先连接再操作！", Toast.LENGTH_SHORT).show();
            }
        }else  if(opsition==ControlTask.INTENT){
            startActivity(new Intent(MainActivity.this,ControlActivity.class));
            btCommitp.setEnabled(true);
            finish();
        }
    }
    @Override
    public void readCard() {
        if (connectTask.getSTATU()) {
            controlTask = new ControlTask(MainActivity.this, tv_CardId, connectTask.getInputStream(),
                    connectTask.getOutputStream(), Constant.FIND_CARD_CMD, Constant.FIND_CARD_ERROR,btCommitp);
            controlTask.setmCallBack(MainActivity.this);
            controlTask.execute();
        } else {
            Toast.makeText(MainActivity.this, "请先连接再操作！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btHome:
                 intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.btSun:
                 intent=new Intent(MainActivity.this,SunActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btBuzzer:
                intent=new Intent(MainActivity.this,BuzzerActivity.class);
                startActivity(intent);
                break;
        }
    }
}
