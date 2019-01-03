package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.utils.Md5;
import com.example.administrator.myapplication.utils.entity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zLogin;

public class login extends AppCompatActivity {
    private EditText user;
    private EditText pwd;
    Md5 md5 = new Md5();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        qz();
    }

    //初始化控件
    public void init() {
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);
    }

    //点击事件
    public void click(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            //注册跳转click事件
            case R.id.register:
                Intent intent2 = new Intent();
                intent2.setAction("register");
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    //登录
    public void login() {
        final String uUser = user.getText().toString().trim();
        final String uPwd = pwd.getText().toString().trim();
        if (uUser.equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        } else if (uPwd.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            zLogin(uUser, uPwd, new Callback() {
                @Override
                //请求失败
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(login.this, "登入失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //请求成功
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            entity user = gson.fromJson(json, entity.class);
                            if (user.status.equals("登陆成功")) {
                                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("user", user.user);
                                edit.putString("name", uUser);
                                edit.putString("password", md5.KL(uPwd));
                                edit.commit();
                                Toast.makeText(login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent();
                                intent1.setAction("chat");
                                startActivity(intent1);
                            }
                            if (user.status.equals("登陆失败")) {
                                Toast.makeText(login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    //
    public void qz() {
        SharedPreferences qz = getSharedPreferences("data", MODE_PRIVATE);
        String uname = qz.getString("name", null);
        String upwd = md5.JM(qz.getString("password", null));
        user.setText(uname);
        pwd.setText(upwd);
    }

}


