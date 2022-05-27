package com.afauria.sample.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.afauria.sample.ipc.Book;
import com.afauria.sample.ipc.IBookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Afauria on 5/27/22.
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";

    private List<Book> mBooks;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new IBookManager.Stub() {
            @Override
            public void initBooks() throws RemoteException {
                try {
                    Log.d(TAG, "initBooks start: " + Thread.currentThread());
                    //模拟耗时调用
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBooks = new ArrayList<>();
                mBooks.add(new Book("《雪中悍刀行》", 10));
                mBooks.add(new Book("《大奉打更人》", 20));
                Log.d(TAG, "initBooks end: " + Thread.currentThread());
            }

            @Override
            public void initBooksOneWay() throws RemoteException {
                try {
                    Log.d(TAG, "initBooksOneWay start: " + Thread.currentThread());
                    //模拟耗时调用
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBooks = new ArrayList<>();
                mBooks.add(new Book("《雪中悍刀行》", 10));
                mBooks.add(new Book("《大奉打更人》", 20));
                Log.d(TAG, "initBooksOneWay end: " + Thread.currentThread());
            }

            @Override
            public Book findBook(String name) throws RemoteException {
                Log.d(TAG, "findBook: " + name);
                for (Book book : mBooks) {
                    if (name.equals(book.getName())) {
                        return book;
                    }
                }
                return null;
            }

            @Override
            public void addBookIn(Book book) throws RemoteException {
                Log.d(TAG, "addBookIn: " + book);
                book.setPrice(book.getPrice() + 5);
                mBooks.add(book);
            }

            @Override
            public void addBookOut(Book book) throws RemoteException {
                Log.d(TAG, "addBookOut: " + book);
                book.setPrice(book.getPrice() + 5);
                mBooks.add(book);
            }

            @Override
            public void addBookInout(Book book) throws RemoteException {
                Log.d(TAG, "addBookInout: " + book);
                book.setPrice(book.getPrice() + 5);
                mBooks.add(book);
            }

            @Override
            public List<Book> listBooks() throws RemoteException {
                Log.d(TAG, "listBooks: " + mBooks.size());
                return mBooks;
            }
        };
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
