package com.example.administrator.myapplication.Bean;

public class Msg {

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    private String content;
    private String name;
    private String time;
    private int type;

    public Msg(String content, String name, String time, int type) {
        this.content = content;
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }
}
