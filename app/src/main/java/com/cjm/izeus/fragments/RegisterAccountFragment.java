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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cjm.izeus.listener.IFragmentClickListener;
import com.cjm.izeus.R;
import com.cjm.izeus.util.PrefSaveAndRead;
import com.cjm.izeus.util.URLConstant;
import com.cjm.izeus.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterAccountFragment extends Fragment implements View.OnClickListener {
    private static final int HANDLER_MSG_SUC = 1;
    private static final int HANDLER_MSG_EOR = 0;
    private static final String TAG = "RegisterAccountFragment";

    private static int FRAGMENT_ID;
    private EditText etCount,etMinCount,etMinMoney;
    private Button btnConfirm;
    private IFragmentClickListener listener;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_MSG_SUC:
                    int count = msg.arg1;
                    listener.fragmentTwoClick(FRAGMENT_ID,count);
                    break;
                case HANDLER_MSG_EOR:
                    Toast.makeText(getContext(),"连接失败,请重新输入!",Toast.LENGTH_SHORT).show();
                    btnConfirm.setClickable(true);
                    break;
            }
        }
    };


    public static RegisterAccountFragment newInstance(int fragId){
        RegisterAccountFragment registerAccountFragment = new RegisterAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("FRAGMENT_ID",fragId);
        registerAccountFragment.setArguments(bundle);
        FRAGMENT_ID = fragId;
        return registerAccountFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_acc,container,false);
        etCount = view.findViewById(R.id.account_et_count);
        etMinCount = view.findViewById(R.id.account_et_min_count);
        etMinMoney = view.findViewById(R.id.account_min_money);

        btnConfirm = view.findViewById(R.id.fragment_account_btn);
        btnConfirm.setOnClickListener(this);
        return view;
    }

    public void setFragmentListener(IFragmentClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fragment_account_btn){
            final String count = etCount.getText().toString();
            String minCount = etMinCount.getText().toString();
            String minMoney    = etMinMoney.getText().toString();
            /*
            执行一些骚操作
             */
            String sessionID = PrefSaveAndRead.getSession(getActivity());
            Log.e(TAG,"get the SessionId is "+sessionID);
            RequestBody requestBody = new FormBody.Builder()
                                                  .add("tn_n",count)
                                                  .add("tn_t",minCount)
                                                  .add("minAmount",minMoney)
                                                  .build();
            assert sessionID != null;
            Request request = new Request.Builder()
                                         .addHeader("cookie",sessionID)
                                         .url(URLConstant.ACCOUNT_URL)
                                         .post(requestBody)
                                         .build();

            HttpUtil.sendOkHttpRequest(request, new Callback() {
                Message msg = handler.obtainMessage();
                @Override
                public void onFailure(Call call, IOException e) {
                    msg.what = HANDLER_MSG_EOR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.body() != null && response.body().string().equals("Success")){
                        msg.what = HANDLER_MSG_SUC;
                        msg.arg1 = Integer.parseInt(count);
                    }else{
                        msg.what = HANDLER_MSG_EOR;
                    }
                    handler.sendMessage(msg);
                }

            });
            btnConfirm.setClickable(false);

        }
    }
}
