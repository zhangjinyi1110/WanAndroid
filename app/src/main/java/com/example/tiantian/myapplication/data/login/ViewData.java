package com.example.tiantian.myapplication.data.login;

public class ViewData {

    private boolean flag;
    private User user;

    public ViewData(boolean flag, User user) {
        this.flag = flag;
        this.user = user;
    }

    public ViewData(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
