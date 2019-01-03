package com.example.administrator.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.myapplication.utils.znHttp.zChat;

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
                            List<Map<String, String>> lis = new ArrayList<Map<String, String>>();
                            for (entity info : chatss) {
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
