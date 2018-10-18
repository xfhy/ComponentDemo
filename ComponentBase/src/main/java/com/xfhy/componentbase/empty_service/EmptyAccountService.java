package com.xfhy.componentbase.empty_service;

import com.xfhy.componentbase.service.IAccountService;

/**
 * Created by feiyang on 2018/10/18 14:46
 * Description : 组件Service空实现
 * empty_service 中是 service 中定义的接口的空实现
 */
public class EmptyAccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return null;
    }
}
