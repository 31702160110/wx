package com.example.administrator.myapplication.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.Adapter.MsgAdapter;
import com.example.administrator.myapplication.Bean.ChatBean;
import com.example.administrator.myapplication.Bean.Msg;
import com.example.administrator.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.administrator.myapplication.utils.znHttp.zChat;

public class WeixinFragment extends Fragment implements View.OnClickListener {
    private List<Msg> msgList = new ArrayList<Msg>();
    private View view;
    private Button btn_send;
    private EditText et_input;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private String user, name;//登录的user和用户名
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weixin_fragment, container, false);
        init(view);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    msgRecyclerView.setAdapter(adapter);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    et_input.setText(""); // 清空输入框中的内容
                } else if (msg.what == 2) {
                    Toast.makeText(getActivity(), "糟糕!网络好像出问题了", Toast.LENGTH_SHORT).show();
                }
            }
        };

        return view;
    }

    //初始化
    private void init(View view) {
        btn_send = view.findViewById(R.id.send);
        btn_send.setOnClickListener(this);
        et_input = view.findViewById(R.id.input_text);
        msgRecyclerView = view.findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        SharedPreferences qz = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        user = qz.getString("user", "");
        name = qz.getString("name", "");
    }
    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                chatPOST();
                break;
        }
    }

    //点击send触发的方法
    private void chatPOST() {
        final String content = et_input.getText().toString();
        if (content.equals("")) {
            Toast.makeText(getActivity(), "发送内容不能为空", Toast.LENGTH_SHORT).show();
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
                    if (!tmp_name.equals(name)) {
                        msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_RECEIVED));
                    } else {
                        msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_SENT));
                    }
                }
                //通知Handler执行刷新操作
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }
}