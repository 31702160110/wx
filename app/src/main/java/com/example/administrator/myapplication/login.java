package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login extends AppCompatActivity {
    private EditText user;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
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
        final String uName = user.getText().toString().trim();
        final String uPwd = pwd.getText().toString().trim();
        if (uName.equals("")) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (uPwd.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            loginpost(uName, uPwd, new Callback() {
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
                            User user = gson.fromJson(json, User.class);
                            if (user.status.equals("登陆成功")) {
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

    //User实体类
    public class User {
        private String status;
        private String user;
        private String name;

        User(String status, String user, String name) {
            this.status = status;
            this.user = user;
            this.name = name;
        }
    }

    //post请求
    public static void loginpost(String uName, String uPwd, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //POST 表单创建
        RequestBody body = new FormBody.Builder()
                .add("user", uName)
                .add("password", uPwd)
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

    // 进行md5的加密运算
    public static String md5(String password) {
        // MessageDigest专门用于加密的类
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(password.getBytes()); // 得到加密后的字符组数

            StringBuffer sb = new StringBuffer();

            for (byte b : result) {
                int num = b & 0xff; // 这里的是为了将原本是byte型的数向上提升为int型，从而使得原本的负数转为了正数
                String hex = Integer.toHexString(num); //这里将int型的数直接转换成16进制表示
                //16进制可能是为1的长度，这种情况下，需要在前面补0，
                if (hex.length() == 1) {
                    sb.append(0);
                }
                sb.append(hex);
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}


