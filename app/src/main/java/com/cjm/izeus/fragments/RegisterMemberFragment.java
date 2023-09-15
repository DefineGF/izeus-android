package com.cjm.izeus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cjm.izeus.listener.IFragmentClickListener;
import com.cjm.izeus.R;
import com.cjm.izeus.adapter.MemberInputAdapter;
import com.cjm.izeus.bean.Member;
import com.cjm.izeus.util.HttpUtil;
import com.cjm.izeus.util.PrefSaveAndRead;
import com.cjm.izeus.util.URLConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterMemberFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "RegisterMemberFragment";
    private static final int HANDLER_SUC = 1;
    private static final int HANDLER_EOR = 0;

    private static MemberInputAdapter adapter;
    private IFragmentClickListener listener;
    private Button btnConfirm;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_SUC:
                    Toast.makeText(getContext(),"注册成功!",Toast.LENGTH_SHORT).show();
                    listener.fragmentThreeClick();
                    break;
                case HANDLER_EOR:
                    Toast.makeText(getContext(),"注册失败!",Toast.LENGTH_SHORT).show();
                    btnConfirm.setClickable(true);
                    break;
            }
        }
    };
    public static RegisterMemberFragment newInstance(int count){
        initData(count);
        return new RegisterMemberFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){
            View view = inflater.inflate(R.layout.fragment_register_member,container,false);
            RecyclerView recyclerView = view.findViewById(R.id.member_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
            recyclerView.setAdapter(adapter);
            btnConfirm = view.findViewById(R.id.fragment_member_btn_confirm);
            btnConfirm.setOnClickListener(this);
            return view;
        }else{
            Log.e(TAG,"container is null");
            return null;
        }

    }



    private static void initData(int count){
        List<Member> memberList = new ArrayList<>();
        for(int i = 1;i <= count;i++){
            Member temp = new Member(i);
            memberList.add(temp);
        }
        adapter = new MemberInputAdapter(memberList);
    }

    public void setFragmentListener(IFragmentClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fragment_member_btn_confirm){
            String messageSent = adapter.getMsg();
            String sessionId = PrefSaveAndRead.getSession(getActivity());
            Log.e(TAG,"待发送数据:sessionId = "+sessionId + "messageSend is :"+messageSent);

            RequestBody requestBody = new FormBody.Builder().add("members_json",messageSent).build();
            assert sessionId != null;
            Request request = new Request.Builder()
                    .addHeader("cookie",sessionId)
                    .url(URLConstant.MEMBER_URL)
                    .post(requestBody)
                    .build();
            HttpUtil.sendOkHttpRequest(request, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msgEor = handler.obtainMessage();
                    msgEor.what = HANDLER_EOR;
                    handler.sendMessage(msgEor);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message msg = handler.obtainMessage();
                    if(response.body() != null){
                        msg.what = (response.body().string().equals("Success"))?HANDLER_SUC:HANDLER_EOR;
                    }else{
                        msg.what = HANDLER_EOR;
                    }
                    handler.sendMessage(msg);
                }
            });
            btnConfirm.setClickable(false);
        }
    }
}
