package com.example.administrator.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.MsgAdapter;
import com.example.administrator.myapplication.Bean.ChatBean;
import com.example.administrator.myapplication.Bean.Msg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zChat;

public class one_to_one extends AppCompatActivity implements View.OnClickListener {
    private List<Msg> msgList = new ArrayList<Msg>();
    private Button btn_send;
    private EditText et_input;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private String user, name;//登录的user和用户名
    private Handler handler;
    private Boolean flg = true;
    private String names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onebyone);
        init();
        setLablr();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(one_to_one.this, "糟糕!网络好像出问题了", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        msgRecyclerView.setAdapter(adapter);
                        break;
                    case 2:
                        msgRecyclerView.setAdapter(adapter);
                       break;
                    default:
                        break;
                }
            }
        };
        new run().start();
        Log.v("pp1", String.valueOf(flg));
    }

    //被覆盖调用
    @Override
    protected void onPause() {
        super.onPause();
        flg = false;
        Log.v("pp1", String.valueOf(flg));
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                cah();
                et_input.setText(""); // 清空输入框中的内容
                break;
        }
    }

    public void cah() {
        final String content = et_input.getText().toString();
        if (content.equals("")) {
            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //先清空MSG
        msgList.clear();
        zChat(user, content, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JSON = response.body().string();
                //gson解析
                Gson gson = new Gson();
                List<ChatBean> list = gson.fromJson(JSON, new TypeToken<List<ChatBean>>() {
                }.getType());
                //倒序
                Collections.reverse(list);

                for (ChatBean i : list) {
                    //Log.d("遍历", "onResponse: "+i.getName()+"---"+i.getTime()+"---"+i.getChat());

                    //根据返回的name 判断是否为自己账号所发
                    String tmp_name = i.getName();
                    if (!i.getChat().equals("")) {
                        if (tmp_name.equals(name)) {
                            msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_SENT));
                        }else if (tmp_name.equals(names)) {
                            msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_RECEIVED));
                        }
                    }
                }
                //通知Handler执行刷新操作
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }

    public class run extends Thread {
        public void run() {
            while (flg) {
                Log.v("ppn", "xsruning");
                try {
                    chats();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //网络请求
        public void chats() {
            msgList.clear();
            zChat(user, "", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String JSON = response.body().string();
                    //gson解析
                    Gson gson = new Gson();
                    List<ChatBean> list = gson.fromJson(JSON, new TypeToken<List<ChatBean>>() {
                    }.getType());
                    //倒序
                    Collections.reverse(list);

                    for (ChatBean i : list) {
                        //Log.d("遍历", "onResponse: "+i.getName()+"---"+i.getTime()+"---"+i.getChat());

                        //根据返回的name 判断是否为自己账号所发
                        String tmp_name = i.getName();
                        if (!i.getChat().equals("")) {
                            if (tmp_name.equals(names)) {
                                msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_RECEIVED));
                            }else if (tmp_name.equals(name)) {
                                msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_SENT));
                            }
                        }
                        //通知Handler执行刷新操作
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                }
            });
        }
    }

    //设置标题
    public void setLablr() {
        names = getIntent().getStringExtra("name");
        setTitle("当前聊天对象为 : " + names);
    }

    //初始化
    private void init() {
        btn_send = findViewById(R.id.send);
        btn_send.setOnClickListener(this);
        et_input = findViewById(R.id.input_text);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        SharedPreferences qz = getSharedPreferences("data", MODE_PRIVATE);
        user = qz.getString("user", "");
        name = qz.getString("name", "");
    }
}
