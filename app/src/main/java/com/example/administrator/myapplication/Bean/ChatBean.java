package com.example.administrator.myapplication.Bean;

public class ChatBean {
    private String chat;
    private String time;
    private String name;

    public ChatBean(String chat, String time, String name) {
        this.chat = chat;
        this.time = time;
        this.name = name;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
