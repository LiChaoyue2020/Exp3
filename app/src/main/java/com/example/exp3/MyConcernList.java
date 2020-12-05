package com.example.exp3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MyConcernList extends AppCompatActivity {

    ArrayAdapter<String> simpleAdapter;
    ListView MyConcernList;
    Button btnBack;
    private final List<String> city_nameList = new ArrayList<>();
    private final List<String> city_codeList = new ArrayList<>();

    private void InitConcern() {       //进行数据填装
        MyDBHelper dbHelper = new MyDBHelper(this, "Weather.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Concern", null);
        while (cursor.moveToNext()) {
            String city_code = cursor.getString(cursor.getColumnIndex("city_code"));
            String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            city_codeList.add(city_code);
            city_nameList.add(city_name);
        }
    }

    public void RefreshList() {
        city_nameList.clear();
        city_codeList.clear();
//        city_nameList.removeAll(city_nameList);
//        city_codeList.removeAll(city_codeList);
        simpleAdapter.notifyDataSetChanged();
        MyDBHelper dbHelper = new MyDBHelper(this, "Weather.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Concern", null);
        while (cursor.moveToNext()) {
            String city_code = cursor.getString(cursor.getColumnIndex("city_code"));
            String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            city_codeList.add(city_code);
            city_nameList.add(city_name);
        }
        cursor.close();
    }


    @Override
    protected void onStart() {
        super.onStart();
        RefreshList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_concern_list);
        MyConcernList = findViewById(R.id.MyConcernList);

        InitConcern();

        simpleAdapter = new ArrayAdapter<>(MyConcernList.this, android.R.layout.simple_list_item_1, city_nameList);

        MyConcernList.setAdapter(simpleAdapter);
        //配置ArrayList点击按钮
        MyConcernList.setOnItemClickListener((parent, view, position, id) -> {
            String tran_city_code = city_codeList.get(position);
            Intent intent = new Intent(MyConcernList.this, Weather.class);
            intent.putExtra("searchCityCode", tran_city_code);
            startActivity(intent);
        });
        btnBack = findViewById(R.id.backToMain);
        btnBack.setOnClickListener(v -> finish());
    }
}
