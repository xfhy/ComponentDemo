package com.xfhy.login;

import android.app.Application;

import com.xfhy.base.BaseApp;
import com.xfhy.componentbase.ServiceFactory;

/**
 * Created by feiyang on 2018/10/18 17:20
 * Description : 登录组件的Application
 */
public class LoginApp extends BaseApp {

    @Override
    public void initModuleApp(Application application) {
        //将 AccountService 类的实例注册到 ServiceFactory
        ServiceFactory.getInstance().setAccountService(new AccountService());
    }

    @Override
    public void initModuleData(Application application) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initModuleApp(this);
        initModuleData(this);
    }
}
