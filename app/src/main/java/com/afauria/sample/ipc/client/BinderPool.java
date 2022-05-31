package com.afauria.sample.ipc.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.afauria.sample.ipc.IBinderPool;
import com.afauria.sample.ipc.IUserManager1;
import com.afauria.sample.ipc.IUserManager2;
import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Afauria on 5/31/22.
 */
//全局绑定
public class BinderPool extends IBinderPool.Stub {
    private static final String TAG = "BinderPool";
    private static volatile BinderPool sInstance;
    private Context mContext;

    public static BinderPool getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private BinderPool(Context context) {
        mContext = context;
        connectToService();
    }

    private IBinderPool mRemoteProxy;
    private final IBinder.DeathRecipient mBinderDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mRemoteProxy.asBinder().unlinkToDeath(mBinderDeathRecipient, 0);
            mRemoteProxy = null;
            //自动重连
            connectToService();
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: "+Thread.currentThread());
            mRemoteProxy = IBinderPool.Stub.asInterface(service);
            //监听Binder对象死亡
            try {
                mRemoteProxy.asBinder().linkToDeath(mBinderDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            mRemoteProxy = null;
            connectToService();
        }
    };

    private void connectToService() {
        //绑定服务
        Intent intent = new Intent("com.afauria.sample.BinderPool");
        intent.setPackage("com.afauria.sample.ipc");
        mContext.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public IBinder queryBinder(int binderCode) {
        if(mRemoteProxy == null) {
            return null;
        }
        try {
            return mRemoteProxy.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IUserManager1 getUserManager1() {
        if(mRemoteProxy == null) {
            return null;
        }
        try {
            return mRemoteProxy.getUserManager1();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IUserManager2 getUserManager2() throws RemoteException {
        if(mRemoteProxy == null) {
            return null;
        }
        try {
            return mRemoteProxy.getUserManager2();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
