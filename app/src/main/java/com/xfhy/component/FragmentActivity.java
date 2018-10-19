package com.xfhy.component;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xfhy.componentbase.ServiceFactory;

/**
 * 2018年10月19日09:34:04
 * 主模块的FragmentActivity
 */
public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //通过组件提供的Service实现Fragment的实例化  哪里需要这个Fragment,就放到哪里  简单方便
        ServiceFactory.getInstance().getAccountService().newUserFragment(this, R.id.layout_fragment, getSupportFragmentManager(), null, "");
    }
}
