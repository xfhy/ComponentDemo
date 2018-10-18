package com.xfhy.base;

import android.app.Application;

/**
 * Created by feiyang on 2018/10/18 17:20
 * Description : 基类  Application
 */
public abstract class BaseApp extends Application {

    /**
     * Application 初始化
     */
    public abstract void initModuleApp(Application application);

    /**
     * 所有 Application 初始化后的自定义操作
     */
    public abstract void initModuleData(Application application);

}
