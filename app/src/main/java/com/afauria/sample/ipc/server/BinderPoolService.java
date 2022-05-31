package com.afauria.sample.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.afauria.sample.ipc.IBinderPool;
import com.afauria.sample.ipc.IUserManager1;
import com.afauria.sample.ipc.IUserManager2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Afauria on 5/31/22.
 */
public class BinderPoolService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderPoolImpl();
    }
}

class BinderPoolImpl extends IBinderPool.Stub {
    public static final int BINDER_USER_MANAGER1 = 1;
    public static final int BINDER_USER_MANAGER2 = 2;
    private IUserManager1 mIUserManager1;
    private IUserManager2 mIUserManager2;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        Log.d("BinderPoolImpl", "queryBinder: " + Thread.currentThread());
        switch (binderCode) {
            case BINDER_USER_MANAGER1:
                return getUserManager1().asBinder();
            case BINDER_USER_MANAGER2:
                return getUserManager2().asBinder();
            default:
                return null;
        }
    }

    @Override
    public IUserManager1 getUserManager1() throws RemoteException {
        if(mIUserManager1 == null) {
            mIUserManager1 = new UserManager1Impl();
        }
        return mIUserManager1;
    }

    @Override
    public IUserManager2 getUserManager2() throws RemoteException {
        if(mIUserManager2 == null) {
            mIUserManager2 = new UserManager2Impl();
        }
        return mIUserManager2;
    }
}

class UserManager1Impl extends IUserManager1.Stub {
    @Override
    public void testUser1() throws RemoteException {
        Log.d("UserManager1Impl", "testUser1: " + Thread.currentThread());
    }
}

class UserManager2Impl extends IUserManager2.Stub {
    @Override
    public void testUser2() throws RemoteException {
        Log.d("UserManager2Impl", "testUser2: " + Thread.currentThread());
    }
}