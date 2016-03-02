package com.example.cnlive.contentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button add_data;
    private Button update_data;
    private Button delete_data;
    private Button query_data;
    private TextView textView;
    private MyDatabaseHelper dbHelper;
    private static final String Author = "content://com.example.cnlive.contentprovidertest.provider/book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        add_data = (Button) findViewById(R.id.add_data);
        update_data = (Button) findViewById(R.id.update_data);
        delete_data = (Button) findViewById(R.id.delete_data);
        query_data = (Button) findViewById(R.id.query_data);
        textView = (TextView) findViewById(R.id.text);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Author);
                ContentValues values = new ContentValues();
                values.put("author", "Magin");
                values.put("pages", 345);
                values.put("price", 22.5);
                values.put("name", "A clash of King");
                getContentResolver().insert(uri, values);
                //通过getContentResolve（）获得ContentResolve的实例，
                // 调用insert（）访问ContentProvider提供的数据。
            }
        });

        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Author);
                getContentResolver().delete(uri, "author=?", new String[]{"Magin"});//通过getContentResolve（）获得ContentResolve的实例，
                // delete（）访问ContentProvider提供的数据。
            }
        });

        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Author);
                ContentValues values = new ContentValues();
                values.put("price", 27.5);
                getContentResolver().update(uri, values, "author=?", new String[]{"Magin"});//通过getContentResolve（）获得ContentResolve的实例，
                // 调用update（）访问ContentProvider提供的数据。
            }
        });

        query_data.setOnClickListener(new View.OnClickListener() {
            //通过getContentResolve（）获得ContentResolve的实例，
            // 调用query（）访问ContentProvider提供的数据。

            @Override
            public void onClick(View v) {
                Cursor cursor = null;
                Uri uri = Uri.parse(Author);
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        int page = cursor.getInt(cursor.getColumnIndex("pages"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        String text = name + price + page + author;
                        textView.setText(text);
                    }
                }
                cursor.close();
            }

        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
