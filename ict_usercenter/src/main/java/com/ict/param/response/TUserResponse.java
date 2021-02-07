package com.ict.param.response;

public class TUserResponse {
    private Integer userId;

    /**
     * 名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String phone;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
