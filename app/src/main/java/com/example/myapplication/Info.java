package com.example.myapplication;

public class Info {
    int id;
    String username;
    String password;
    String url;
    public Info() {
    }

    public Info(int id, String username, String password,String url) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.url=url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url){this.url=url;}
}
