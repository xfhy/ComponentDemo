package com.xfhy.component.interceptor;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.xfhy.componentbase.ServiceFactory;

/**
 * Created by xfhy on 2018/10/18 21:46
 * Description : 登录状态拦截器
 * <p>
 * 自定义的过滤器需要通过 @Interceptor 来注解，priority 是优先级，name 是对这个拦截器的描述。
 */
@Interceptor(priority = 8, name = "登录状态拦截器")
public class LoginInterceptor implements IInterceptor {

    private Context context;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        //onContinue和onInterrupt至少需要调用其中一种,否则不会继续路由
        if (postcard.getPath().equals("/share/share")) {
            if (ServiceFactory.getInstance().getAccountService().isLogin()) {
                //已登录
                callback.onContinue(postcard);
            } else {
                //未登录
                //中断路由流程
                callback.onInterrupt(new RuntimeException("请登录"));
            }
        } else {
            //不是分享   那么你继续
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
        this.context = context;
    }
}
