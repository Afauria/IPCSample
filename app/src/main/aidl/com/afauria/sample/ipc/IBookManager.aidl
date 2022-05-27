// IBookManager.aidl
package com.afauria.sample.ipc;

//对象传递只能使用Parcelable类型
//需要使用parcelable关键字声明，否则编译会报错
parcelable Book;

//可以定义Book.aidl，再import
//import com.afauria.sample.ipc.Book;

interface IBookManager {
    void initBooks();

    //oneway不能有返回值，否则编译会报错：oneway method 'initBooksOneWay' cannot return a value
    oneway void initBooksOneWay();

    //oneway方法不能有out或者inout参数。编译会报错：oneway method 'addBook' cannot have out parameters
    //oneway void addBook(out Book book);

    List<Book> listBooks();

    //参数为非String和基本数据类型时，需要添加数据定向Tag：in、out、inout，否则编译会报错：can be an out type, so you must declare it as in, out, or inout.
    void addBookIn(in Book book);

    void addBookOut(out Book book);

    void addBookInout(inout Book book);

    //参数为String或者基本数据类型时，数据定向Tag只能为in，可以省略。使用out编译会报错：'out String name' can only be an in parameter.
    Book findBook(String name);
}