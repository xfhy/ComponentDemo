package com.xfhy.component;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
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

        // 初始化 ARouter
        if (isDebug()) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效

            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }

        // 初始化 ARouter
        ARouter.init(this);

        // 其他操作 ...

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

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}
