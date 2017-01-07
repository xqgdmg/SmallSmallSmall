package com.example.administrator.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.myapplication.api.Rest;
import com.example.administrator.myapplication.bean.Configures;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);

        performRetrofit();
    }

    private void performRetrofit() {
        // 1. 设置 okhttp
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS);//设置写入超时时间
        OkHttpClient okHttpClient = builder.build();

        // 2. 创建 Retrofit 对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com")// 注意这个接口不要写成 https
                .addConverterFactory(ScalarsConverterFactory.create()) // String 解析
//                .addConverterFactory(GsonConverterFactory.create()) // gson 解析
                .client(okHttpClient)      //自己定制的okhttpClient
                .build();

        // 3. 转化成接口
        Rest service = retrofit.create(Rest.class);
//        Call<String> call = service.getBody();

        // 4. 调用接口中的方法
        Call<String> call2 = service.GetbookOffer();

        // 5. 执行异步请求
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("haha", "Happy== onResponse + " + response.body().toString());
                text.setText(response.body().toString());//运行在UI线程中的
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("haha", "Happy== onFailure");
            }
        });


    }
}
