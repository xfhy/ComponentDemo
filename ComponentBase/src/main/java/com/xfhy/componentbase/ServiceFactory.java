package com.xfhy.componentbase;

import com.xfhy.componentbase.empty_service.EmptyAccountService;
import com.xfhy.componentbase.service.IAccountService;

/**
 * Created by feiyang on 2018/10/18 14:48
 * Description : 单例
 * ServiceFactory 接收组件中实现的接口对象的注册以及向外提供特定组件的接口实现。
 */
public class ServiceFactory {

    private IAccountService mAccountService;

    /**
     * 禁止外部创建ServiceFactory对象
     */
    private ServiceFactory() {
    }

    private static class Inner {
        private final static ServiceFactory SERVICE_FACTORY = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return Inner.SERVICE_FACTORY;
    }

    /**
     * 接收 Login 组件实现的 Service 实例
     * Login 组件中将 IAccountService 的实现类对象注册到 ServiceFactory 中以后，其他模块就可以使用这个 Service 与 Login 组件进行数据传递
     */
    public void setAccountService(IAccountService accountService) {
        mAccountService = accountService;
    }

    /**
     * 返回 Login 组件的 Service 实例
     */
    public IAccountService getAccountService() {
        if (mAccountService == null) {
            mAccountService = new EmptyAccountService();
        }
        return mAccountService;
    }
}
