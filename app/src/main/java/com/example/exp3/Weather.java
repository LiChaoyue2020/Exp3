package com.example.exp3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Weather extends AppCompatActivity {
    /*
     * 目前天气能查询7天（昨天~未来五天）
     * 这些List<TextView>用于显示多日数据
     */
    static final int COUNT = 7;
    TextView tvCityName, tvWeatherType, tvCurrentTemperature, tvTemperatureRange, tvDetail;
    List<TextView> tvDayNames = new ArrayList<>(COUNT);
    List<TextView> tvWeaTypes = new ArrayList<>(COUNT);
    List<TextView> tvTemRanges = new ArrayList<>(COUNT);
    String searchCityCode;
    Button concern, refresh;

    //这些是今日天气的详细数据
    private String cityName, updateTime, humidity, quality, temperature,
            suggestion, sunrise0, sunset0, aqi0, fx0, fl0, type0, notice0;
    //这些保存多日数据
    private final List<String> dayNames = new ArrayList<>();
    private final List<String> weaTypes = new ArrayList<>();
    private final List<String> temRanges = new ArrayList<>();
    MyDBHelper dbHelper;
    int dbCityID;
    String dbData;
    int sign = 1;
    boolean isConcerned = false;

    /*绑定控件，从Intent中获取要搜索的城市号，创建数据库，
     * 查询关注的城市数据表，根据已关注或未关注，设置不同的逻辑流程，
     * 查询数据库，如果天气信息已存在，则直接读取，
     * 如果天气信息不存在，则联网查询，并存入数据库。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tvCityName = findViewById(R.id.city_name);
        tvWeatherType = findViewById(R.id.weather_type);
        tvCurrentTemperature = findViewById(R.id.current_temperature);
        tvTemperatureRange = findViewById(R.id.temperature_range);
        tvDetail = findViewById(R.id.detail);
        concern = findViewById(R.id.concern1);
        refresh = findViewById(R.id.refresh);

        for (int i = 0; i < COUNT; i++) {
            int dayNameId = getResources().getIdentifier("day" + i + "_name", "id", getPackageName());
            tvDayNames.add(findViewById(dayNameId));
            int weaTypeId = getResources().getIdentifier("day" + i + "_wea_type", "id", getPackageName());
            tvWeaTypes.add(findViewById(weaTypeId));
            int temRanId = getResources().getIdentifier("day" + i + "_tem_ran", "id", getPackageName());
            tvTemRanges.add(findViewById(temRanId));
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        searchCityCode = extras.getString("searchCityCode");


        dbHelper = new MyDBHelper(this, "Weather.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();     //同上，获得可写文件
        Cursor cursor0 = db.query("Concern", new String[]{"city_code"}, "city_code=?", new String[]{searchCityCode}, null, null, null);
        if (cursor0.getCount() != 0) {
            isConcerned = true;
            concern.setText("取消关注");
        }
        cursor0.close();
        refresh.setOnClickListener(v -> {
            sign = 3;
            sendRequestWithOkHttp();
            Log.d("MainActivity", "数据库刷新成功");
        });
        concern.setOnClickListener(v -> {
            if (!isConcerned) {
                ContentValues values = new ContentValues();
                values.put("city_code", searchCityCode);
                values.put("city_name", cityName);
                db.insert("Concern", null, values);
                Toast.makeText(Weather.this, "关注成功！", Toast.LENGTH_SHORT).show();
                concern.setText("取消关注");
            } else {
                db.delete("Concern", "city_code=?", new String[]{searchCityCode});
                Toast.makeText(Weather.this, "取消关注成功！", Toast.LENGTH_SHORT).show();
                concern.setText("关注");
            }
            isConcerned = !isConcerned;
        });

        Cursor cursor1 = db.query("Weather", new String[]{"id", "data"}, "id=?", new String[]{searchCityCode + ""}, null, null, null);
        if (cursor1.moveToNext()) {       //逐行查找，得到匹配信息
            do {
                dbCityID = cursor1.getInt(cursor1.getColumnIndex("id"));
                dbData = cursor1.getString(cursor1.getColumnIndex("data"));
            } while (cursor1.moveToNext());
        }
        int cityCode = Integer.parseInt(searchCityCode);

        if (dbCityID == cityCode) {
            sign = 1;
            showResponse(dbData);
        } else {
            sign = 0;
            sendRequestWithOkHttp();
        }
        cursor1.close();
    }

    /*通过OKHttp发送请求，在日志区打印数据，展示数据。*/
    private void sendRequestWithOkHttp() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://t.weather.itboy.net/api/weather/city/" + searchCityCode)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = Objects.requireNonNull(response.body()).string();
                Log.d("data is", responseData);
                showResponse(responseData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /*从json字符串中获取天气数据，并写入数据库*/
    private void readWeatherInfoFromJSON(String jsonData) {
        dayNames.clear();
        weaTypes.clear();
        temRanges.clear();
        Log.d("jsonData", jsonData);
        if (jsonData.length() < 100) {
            Log.d("M", "城市ID不存在");
            Toast.makeText(this, "城市ID不存在，请重新输入", Toast.LENGTH_LONG).show();
            System.exit(0);
        } else {
            App app = JSON.parseObject(jsonData, App.class);
            App.CityInfo cityInfo = app.getCityInfo();
            cityName = cityInfo.getCity();
            updateTime = cityInfo.getUpdateTime();

            App.data data = app.getData();
            App.forecast today = data.getForecast().get(0);
            type0 = today.getType();
            temperature = data.getWendu();

            humidity = data.getShidu();
            aqi0 = today.getAqi();
            quality = data.getQuality();

            sunrise0 = today.getSunrise();
            sunset0 = today.getSunset();

            fl0 = today.getFl();
            fx0 = today.getFx();

            suggestion = data.getGanmao();
            notice0 = today.getNotice();

            App.data.yesterday yesterday = data.getYesterday();
            List<App.forecast> forecasts = data.getForecast();

            dayNames.add("昨天");
            weaTypes.add(yesterday.getType());
            temRanges.add(yesterday.getHigh().substring(2) + " ~" + yesterday.getLow().substring(2));
            //今天和未来四天的天气情况
            for (int i = 1; i < COUNT; i++) {
                dayNames.add(forecasts.get(i - 1).getWeek());
                weaTypes.add(forecasts.get(i - 1).getType());
                temRanges.add(forecasts.get(i - 1).getHigh().substring(2) + " ~" + forecasts.get(i - 1).getLow().substring(2));
            }
            dayNames.set(1, "今天");


            if (sign == 0) {
                dbHelper = new MyDBHelper(this, "Weather.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("id", searchCityCode);
                values.put("data", jsonData);
                db.insert("Weather", null, values);
                Log.d("MainActivity", "数据库写入成功");
            } else if (sign == 1) {
                Log.d("数据库写入失败：", "数据已存在");
            } else {
                MyDBHelper dbHelper = new MyDBHelper(this, "Weather.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("id", searchCityCode);
                values.put("data", jsonData);
                db.update("Weather", values, "id=?", new String[]{searchCityCode + ""});
                Log.d("MainActivity", "数据库更新成功");

            }

        }
    }

    /*展示结果*/
    private void showResponse(final String response) {
        runOnUiThread(() -> {
            readWeatherInfoFromJSON(response);
            String todayDetail = "湿度：" + humidity + "      空气指数：" + aqi0 + "      空气质量：" + quality + "\n" +
                    "日出时间：" + sunrise0 + "          日落时间：" + sunset0 + "\n" +
                    "风力：" + fl0 + "                       风向：" + fx0 + "\n" +
                    "活动建议：" + suggestion + "\n" + "提示：" + notice0 + "\n" + "更新时间：" + updateTime;
            tvCityName.setText(cityName);
            tvWeatherType.setText(type0);
            tvCurrentTemperature.setText(temperature);
            tvTemperatureRange.setText(temRanges.get(1));
            tvDetail.setText(todayDetail);
            for (int i = 0; i < COUNT; i++) {
                tvDayNames.get(i).setText(dayNames.get(i));
                tvDayNames.get(i).setTextSize(20);
                tvWeaTypes.get(i).setText(weaTypes.get(i));
                tvWeaTypes.get(i).setTextSize(20);
                tvTemRanges.get(i).setText(temRanges.get(i));
                tvTemRanges.get(i).setTextSize(20);
            }
        });
    }


}
