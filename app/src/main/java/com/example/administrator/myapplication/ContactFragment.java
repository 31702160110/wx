package com.example.administrator.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myapplication.utils.entity;
import com.example.administrator.myapplication.utils.znAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zUserlist;

public class ContactFragment extends Fragment {
    private ListView mListView;
    private TextView username,user;
    private FragmentManager mFragmentManager;
    private znAdapter<entity> mZnAdapter = null;
    private Map<String, String> map;
    private List<userjx> list;
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userlist, container, false);
        mListView = view.findViewById(R.id.userlist);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    String s = (String) msg.obj;
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<entity>>() {
                    }.getType();
                    List<entity> entities = gson.fromJson(s, listType);
                    list = new ArrayList<userjx>();
                    for (entity info : entities) {
                        list.add(new userjx(info.name,info.user));

                    }
                mListView.setAdapter(new userAdapter());
                }
            }
        };
        hander();
        username = view.findViewById(R.id.username);
        user = view.findViewById(R.id.user);


        return view;
    }

    public void hander() {

        new Thread() {
            @Override
            public void run() {
                zUserlist(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = new Message();
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String json = response.body().string();
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = json;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        }.start();
    }
    class userAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.usertext, viewGroup, false);
            }
            TextView name = view.findViewById(R.id.username);
            TextView user = view.findViewById(R.id.user);
            userjx j = list.get(i);
            name.setText("name:"+j.name);
            user.setText("user:"+j.user);
            return view;
        }
    }
    class userjx {
        public String name;
        public String user;
        public userjx(String name,String user) {
            this.name = name;
            this.user=user;
        }
    }
}

