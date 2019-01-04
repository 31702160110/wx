package com.example.administrator.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zChat;

public class chat extends AppCompatActivity {
    private EditText edit;
    private ListView mlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        edit= findViewById(R.id.text);


    }

    //点击事件
    public void click(View v) {
        switch (v.getId()) {
            case R.id.send:
                chat();
                break;
            default:
                break;
        }
    }

    public void chat() {
        SharedPreferences SP = getSharedPreferences("data", MODE_PRIVATE);
        String user = SP.getString("user", null);
        String ed =edit.getText().toString();
        Log.i("eeeee", user);
        if (ed.equals("")) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        } else {
            zChat(user, ed, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<entity> chatss = chatformjson.chatsFromjson(json);
                            List<jx> lis = new ArrayList<jx>();
                                Log.i("asddd", String.valueOf(chatss.size()));
                            for (entity info : chatss) {
                                lis.add(new jx(info.chat));
                            }


                                mlist=findViewById(R.id.list);
                                ///建适配器
                                MyBaseAdapter my=new MyBaseAdapter(lis);

                                mlist.setAdapter(my);
                        }
                    });

                }
            });
        }

    }

    //gson解析
    static class chatformjson {
        public static List<entity> chatsFromjson(String json) {
            Gson gson = new Gson();
            Type listype = new TypeToken<List<entity>>() {
            }.getType();
            List<entity> chatsinfo = gson.fromJson(json, listype);
            return chatsinfo;
        }
    }

    class  MyBaseAdapter extends BaseAdapter {
        private List<jx> mList;//数据源

        public MyBaseAdapter(List<jx> lis) {
            mList = lis;
            Log.i("wxss", String.valueOf(lis.size()));
        }

        @Override
     public int getCount() {

         return  mList.size();
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



         View view2=View.inflate(chat.this,R.layout.leftandright,null);

         TextView tl=view2.findViewById(R.id.left);
        jx j=mList.get(i);

         tl.setText(j.chat);


         return view2;
     }
 }
 class jx{
        public  String chat;
        public jx(String chat){
            this.chat=chat;
     }
 }

}
