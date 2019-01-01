package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class register extends AppCompatActivity {
    private EditText user;
    private EditText pwd;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //初始化控件
    public void init() {
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);
        name = findViewById(R.id.name);
    }

    //点击事件
    public void click(View v) {
        switch (v.getId()) {
            case R.id.adduser:
                register();
                break;
            default:
                break;
        }
    }

    //登录
    public void register() {
        final String uUser = user.getText().toString().trim();
        final String uPwd = pwd.getText().toString().trim();
        final String uName = name.getText().toString().trim();
        if (uName.equals("")) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (uUser.equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        } else if (uPwd.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            report(uName, uPwd, uUser, new Callback() {
                @Override
                //请求失败
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(register.this, "注册失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //请求成功
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            status status = gson.fromJson(json, status.class);
                            if (status.status.equals("注册成功")) {
                                Toast.makeText(register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(register.this,login.class);
                                startActivity(intent);
                            }
                            if (status.status.equals("用户名重复")) {
                                Toast.makeText(register.this, "账号重复", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    //status实体类
    public class status {
        private String status;

        status(String status) {
            this.status = status;
        }
    }

    //post请求
    public static void report(String uName, String uPwd,String uUser, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //POST 表单创建
        RequestBody body = new FormBody.Builder()
                .add("name", uName)
                .add("password", uPwd)
                .add("user", uUser)
                .build();
        //访问请求
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/register.php")
                //提交表单
                .post(body)
                .build();
        //网络异步回调
        client.newCall(request).enqueue(callback);
    }

}