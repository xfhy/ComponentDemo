package com.xfhy.component.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.xfhy.componentbase.ServiceFactory;

/**
 * Created by xfhy on 2018/10/18 21:46
 * Description :
 */
public class LoginInterceptor implements IInterceptor {

    private Context context;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getPath().equals("/share/share")) {
            if(ServiceFactory.getInstance().getAccountService().isLogin()){
                callback.onContinue(postcard);
            }
        }
    }

    @Override
    public void init(Context context) {
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
        this.context = context;
    }
}
