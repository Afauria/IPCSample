package com.afauria.sample.ipc.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.afauria.sample.ipc.Book;
import com.afauria.sample.ipc.IBookManager;
import com.afauria.sample.ipc.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "MainActivity";
    IBookManager mRemoteProxy;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent("com.afauria.sample.BookManager");
        //需要使用显式Intent
        intent.setPackage("com.afauria.sample.ipc");
        bindService(intent, this, BIND_AUTO_CREATE);
        HandlerThread handlerThread = new HandlerThread("childThread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
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
        //只在连接意外中断时调用，例如服务端进程崩溃或者终止时
        //主动unbindService不会调用
    }

    public void onInitBooksClick(View view) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "onInitBooksClick before: " + Thread.currentThread());
                    //耗时调用
                    mRemoteProxy.initBooks();
                    Log.d(TAG, "onInitBooksClick after: " + Thread.currentThread());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        //主线程调用测试
        task.run();
        //子线程调用测试
//        mHandler.post(task);
        //新线程调用测试
//        new Thread(task).start();
    }

    public void onInitBooksOneWayClick(View view) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "onInitBooksOneWayClick before: " + Thread.currentThread());
                    //耗时调用
                    mRemoteProxy.initBooksOneWay();
                    Log.d(TAG, "onInitBooksOneWayClick after: " + Thread.currentThread());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        task.run();
//        new Thread(task).start();
    }

    public void onListBooksClick(View view) {
        try {
            List<Book> books = mRemoteProxy.listBooks();
            Log.d(TAG, "onListBooksClick: " + books.size());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onAddBookInClick(View view) {
        try {
            Book book = new Book("《龙族》", 30);
            mRemoteProxy.addBookIn(book);
            Log.d(TAG, "onAddBookInClick: " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onAddBookOutClick(View view) {
        try {
            Book book = new Book("《龙族》", 30);
            mRemoteProxy.addBookOut(book);
            Log.d(TAG, "onAddBookOutClick: " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onAddBookInoutClick(View view) {
        try {
            Book book = new Book("《龙族》", 30);
            mRemoteProxy.addBookInout(book);
            Log.d(TAG, "onAddBookInOutClick: " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onFindBookClick(View view) {
        try {
            Book returnBook = mRemoteProxy.findBook("《雪中悍刀行》");
            Log.d(TAG, "onFindBookClick: " + returnBook);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}