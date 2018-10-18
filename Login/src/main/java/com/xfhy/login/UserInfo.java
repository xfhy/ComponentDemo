package com.xfhy.login;

/**
 * Created by feiyang on 2018/10/18 17:16
 * Description : 用户 实体类
 */
public class UserInfo {

    public String accountId;
    public String password;

    public UserInfo(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

}
