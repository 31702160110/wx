package com.example.administrator.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Bean.entity;
import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.one_to_one;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zUserlist;

public class ContactFragment extends Fragment implements View.OnClickListener {
    private ListView mListView;
    private List<userjx> list = new ArrayList<>();
    private Handler mHandler;
    private String jsons;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        mListView = view.findViewById(R.id.userlist);
        editText = view.findViewById(R.id.ss);
        //点击事件
        view.findViewById(R.id.sss).setOnClickListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(getActivity(),"获取联系人失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        userlist();
                        break;
                    default:
                        break;
                }
            }
        };
        itemliste();
        handler();
        return view;
    }

    public void handler() {
        zUserlist(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsons = response.body().string();
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        String ss = editText.getText().toString();
        if (!ss.equals("")) {
            list.clear();
            Gson gson = new Gson();
            final Type listType = new TypeToken<List<entity>>() {
            }.getType();
            String s = jsons;
            List<entity> entities = gson.fromJson(s, listType);
            for (entity info : entities) {
                String array = info.name;
                if (array.indexOf(ss) != -1) {
                    list.add(new userjx(info.name, info.user));
                } else {
                    continue;
                }
            }
            mListView.setAdapter(new userAdapter());
            editText.setText("");
            editText.setHint("点击搜索显示所有联系人");
        } else {
            list.clear();
            userlist();
            editText.setHint("搜索联系人......");
        }
    }

    public void userlist() {
        Gson gson = new Gson();
        final Type listType = new TypeToken<List<entity>>() {
        }.getType();
        List<entity> entities = gson.fromJson(jsons, listType);
        for (entity info : entities) {
            list.add(new userjx(info.name, info.user));
        }
        mListView.setAdapter(new userAdapter());
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
            ViewHolder holder;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.contact_item, viewGroup, false);
                holder = new ViewHolder();
                holder.mTVName = view.findViewById(R.id.username);
                holder.mTVUser = view.findViewById(R.id.user);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            userjx j = list.get(i);
            holder.mTVName.setText("name:" + j.name);
            holder.mTVUser.setText("user:" + j.user);
            return view;
        }

        class ViewHolder {
            TextView mTVName;
            TextView mTVUser;
        }
    }

    class userjx {
        public String name;
        public String user;

        public userjx(String name, String user) {
            this.name = name;
            this.user = user;
        }
    }
    //listitem的监听事件
    public void itemliste(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ousername = list.get(i).name;
//                Toast.makeText(getActivity(), list.get(i).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),one_to_one.class);
                intent.putExtra("name",ousername);
                startActivity(intent);
            }
        });
    }

}

