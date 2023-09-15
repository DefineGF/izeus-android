package com.cjm.izeus.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cjm.izeus.listener.IFragmentClickListener;
import com.cjm.izeus.R;
import com.cjm.izeus.fragments.RegisterAccountFragment;
import com.cjm.izeus.fragments.RegisterCompanyFragment;
import com.cjm.izeus.fragments.RegisterMemberFragment;

public class RegisterActivity extends FragmentActivity implements IFragmentClickListener {
    private static final String TAG = "RegisterActivity";
    private TextView tvCurFrgMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvCurFrgMsg = findViewById(R.id.register_tv_cur_frg);
        setDefaultFragment();
    }

    private void setDefaultFragment(){
        RegisterCompanyFragment registerCompanyFragment =
                RegisterCompanyFragment.newInstance(FRAGMENT_COMPANY);
        registerCompanyFragment.setFragmentListener(this);
        replaceFragment(registerCompanyFragment);
        tvCurFrgMsg.setText("单位信息");
    }


    @Override
    public void fragmentOneClick(int curFlag) {
        if(curFlag == FRAGMENT_COMPANY){
            RegisterAccountFragment registerAccountFragment =
                    RegisterAccountFragment.newInstance(FRAGMENT_ACCOUNT);
            registerAccountFragment.setFragmentListener(this);
            replaceFragment(registerAccountFragment);
            tvCurFrgMsg.setText("账户信息");
        }
    }

    @Override
    public void fragmentTwoClick(int fragId, int count) {
        if(fragId == FRAGMENT_ACCOUNT){
            Log.e("RegisterActivity","加載最後界面");
            RegisterMemberFragment registerMemberFragment = RegisterMemberFragment.newInstance(count);
            registerMemberFragment.setFragmentListener(this);
            replaceFragment(registerMemberFragment);
            tvCurFrgMsg.setText("成员信息");
        }
    }

    @Override
    public void fragmentThreeClick() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.register_fragment_container,fragment);
        transaction.commit();
    }
}
