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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterCompanyFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "单位信息";
    private static final int HANDLER_MSG_SUC = 1;
    private static final int HANDLER_MSG_ERR = 0;
    public static int FRAGMENT_ID;
    private EditText etCompanyName,etCompanyQuality,etRepresent,etContact,etAddress;
    private Button btnConfirm;
    private IFragmentClickListener listener;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == HANDLER_MSG_SUC){
                Toast.makeText(getContext(),"输入成功!",Toast.LENGTH_SHORT).show();
                listener.fragmentOneClick(FRAGMENT_ID);
            }

            if(msg.what == HANDLER_MSG_ERR){
                Toast.makeText(getContext(),"信息错误,请重新输入!",Toast.LENGTH_SHORT).show();
                btnConfirm.setClickable(true);
            }
        }
    };
    public static RegisterCompanyFragment newInstance(int fragId){
        RegisterCompanyFragment registerCompanyFragment = new RegisterCompanyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("FRAGMENT_ID",fragId);
        registerCompanyFragment.setArguments(bundle);
        FRAGMENT_ID = fragId;
        return registerCompanyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_company,container,false);
        etCompanyName    = view.findViewById(R.id.et_company_name);
        etCompanyQuality = view.findViewById(R.id.et_company_quality);
        etRepresent      = view.findViewById(R.id.et_represent);
        etContact        = view.findViewById(R.id.et_contact);
        etAddress        = view.findViewById(R.id.et_address);
        btnConfirm       = view.findViewById(R.id.fragment_company_btn_confirm);
        btnConfirm.setOnClickListener(this);
        return view;
    }


    public void setFragmentListener(IFragmentClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fragment_company_btn_confirm){
            btnConfirm.setClickable(false);
            String company_name     = etCompanyName.getText().toString();
            String company_quality  = etCompanyQuality.getText().toString();
            String representative   = etRepresent.getText().toString();
            String contact          = etContact.getText().toString();
            String address          = etAddress.getText().toString();
            /*
             post
             */
            RequestBody requestBody = new FormBody.Builder()
                    .add("company_name",company_name)
                    .add("company_quality",company_quality)
                    .add("representative",representative)
                    .add("contact",contact)
                    .add("address",address)
                    .build();
            Request request = new Request.Builder()
                                .url(URLConstant.COMPANY_URL)
                                .post(requestBody)
                                .build();

            HttpUtil.sendOkHttpRequest(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg = handler.obtainMessage();
                    msg.what = HANDLER_MSG_ERR;
                    handler.sendMessage(msg);
                    Log.e(TAG,"onFailure message :"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respBodyStr = response.body().string();
                    Log.e(TAG,"onResponse:the response body is :"+respBodyStr);
                    Message msg = handler.obtainMessage();
                    if(response.body() != null && respBodyStr.equals("Success")){ //成功
                        msg.what = HANDLER_MSG_SUC;
                        Headers headers = response.headers();
                        List<String> cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0);
                        String sessionId = session.substring(0,session.indexOf(";"));
                        Log.e(TAG,"get the session id is :"+session+" sessionId is :"+sessionId);
                        PrefSaveAndRead.saveSession(getActivity(),sessionId);
                    }else{//失败
                        msg.what = HANDLER_MSG_ERR;
                    }
                    handler.sendMessage(msg);
                }
            });
        }

    }

}
