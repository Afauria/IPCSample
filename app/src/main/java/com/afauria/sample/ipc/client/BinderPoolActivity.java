package com.afauria.sample.ipc.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.afauria.sample.ipc.IBinderPool;
import com.afauria.sample.ipc.IUserManager1;
import com.afauria.sample.ipc.IUserManager2;
import com.afauria.sample.ipc.R;

/**
 * Created by Afauria on 5/27/22.
 */
public class BinderPoolActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "BinderPoolActivity";
    private IBinderPool mRemoteProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        connectToService();
    }

    private void connectToService() {
        //绑定服务
        Intent intent = new Intent("com.afauria.sample.BinderPool");
        intent.setPackage("com.afauria.sample.ipc");
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected: ");
        mRemoteProxy = IBinderPool.Stub.asInterface(service);
        try {
            //方式1：使用queryBinder查询接口1、2，并且调用asInterface转换
            IUserManager1.Stub.asInterface(mRemoteProxy.queryBinder(1)).testUser1();
            IUserManager2.Stub.asInterface(mRemoteProxy.queryBinder(2)).testUser2();
            //方式2：直接调用方法获取接口1、2
            mRemoteProxy.getUserManager1().testUser1();
            mRemoteProxy.getUserManager2().testUser2();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected: ");
    }
}