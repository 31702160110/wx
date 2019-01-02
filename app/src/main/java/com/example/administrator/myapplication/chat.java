package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chat extends AppCompatActivity {
    private EditText edit;
    private ListView mlist;
    private  String chatsss[];
    private  String namesss[];
    private  String timesss[];
    private TextView tl,tr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        edit= (EditText) findViewById(R.id.text);

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
        Log.i("eeeee", ed);
        if (ed.equals("")) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        } else {
            chatpost(user, ed, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<chats> chatss = chatformjson.chatsFromjson(json);
                            List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
                            for (chats info : chatss) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("name", info.getName());
                                map.put("chat", info.getChat());
                                map.put("time", info.getTime());
                                lis.add(map);
                            }
                            for (int i=0;i<lis.size();i++){
                                Log.i("listchat", lis.get(i).get("chat"));
//                                namesss[i]=lis.get(i).get("name");
//                                chatsss[i]=lis.get(i).get("chat");
//                                timesss[i]=lis.get(i).get("time");
                            }

//                                mlist=(ListView)findViewById(R.id.list);
//                                ///建适配器
//                                MyBaseAdapter mAdapter=new  MyBaseAdapter();
//                                mlist.setAdapter(mAdapter);
                        }
                    });

                }
            });
        }

    }

    //post请求
    public static void chatpost(String user, String chat, Callback callback) {
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


    //User实体类
    public class chats {
        private String chat;
        private String name;
        private String time;

        public String getChat() {
            return chat;
        }

        public void setChat(String chat) {
            this.chat = chat;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    //gson解析
    static class chatformjson {
        public static List<chats> chatsFromjson(String json) {
            Gson gson = new Gson();
            Type listype = new TypeToken<List<chats>>() {
            }.getType();
            List<chats> chatsinfo = gson.fromJson(json, listype);
            return chatsinfo;
        }
    }

//    class  MyBaseAdapter extends  BaseAdapter{
//     @Override
//     public int getCount() {
//         return  namesss.length;
//     }
//
//     @Override
//     public Object getItem(int i) {
//         return namesss[i];
//     }
//
//     @Override
//     public long getItemId(int i) {
//         return i;
//     }
//
//     @SuppressLint("WrongViewCast")
//     @Override
//     public View getView(int i, View view, ViewGroup viewGroup) {
//
//         final LinearLayout leftlinear=(LinearLayout)findViewById(R.id.left_Layout);
//         final LinearLayout rightlinear=(LinearLayout)findViewById(R.id.right_Layout);
//         tl=findViewById(R.id.left);tr=findViewById(R.id.right);
//         View view2=View.inflate(chat.this,R.layout.leftandright,null);
//         if(namesss[i]=="a"){
//
//                 leftlinear.setVisibility(View.VISIBLE);
//                rightlinear.setVisibility(View.GONE);
//                tl.setText(namesss[i]);
//                }else{
//                //如果是发出去的消息，显示右边布局的消息布局，将左边的消息布局隐藏
//                rightlinear.setVisibility(View.VISIBLE);
//                leftlinear.setVisibility(View.GONE);
//                tr.setText(namesss[i]);
//                }
//         return null;
//     }
// }

}
