package com.cjm.izeus.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cjm.izeus.R;
import com.cjm.izeus.bean.Transfer;
import com.cjm.izeus.fragments.AccountDisplayFragment;
import com.cjm.izeus.fragments.AccountTransferFragment;
import com.cjm.izeus.fragments.BankCardMsgFragment;
import com.cjm.izeus.fragments.TransferRecordFragment;
import com.cjm.izeus.listener.ILunchTfListener;
import com.cjm.izeus.util.HttpUtil;
import com.cjm.izeus.util.PrefSaveAndRead;
import com.cjm.izeus.util.URLConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , ILunchTfListener {

    private static final String TAG = "HomeActivity";
    private static final int HANDLER_GET_SUC = 1;
    private AccountTransferFragment   accountTransferFragment;
    private AccountDisplayFragment    accountDisplayFragment;
    private BankCardMsgFragment       bankCardMsgFragment;
    private TransferRecordFragment    transferRecordFragment;

    private String sessionId;
    private long mPressedTime = 0;
    private List<Transfer> transferList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle("门限");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.home_nav_header);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sessionId = PrefSaveAndRead.getSession(this);
        String userName = PrefSaveAndRead.getMsg(this,URLConstant.USER_MSG,URLConstant.USER_NAME_kEY);
        if(userName != null){
            ((TextView)headerView.findViewById(R.id.nv_header_tv_account)).setText(userName);
        }
        getMsgFromServer();
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_GET_SUC){
                String json = String.valueOf(msg.obj);
                Log.e(TAG,"get the json is :"+json);

                transferList = JSON.parseObject(json,new TypeReference<List<Transfer>>(){}.getType());
                if(transferRecordFragment == null){
                    transferRecordFragment = TransferRecordFragment.newInstance(transferList);
                }else{//重新获取 数据更新
                    transferRecordFragment.dataUpdate(transferList);
                }

                List<Transfer> transferUnFinishList = getTransferUnFinishList(transferList);
                if(accountTransferFragment == null){
                    accountTransferFragment = AccountTransferFragment.newInstance(transferUnFinishList);
                    accountTransferFragment.setLunchTfListener(HomeActivity.this);
                }else{//重新获取 数据更新
                    accountTransferFragment.dataUpdate(transferUnFinishList);
                }

                setDefaultFragment();//获取转账信息成功后，设置门限转账界面为默认主界面
            }
        }
    };

    //从服务器获取转账信息
    private void getMsgFromServer(){
        Log.e(TAG,"get the transfer record");
        Request request = new Request.Builder()
                .addHeader("cookie",sessionId)
                .url(URLConstant.TRANSFER_RECORD_URL)
                .build();
        HttpUtil.sendOkHttpRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"获取转账记录失败，错误信息为:"+e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respBody = response.body().string();
                Log.d(TAG,"get the response string is :"+respBody);
                Message msg = handler.obtainMessage();
                msg.what = HANDLER_GET_SUC;
                msg.obj = respBody;
                handler.sendMessage(msg);
            }
        });
    }

    private List<Transfer> getTransferUnFinishList(List<Transfer> transferList){
        List<Transfer> result = new ArrayList<>();
        for(Transfer transfer : transferList){
            if(transfer.getState().equals("未完成")){
                result.add(transfer);
            }
        }
        return result;
    }
    //增加新的记录时 重新获取数据
    @Override
    public void notifyDataUpdate() {
        Log.d(TAG,"run the notifyDataUpdate");
        getMsgFromServer();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_account:
                if(accountDisplayFragment == null){
                    Log.e(TAG,"accountDisplayFragment is null");
                    accountDisplayFragment = AccountDisplayFragment.newInstance(sessionId);
                }else{
                    Log.e(TAG,"accountDisplayFragment not null");
                }
                getSupportActionBar().setTitle("账户信息");
                replaceFragment(accountDisplayFragment);
                break;
            case R.id.nav_transfer://门限转账界面
                if(accountTransferFragment == null){
                    accountTransferFragment = AccountTransferFragment.newInstance(transferList);
                    accountTransferFragment.setLunchTfListener(this);
                }
                getSupportActionBar().setTitle("门限转账");
                replaceFragment(accountTransferFragment);
                break;
            case R.id.nav_record://转账记录
                if (transferRecordFragment == null){
                    transferRecordFragment = TransferRecordFragment.newInstance(transferList);
                }
                getSupportActionBar().setTitle("交易记录");
                replaceFragment(transferRecordFragment);
                break;
            case R.id.nav_bank_card://银行卡信息
                if(bankCardMsgFragment == null){
                    bankCardMsgFragment = BankCardMsgFragment.newInstance(sessionId);
                }
                getSupportActionBar().setTitle("银行卡信息");
                replaceFragment(bankCardMsgFragment);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_frag_container,fragment);
        transaction.commit();
    }


    private void setDefaultFragment(){
        if(transferRecordFragment == null) {
            transferRecordFragment = TransferRecordFragment.newInstance(transferList);
        }
        replaceFragment(transferRecordFragment);
        getSupportActionBar().setTitle("交易记录");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long mNowTime = System.currentTimeMillis();
            if((mNowTime - mPressedTime) > 2000){
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
            }else{
                finish();
                System.exit(0);
            }
        }
    }
}
