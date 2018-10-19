package com.xfhy.componentbase.service;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by feiyang on 2018/10/18 14:47
 * Description : 组件Service接口定义
 * 定义了 Login 组件向外提供的数据传递的接口方法
 * <p>
 * 其中 service 文件夹中定义接口， IAccountService 接口中定义了 Login 组件向外提供的数据传递的接口方法
 */
public interface IAccountService {
    /**
     * 是否已经登录
     */
    boolean isLogin();

    /**
     * 获取登录用户的 AccountId
     */
    String getAccountId();

    /**
     * 创建 UserFragment
     */
    Fragment newUserFragment(Activity activity, int containerId, FragmentManager manager, Bundle bundle, String tag);
}
