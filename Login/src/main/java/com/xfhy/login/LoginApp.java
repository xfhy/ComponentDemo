package com.xfhy.login;

import com.xfhy.base.BaseApp;
import com.xfhy.componentbase.ServiceFactory;

/**
 * Created by feiyang on 2018/10/18 17:20
 * Description : 登录组件的Application
 */
public class LoginApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        //将 AccountService 类的实例注册到 ServiceFactory
        ServiceFactory.getInstance().setAccountService(new AccountService());
    }
}
