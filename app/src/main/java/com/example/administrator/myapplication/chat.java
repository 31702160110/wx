package com.example.administrator.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chat extends AppCompatActivity {
    private EditText edit;
    private ListView mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
    }

    //点击事件
    public void click(View v) {
        switch (v.getId()) {
            case R.id.send:
                chat();
                break;
            default:
                break;
        }
    }

    public void chat() {
        SharedPreferences SP = getSharedPreferences("data", MODE_PRIVATE);
        String user = SP.getString("user", null);
        String ed = findViewById(R.id.text).toString().trim();
        if (ed.equals("")) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        } else {
            chatpost(user, ed, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            chats chat = gson.fromJson(json, chats.class);
                            mlist=(ListView)findViewById(R.id.list);
                            ///建适配器

                        }
                    });

                }
            });
        }

    }

    //post请求
    public static void chatpost(String user, String chat, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //POST 表单创建
        RequestBody body = new FormBody.Builder()
                .add("user", user)
                .add("chat", chat)
                .build();
        //访问请求
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/login.php")
                //提交表单
                .post(body)
                .build();
        //网络异步回调
        client.newCall(request).enqueue(callback);
    }


    //User实体类
    public class chats {
        private String status;
        private String chat;
        private int time;
        private String name;

        chats(String status, String chat, int time, String name) {
            this.status = status;
            this.chat = chat;
            this.name = name;
            this.time = time;
        }
    }


}
