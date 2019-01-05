package com.example.administrator.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

public class SelfFragment extends Fragment {
    protected TextView text_user,text_name;
    private String user;
    private String name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.self_fragment,container,false);
        text_user = view.findViewById(R.id.WeChat_User);
        text_name = view.findViewById(R.id.WeChat_name);
        text_user.setText(user);
        text_name.setText(name);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        user = ((MainActivity) context).Date_User();
        name = ((MainActivity) context).Date_Name();
    }
}
