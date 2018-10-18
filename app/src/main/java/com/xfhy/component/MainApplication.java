package com.xfhy.component;

import android.app.Application;

import com.xfhy.base.AppConfig;
import com.xfhy.base.BaseApp;

/**
 * Created by feiyang on 2018/10/18 17:47
 * Description :
 * <p>
 * 主 module 的 Application 也继承 BaseApp ，并实现两个初始化方法，
 * 在这两个初始化方法中遍历 AppcConfig 类中定义的 moduleApps 数组中的类名，通过反射，初始化各个组件的 Application。
 */
public class MainApplication extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化组件 Application
        initModuleApp(this);

        // 其他操作

        // 所有 Application 初始化后的操作
        initModuleData(this);
    }

    @Override
    public void initModuleApp(Application application) {
        for (int i = 0; i < AppConfig.moduleApps.length; i++) {
            try {
                Class clazz = Class.forName(AppConfig.moduleApps[i]);
                BaseApp baseApp = (BaseApp) clazz.newInstance();
                baseApp.initModuleApp(this);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initModuleData(Application application) {
        for (int i = 0; i < AppConfig.moduleApps.length; i++) {
            try {
                Class clazz = Class.forName(AppConfig.moduleApps[i]);
                BaseApp baseApp = (BaseApp) clazz.newInstance();
                baseApp.initModuleData(this);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
