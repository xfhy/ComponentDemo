package com.xfhy.login;

import com.xfhy.componentbase.service.IAccountService;

/**
 * Created by feiyang on 2018/10/18 17:15
 * Description :
 */
public class AccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return UserInfo.getUserInfo() != null;
    }

    @Override
    public String getAccountId() {
        return UserInfo.getUserInfo() == null ? null : UserInfo.getUserInfo().accountId;
    }
}
