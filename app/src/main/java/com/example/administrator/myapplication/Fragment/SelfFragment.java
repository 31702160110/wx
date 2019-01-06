package com.example.administrator.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.login;

import static android.content.Context.MODE_PRIVATE;

public class SelfFragment extends Fragment implements View.OnClickListener {

    protected TextView text_user, text_name;
    private String user;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.self_fragment, container, false);
        text_user = view.findViewById(R.id.WeChat_User);
        text_name = view.findViewById(R.id.WeChat_name);
        view.findViewById(R.id.loginout).setOnClickListener(this);
        text_user.setText(user);
        text_name.setText(name);
        if (text_user.getText().equals("")) {
            getInfo();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        user = ((MainActivity) context).Date_User();
        name = ((MainActivity) context).Date_Name();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginout:
                cleanInfo();
                Intent intent = new Intent(getActivity(), login.class);
                //以下标志将会清除之前所有已经打开的activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //清除账号
    private void cleanInfo() {
        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("islogin", false);
        edit.commit();
    }

    //获取账号
    public void getInfo() {
        SharedPreferences qz = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String uuser = qz.getString("user", "");
        String uname = qz.getString("name", "");
        text_user.setText(uuser);
        text_name.setText(uname);
    }

}
