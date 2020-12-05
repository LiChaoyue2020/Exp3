package com.example.exp3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final List<Integer> idList = new ArrayList<>();
    private final List<String> city_nameList = new ArrayList<>();
    private final List<String> city_codeList = new ArrayList<>();
    Button OK, MyConcern;
    EditText etSearch;
    ListView provinceList;
    String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OK = findViewById(R.id.ok);
        etSearch = findViewById(R.id.search);
        provinceList = findViewById(R.id.provincelist);
        OK.setOnClickListener(this);
        MyConcern = findViewById(R.id.concern_list);
        MyConcern.setOnClickListener(this);
        Button buttonBack = findViewById(R.id.back);

        responseData = getJson(this);
        readCityInfo(responseData, 0);
        ArrayAdapter<String> simpleAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, city_nameList);
        provinceList.setAdapter(simpleAdapter);
        provinceList = findViewById(R.id.provincelist);
        //配置ArrayList点击事件监听器
        provinceList.setOnItemClickListener((parent, view, position, id) -> {
            int tran_id = idList.get(position);
            String tran_city_code = city_codeList.get(position);
            //如果这个地点的cityCode为空，则表明这个地点还不能查询天气，需要往下搜索一层。
            //否则，用Intent打开Weather活动，并把cityCode传给它。
            if (tran_city_code.equals("")) {
                readCityInfo(responseData, tran_id);
                provinceList.setAdapter(simpleAdapter);
                buttonBack.setVisibility(View.VISIBLE);
                buttonBack.setOnClickListener(v -> {
                    readCityInfo(responseData, 0);
                    provinceList.setAdapter(simpleAdapter);
                    buttonBack.setVisibility(View.INVISIBLE);
                });
            } else {
                Intent intent = new Intent(MainActivity.this, com.example.exp3.Weather.class);
                intent.putExtra("searchCityCode", tran_city_code);
                startActivity(intent);
            }
        });
    }

    /*jsonData是从city.json转化而来的字符串，targetPid是目标pid，即目标父级行政区划的编号*/
    private void readCityInfo(String jsonData, int targetPid) {
        try {
            idList.clear();
            //pidList.clear();
            city_nameList.clear();
            city_codeList.clear();
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int pid = jsonObject.getInt("pid");
                String city_code = jsonObject.getString("city_code");
                String city_name = jsonObject.getString("city_name");
                if (pid == targetPid) {
                    idList.add(id);
                    //pidList.add(pid);
                    city_codeList.add(city_code);
                    city_nameList.add(city_name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*从res/raw中读取城市信息city.json，返回json的字符串*/
    public static String getJson(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.city);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                String searchCityCode = String.valueOf(etSearch.getText());
                if (searchCityCode.length() != 9) {
                    Toast.makeText(this, "数字长度不是九位！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, com.example.exp3.Weather.class);
                    intent.putExtra("searchCityCode", searchCityCode);
                    startActivity(intent);
                }
                break;
            case R.id.concern_list:
                Intent intent = new Intent(MainActivity.this, com.example.exp3.MyConcernList.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}