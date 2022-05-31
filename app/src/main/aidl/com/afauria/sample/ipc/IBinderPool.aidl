// IBinderPool.aidl
package com.afauria.sample.ipc;
import com.afauria.sample.ipc.IUserManager1;
import com.afauria.sample.ipc.IUserManager2;

interface IBinderPool {
    //方式1：通过参数查询
    IBinder queryBinder(int binderCode);
    //方式2：直接调用方法
    IUserManager1 getUserManager1();
    IUserManager2 getUserManager2();
}