package com.example.administrator.myapplication.utils;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public abstract class znHttp {
    public static void zLogin(String uName, String uPwd, Callback callback) {
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

    public static void zRegister(String uName, String uPwd, String uUser, Callback callback) {
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

    public static void zChat(String user, String chat, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //POST 表单创建
        RequestBody body = new FormBody.Builder()
                .add("user", user)
                .add("chat", chat)
                .build();
        //访问请求
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/chat1.php")
                //提交表单
                .post(body)
                .build();
        //网络异步回调
        client.newCall(request).enqueue(callback);
    }

    public static void zUserlist(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/member.php")
                //提交表单
                .build();
        //网络异步回调
        client.newCall(request).enqueue(callback);
    }

}
