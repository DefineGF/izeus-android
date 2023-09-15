package com.cjm.izeus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cjm.izeus.R;
import com.cjm.izeus.bean.AccountOverview;
import com.cjm.izeus.util.HttpUtil;
import com.cjm.izeus.util.URLConstant;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class BankCardMsgFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BANK_CARD_MSG_FRAGMENT";
    private static final int HANDLER_SUC = 1;
    private static final int HANDLER_EOR = 0;

    private Button btnCurrentAcc,btnRegularAcc;
    private boolean isLeft = true;
    private static String sessionID;
    private BankCurrentAccountFragment bankCurrentAccountFragment;
    private BankRegularAccountFragment bankRegularAccountFragment;

    private List<AccountOverview> accountOverviewList;

//    private List<CurrentAccountMsg> currentAccountMsgList = new ArrayList<>();
//    private List<RegularAccountMsg> regularAccountMsgList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_SUC){
                Toast.makeText(getContext(),"获取信息成功",Toast.LENGTH_SHORT).show();
                setLeftFrag();
            }
            if(msg.what == HANDLER_EOR){
                Toast.makeText(getContext(),"获取信息失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static BankCardMsgFragment newInstance(String sessionId){
        BankCardMsgFragment bankCardMsgFragment = new BankCardMsgFragment();
        sessionID = sessionId;
        Log.e(TAG,"run the new Fragment");
        return bankCardMsgFragment;
    }
    private void initData(String sessionId){
        Request request = new Request.Builder()
                .url(URLConstant.GET_BANK_CARD_URL)
                .addHeader("cookie",sessionId )
                .build();

        HttpUtil.sendOkHttpRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = HANDLER_EOR;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resBodyString = response.body().string();
                Log.d(TAG,"get the message is:"+resBodyString);
                accountOverviewList = JSON.parseObject(resBodyString,new TypeReference<List<AccountOverview>>(){}.getType());
                Message msg = handler.obtainMessage();
                msg.what = HANDLER_SUC;
                handler.sendMessage(msg);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView run");
        initData(sessionID);
        View view     = inflater.inflate(R.layout.fragment_bank_card_msg,container,false);
        btnCurrentAcc = view.findViewById(R.id.acc_msg_btn_current);
        btnRegularAcc = view.findViewById(R.id.acc_msg_btn_regular);
        btnCurrentAcc.setOnClickListener(this);
        btnRegularAcc.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.acc_msg_btn_current){
            if(!isLeft){
                setLeftFrag();
            }
        }

        if(v.getId() == R.id.acc_msg_btn_regular){
            if(isLeft){
                setRightFrag();
            }
        }
    }


    private void setLeftFrag(){
        isLeft = true;
        btnCurrentAcc.setBackground(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.account_msg_btn_select));
        btnRegularAcc.setBackground(getActivity().getDrawable(R.drawable.account_msg_btn_unselected));
        if(bankCurrentAccountFragment == null){
            bankCurrentAccountFragment = BankCurrentAccountFragment.newInstance(accountOverviewList);
        }

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.acc_msg_frag_container, bankCurrentAccountFragment);
        transaction.commit();

    }

    @SuppressLint("ResourceAsColor")
    private void setRightFrag(){
        isLeft = false;
        btnRegularAcc.setBackground(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.account_msg_btn_select));
        btnCurrentAcc.setBackground(getActivity().getDrawable(R.drawable.account_msg_btn_unselected));
        if(bankRegularAccountFragment == null){
            bankRegularAccountFragment = BankRegularAccountFragment.newInstance(accountOverviewList);
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.acc_msg_frag_container, bankRegularAccountFragment);
        transaction.commit();
    }
}
