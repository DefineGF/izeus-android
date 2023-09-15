package com.cjm.izeus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjm.izeus.R;
import com.cjm.izeus.util.HttpUtil;
import com.cjm.izeus.util.URLConstant;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class AccountDisplayFragment extends Fragment {
    private static final String TAG ="AccountDisplay";
    private static final int HANDLER_SUC = 1;
    private static final int HANDLER_EOR = 1;
    private static String sessionID;
    private View view;

    public static AccountDisplayFragment newInstance(String sessionId){
        AccountDisplayFragment accountDisplayFragment = new AccountDisplayFragment();
        sessionID = sessionId;
        Log.d(TAG,"run the newInstance");
        return accountDisplayFragment;
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_SUC){
                String resBodyString = String.valueOf(msg.obj);
                Log.d(TAG +"get_res","get the response body is :"+resBodyString);
                JSONObject jsonObject = JSON.parseObject(resBodyString);
                updateView(jsonObject);
            }
        }
    };

    private void initData(String sessionId){
        Request request = new Request.Builder()
                .url(URLConstant.GET_ACCOUNT_MSG_URL)
                .addHeader("cookie",sessionId)
                .build();

        HttpUtil.sendOkHttpRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"连接失败: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = handler.obtainMessage();
                msg.what = HANDLER_SUC;
                msg.obj = response.body().string();
                Log.d(TAG,"get the result is "+ msg.obj);
                handler.sendMessage(msg);
            }
        });

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"run the onCreateView");
        view = inflater.inflate(R.layout.fragment_account_msg,container,false);
        initData(sessionID);
        return view;
    }
    private void updateView(JSONObject jsonObject){
        ((TextView)view.findViewById(R.id.company_tv_name)).setText(jsonObject.getString("company_name"));
        ((TextView)view.findViewById(R.id.company_tv_type)).setText(jsonObject.getString("company_quality"));
        ((TextView)view.findViewById(R.id.company_tv_rep)).setText(jsonObject.getString("representative"));
        ((TextView)view.findViewById(R.id.company_tv_contact)).setText(jsonObject.getString("contact"));
        ((TextView)view.findViewById(R.id.company_tv_address)).setText(jsonObject.getString("address"));
        ((TextView)view.findViewById(R.id.acc_tv_name)).setText(jsonObject.getString("account"));
        ((TextView)view.findViewById(R.id.acc_tv_balance)).setText(jsonObject.getString("balance"));
        ((TextView)view.findViewById(R.id.acc_tv_tn)).setText(jsonObject.getString("tn_n"));
        ((TextView)view.findViewById(R.id.acc_tv_tt)).setText(jsonObject.getString("tn_t"));
        ((TextView)view.findViewById(R.id.acc_tv_amount)).setText(jsonObject.getString("max_amount"));
    }
}
