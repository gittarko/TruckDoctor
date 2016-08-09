package com.juma.truckdoctor.js.model;

import java.io.Serializable;

/**
 * Created by hedong on 16/8/4.
 */

public class User implements Serializable {
    int userId;
    String loginTime;
    String userName;
    String phone;
    String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
