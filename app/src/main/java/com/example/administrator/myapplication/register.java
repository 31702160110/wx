package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.utils.entity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zRegister;

public class register extends AppCompatActivity implements View.OnClickListener {
    private EditText user;
    private EditText pwd;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        findViewById(R.id.adduser).setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adduser:
                register();
                break;
            default:
                break;
        }
    }

    //注册
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
            zRegister(uName, uPwd, uUser, new Callback() {
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
                            entity status = gson.fromJson(json, entity.class);
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

}