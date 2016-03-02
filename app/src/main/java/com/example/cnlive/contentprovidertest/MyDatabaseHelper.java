package com.example.cnlive.contentprovidertest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by CNLive on 2016/2/22.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mcontext;
    private static final String CREATE_BOOK="create table Book("+
            "author text,"+
            "pages integer,"+
            "price real,"+
            "name real)";//创建一个Book表
    private static final String CREATE_CATEGORY="create table Category("+
            "id integer primary key autoincrement,"+
            "category_name text,"+"category_code integer)";//创建另一个category表

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //重写四个参数的构造函数，第一个参数是context，用于对数据库进行操作。
        // 第二个参数是数据库的名字，我们预先定义的是BookStore。
        // 第三个参数是查询数据的时候返回一个自定义的Cursor，一般都是传入null值。
        // 第四个参数是当前数据库的版本号，用于对数据库进行升级。
        // TODO Auto-generated constructor stub
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);//执行建表语句。
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        // TODO Auto-generated method stub

    }
}


