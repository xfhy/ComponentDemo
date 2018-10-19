package com.xfhy.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xfhy.componentbase.service.IAccountService;

/**
 * Created by feiyang on 2018/10/18 17:15
 * Description :
 */
public class AccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return AccountUtils.userInfo != null;
    }

    @Override
    public String getAccountId() {
        return AccountUtils.userInfo == null ? null : AccountUtils.userInfo.accountId;
    }

    @Override
    public Fragment newUserFragment(Activity activity, int containerId, FragmentManager manager, Bundle bundle, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        UserFragment userFragment = new UserFragment();
        fragmentTransaction.add(containerId, userFragment, tag);
        fragmentTransaction.commit();
        return userFragment;
    }
}
