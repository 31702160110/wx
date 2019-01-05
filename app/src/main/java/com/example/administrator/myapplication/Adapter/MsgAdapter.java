package com.example.administrator.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.Bean.Msg;
import com.example.administrator.myapplication.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout leftLayout;

        RelativeLayout rightLayout;
        TextView time;
        TextView leftMsg,leftName;

        TextView rightMsg,rightName;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.left_layout);
            rightLayout = (RelativeLayout) view.findViewById(R.id.right_layout);
            time  = (TextView) view.findViewById(R.id.tv_send_time);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            leftName = (TextView) view.findViewById(R.id.left_name);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            rightName = (TextView) view.findViewById(R.id.right_name);
        }
    }

    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        holder.time.setText(msg.getTime());
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.leftName.setText(msg.getName());
        } else if(msg.getType() == Msg.TYPE_SENT) {
            // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            holder.rightName.setText(msg.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

}
