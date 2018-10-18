package com.xfhy.login;

/**
 * Created by feiyang on 2018/10/18 17:16
 * Description : 用户 实体类
 */
public class UserInfo {

    public String accountId;

    /**
     * 获取用户信息  测试
     */
    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.accountId = "test";
        return userInfo;
    }

}
