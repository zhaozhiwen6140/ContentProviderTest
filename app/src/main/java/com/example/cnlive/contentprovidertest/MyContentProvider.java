package com.example.cnlive.contentprovidertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by CNLive on 2016/2/22.
 */
public class MyContentProvider extends ContentProvider {
//在这里实现四个常量，作为UriMatcher的match返回的数据，用于在6个方法中进行匹配使用，如果匹配成功，才能进行数据操作。
    public static final int BOOK_DIR=0;
    public  static final int BOOK_ITEM=1;
    public static final int CATEGORY_DIR=2;
    public  static final int CATEGORY_ITEM=3;
    public static final String AUTHORITY="com.example.cnlive.contentprovidertest.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static{//在这里将想要通过ContentProvider接口分享出去的数据进行初始化。
        //即提供要分享的数据。
         uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
        //UriMatcher的addURL方法需要三个参数，分别是数据所在的权限，路径，以及一个自定义代码。
        //路径/#表示访问该表的某一条数据。如果只有路径，则表示访问该表的所有数据。
    }

    @Override
    public boolean onCreate() {
        //这个方法实现了继承SQLiteOpenHelper的MyDatabaseHelper的实例。
        dbHelper=new MyDatabaseHelper(getContext(),"BookStore.db",null,1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //这个方法返回一个Cursor的对象。
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch(uriMatcher.match(uri)){//在这里进行匹配，如果此时外部想要访问的数据能够与我们所提供的数据匹配上的话，就可以进行外部操作。
            case BOOK_DIR://若访问所有数据，则直接查询数据库，将传入的参数进行赋值即可。
                cursor=db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM://若访问表中的单条数据，那么传入的uri中就存在一个id，然后将id分离出来。对这一条数据进行操作。以下都相同。
                String bookId=uri.getPathSegments().get(1);
                cursor=db.query("book",projection,"id=?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor=db.query("category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                cursor=db.query("category",projection,"id=?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            //这个方法返回的是一个字符串，字符串有两部分，一部分是vnd.android.cursor.(dir/item),
            // 如果是访问整个表，就用dir,如果是访问表中的一条数据，就用item。第二部分是vnd.权限.路径。
            // 把两部分进行拼接然后返回就行了。
            case BOOK_DIR:
                return "vnd.android.curdor.dir/vnd.com.example.cnlive.contentprovidertest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.curdor.item/vnd.com.example.cnlive.contentprovidertest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.cnlive.contentprovidertest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.cnlive.contentprovidertest.provider.category";
        }
        return  null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //这个方法返回插入新数据后，新数据的uri对象，需要先获取uri内容，再解析成uri对象。
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri uriNew = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId=db.insert("book",null,values);
                uriNew=Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId=db.insert("category",null,values);
                uriNew=Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
        }
        return uriNew;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows=db.delete("book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                updateRows=db.delete("Book","id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows=db.delete("category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                updateRows=db.delete("category","id=?",new String[]{categoryId});
                break;
        }
        return updateRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //这个方法是返回更新的数据的ID号，即受影响的行数
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRow=0;
         switch(uriMatcher.match(uri)){
             case BOOK_DIR:
                 updateRow=db.update("book",values,selection,selectionArgs);
             break;
             case BOOK_ITEM:
                 String bookId=uri.getPathSegments().get(1);
                 updateRow=db.update("book",values,"id=?",new String[]{bookId});
                 break;
             case CATEGORY_DIR:
                 updateRow=db.update("category",values,selection,selectionArgs);
                 break;
             case CATEGORY_ITEM:
                 String categoryId=uri.getPathSegments().get(1);
                 updateRow=db.update("book",values,"id=?",new String[]{categoryId});
                 break;

         }
        return updateRow;
    }

}
