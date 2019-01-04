package com.example.administrator.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
    private EditText ed_user;
    private EditText ed_pwd;
    private String uUser;
    private String uPwd;
    private String jiauser;
    Md5 md5 = new Md5();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        getInfo();//获取账号信息 填到对应的EditText

    }

    //初始化控件
    public void init() {
        ed_user = findViewById(R.id.user);
        ed_pwd = findViewById(R.id.pwd);
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
        uUser = ed_user.getText().toString().trim();
        uPwd = ed_pwd.getText().toString().trim();
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
                            //                            Log.d("test", "run: json:"+json);
                            Gson gson = new Gson();
                            entity user = gson.fromJson(json, entity.class);
                            if (user.status.equals("登陆成功")) {
                                jiauser = user.user;
                                saveInfo();//保存账号信息
                                Toast.makeText(login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login.this, MainActivity.class);
                                intent.putExtra("name", user.name);
                                intent.putExtra("user", user.user);
                                startActivity(intent);
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

    //保存账号
    private void saveInfo() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username", uUser);
        edit.putString("password", md5.KL(uPwd));
        edit.putString("user", jiauser);
        edit.commit();
    }

    //获取账号
    public void getInfo() {
        SharedPreferences qz = getSharedPreferences("data", MODE_PRIVATE);
        String uname = qz.getString("username", "");
        String upwd = md5.JM(qz.getString("password", ""));
        ed_user.setText(uname);
        ed_pwd.setText(upwd);
    }

    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }

    }
}