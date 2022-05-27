package com.afauria.sample.ipc.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.afauria.sample.ipc.IBookManager;
import com.afauria.sample.ipc.R;

/**
 * Created by Afauria on 5/27/22.
 */
public class SecondActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "SecondActivity";
    IBookManager mRemoteProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = new Intent("com.afauria.sample.BookManager");
        //使用相同的Intent，不会重复调用onBind
        //intent.setPackage("com.afauria.sample.ipc");
        //使用不同的Intent，会再次调用onBind
        intent.setComponent(new ComponentName("com.afauria.sample.ipc", "com.afauria.sample.ipc.server.BookManagerService"));
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected: ");
        //获取服务端的代理对象
        mRemoteProxy = IBookManager.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected: ");
    }
}
