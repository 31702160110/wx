package com.example.administrator.myapplication.utils;

public class entity {

    public String status;
    public String user;
    public String chat;
    public String name;
    public String time;
    public String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

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

    entity(String status, String user, String chat, String name, String time) {
        this.status = status;
        this.user = user;
        this.status = chat;
        this.user = name;
        this.status = time;
    }
}

