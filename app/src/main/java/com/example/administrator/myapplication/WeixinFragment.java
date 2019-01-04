package com.example.administrator.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.utils.entity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.administrator.myapplication.utils.znHttp.zChat;

public class WeixinFragment extends Fragment implements View.OnClickListener {
    private Button send;
    private EditText et;
    private View view;
    private Handler handler;
    private ListView mlistv;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item, container, false);
        mlistv = view.findViewById(R.id.list);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    final List<jx> kk = (List<jx>) msg.obj;
                    ///建适配器
                    MyBaseAdapter my = new MyBaseAdapter(kk);
                    mlistv.setAdapter(my);
                    mlistv.post(new Runnable() {
                        @Override
                        public void run() {
                            mlistv.smoothScrollToPosition(kk.size());
                        }
                    });
                }
            }
        };
        send = view.findViewById(R.id.send);
        et = view.findViewById(R.id.text);
        send.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                chat();
                break;
        }
    }

    public void chat() {
        new Thread() {
            @Override
            public void run() {

                SharedPreferences SP = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                final String user = SP.getString("user", null);
                final String ed = et.getText().toString().trim();

                if (ed.equals(""))

                {
                    Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
                } else

                {

                    zChat(user, ed, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String json = response.body().string();
                            Gson gson = new Gson();
                            Type listype = new TypeToken<List<entity>>() {
                            }.getType();
                            List<entity> chatss = gson.fromJson(json, listype);
                            List<jx> lis = new ArrayList<>();
                            for (entity info : chatss) {
                                lis.add(new jx(info.chat));
                            }
                            Collections.reverse(lis);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = lis;
                            handler.sendMessage(msg);

                        }
                    });

                }

            }
        }.start();
    }

    class MyBaseAdapter extends BaseAdapter {
        private List<jx> mList;//数据源

        public MyBaseAdapter(List<jx> lis) {
            mList = lis;

        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.leftandright, viewGroup, false);
            }
            TextView tl = view.findViewById(R.id.left);
            jx j = mList.get(i);
            tl.setText(j.chat);
            return view;
        }
    }

    class jx {
        public String chat;

        public jx(String chat) {
            this.chat = chat;
        }
    }

}