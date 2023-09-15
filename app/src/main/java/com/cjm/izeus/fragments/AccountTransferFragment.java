package com.cjm.izeus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cjm.izeus.R;
import com.cjm.izeus.adapter.AccountTransferAdapter;
import com.cjm.izeus.bean.Transfer;
import com.cjm.izeus.listener.ILunchTfListener;
import com.cjm.izeus.listener.IRecyclerItemListener;
import com.cjm.izeus.util.HttpUtil;
import com.cjm.izeus.util.PrefSaveAndRead;
import com.cjm.izeus.util.URLConstant;
import com.cjm.izeus.widget.BottomDialog;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountTransferFragment extends Fragment implements View.OnClickListener,
        IRecyclerItemListener {

    private static final String TAG = "AccountTransferDisplay";
    private static final int HANDLER_LUNCH_SUC    = 1;
    private static final int HANDLER_LUNCH_EOR    = 0;
    private static final int HANDLER_CONF_SUC     = 11;
    private static final int HANDLER_CONF_EOR     = 10;
    private static final int HANDLER_CANE_SUC     = 21;
    private static final int HANDLER_CANE_EOR     = 20;
    private static final int HANDLER_INTERNET_EOR = -1;

    private String sessionId,userName;
    private AccountTransferAdapter adapter;
    private ILunchTfListener lunchTfListener;
    private List<Transfer> transferList;

    private Button btnLunchTf;
    private Button btnConfirm,btnCancel;


    public static AccountTransferFragment newInstance(List<Transfer> transfers){
        AccountTransferFragment accountTransferFragment = new AccountTransferFragment();
        accountTransferFragment.initData(transfers);
        Log.e(TAG,"set the data finish");
        return accountTransferFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"run the onCreateView");
        View view = inflater.inflate(R.layout.acc_transfer_display,container,false);
        FloatingActionButton fabAddTransfer = view.findViewById(R.id.fab_add_transfer);
        fabAddTransfer.setOnClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.account_transfer_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter = new AccountTransferAdapter(transferList);
        adapter.setRecyclerItemListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionId = PrefSaveAndRead.getSession(getActivity());
        userName  = PrefSaveAndRead.getMsg(getActivity(),URLConstant.USER_MSG,URLConstant.USER_NAME_kEY);
        Log.e(TAG,"get the sessionId "+sessionId +" userName = " + userName);
    }

    private void initData(List<Transfer> transfers){
        this.transferList = transfers;
    }


    public void dataUpdate(List<Transfer> transfers){
        this.transferList = transfers;
        adapter.notifyDataSetChanged();
    }

    public void setLunchTfListener(ILunchTfListener listener){
        this.lunchTfListener = listener;
    }

    @Override
    public void onClick(View v) {
        if((v.getId() == R.id.fab_add_transfer)){
            View dialogLunchTfView = initDialogLunchContentView();
            BottomDialog bottomDialog = BottomDialog.newInstance(getContext(), dialogLunchTfView);
            bottomDialog.show();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String toastMessage = "null";
            switch (msg.what){
                case HANDLER_LUNCH_SUC:
                    toastMessage = "发起转账成功";
                    Log.e(TAG,"更新数据");
                    lunchTfListener.notifyDataUpdate();
                    break;
                case HANDLER_LUNCH_EOR:
                    toastMessage = "发起转账失败 金额过小";
                    btnLunchTf.setClickable(true);
                    break;
                case HANDLER_CONF_SUC:
                    toastMessage = "确认转账成功";
                    lunchTfListener.notifyDataUpdate();
                    btnConfirm.setBackground(getActivity().getDrawable(R.drawable.btn_cannt_click));
                    break;
                case HANDLER_CONF_EOR:
                    toastMessage = "确认转账失败";
                    btnConfirm.setClickable(true);
                    break;
                case HANDLER_CANE_SUC:
                    toastMessage = "终止转账成功";
                    lunchTfListener.notifyDataUpdate();
                    btnCancel.setBackground(getActivity().getDrawable(R.drawable.btn_cannt_click));
                    break;
                case HANDLER_CANE_EOR:
                    toastMessage = "终止转账失败";
                    btnCancel.setClickable(true);
                    break;
                case HANDLER_INTERNET_EOR:
                    toastMessage = "网络连接失败";
                    break;
            }
            Toast.makeText(getContext(),toastMessage,Toast.LENGTH_SHORT).show();
        }
    };

    //发起转账dialog_view 初始化
    @SuppressLint("InflateParams")
    private View initDialogLunchContentView(){
        final View dialogLunchTfView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_lunch_transfer,null);
        btnLunchTf = dialogLunchTfView.findViewById(R.id.lunch_rt_btn_confirm);
        btnLunchTf.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String toAcc   = ((EditText) dialogLunchTfView.findViewById(R.id.lunch_tf_et_to_acc)).getText().toString();
                String amount  = ((EditText) dialogLunchTfView.findViewById(R.id.lunch_tf_amount)).getText().toString();
                String addMsg  = ((EditText) dialogLunchTfView.findViewById(R.id.lunch_tf_add_msg)).getText().toString();

                RequestBody requestBody = new FormBody.Builder()
                        .add("to_account",toAcc)
                        .add("amount",amount)
                        .add("note",addMsg)
                        .build();

                Request request = new Request.Builder()
                        .addHeader("cookie",sessionId)
                        .post(requestBody)
                        .url(URLConstant.LUNCH_TRANSFER_URL)
                        .build();
                HttpUtil.sendOkHttpRequest(request, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG,"获取失败，错误信息为:"+ e.getMessage());
                        Message msg = handler.obtainMessage();
                        msg.what = HANDLER_INTERNET_EOR;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String respBodyStr = response.body().string();
                        Log.e(TAG,"get the lunch response is :" + respBodyStr);
                        Message msg = handler.obtainMessage();
                        if(respBodyStr != null) {
                            if (respBodyStr.equals("Success")) {
                                msg.what = HANDLER_LUNCH_SUC;
                            } else {
                                msg.what = HANDLER_LUNCH_EOR;
                            }
                            handler.sendMessage(msg);
                        }
                    }
                });
                btnLunchTf.setClickable(false);
            }
        });
        return dialogLunchTfView;
    }


    //确认 取消交易 监听事件
    @SuppressLint({"InflateParams"})
    @Override
    public void recyclerItemClick(final Transfer record) {
        View dialogTranDetailView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_content, null);
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_id)).setText(record.getId());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_from_name)).setText(record.getFrom_name());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_add_msg)).setText(record.getNote());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_pass)).setText(record.getVerified());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_un_pass)).setText(record.getUnverified());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_from_acc)).setText(record.getFrom_account());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_to_acc)).setText(record.getTo_account());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_state)).setText(record.getState());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_money)).setText(record.getAmount());
        ((TextView) dialogTranDetailView.findViewById(R.id.dialog_start_time)).setText(record.getTn_time());

        //根据状态显示颜色
        String state = record.getState();
        if(state.equals("已完成")){
            ((TextView) dialogTranDetailView.findViewById(R.id.dialog_state))
                    .setTextColor(getResources().getColor(R.color.colorTransferStateFinish));
        }else if(state.equals("未完成")){
            ((TextView) dialogTranDetailView.findViewById(R.id.dialog_state))
                    .setTextColor(getResources().getColor(R.color.colorTransferStateUnFinish));
        }else{
            ((TextView) dialogTranDetailView.findViewById(R.id.dialog_state))
                    .setTextColor(getResources().getColor(R.color.colorTransferStateFailure));
        }


        btnConfirm = dialogTranDetailView.findViewById(R.id.dialog_btn_confirm);
        btnCancel  = dialogTranDetailView.findViewById(R.id.dialog_btn_cancel);
        if(record.getFrom_name().equals(userName)){
            btnConfirm.setClickable(false);
            btnConfirm.setBackground(getActivity().getDrawable(R.drawable.btn_cannt_click));
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIDToServer(URLConstant.CONF_LUNCH_TF_ID,record.getId(),0);
                btnConfirm.setClickable(false);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIDToServer(URLConstant.CNCL_LUNCH_TF_ID,record.getId(),1);
                btnCancel.setClickable(false);
            }
        });

        BottomDialog bottomDialog = BottomDialog.newInstance(getContext(), dialogTranDetailView);
        bottomDialog.show();
    }

    private void sendIDToServer(String url,String id,final int flg){
        final RequestBody requestBody = new FormBody.Builder().add("id",id).build();
        Request request = new Request.Builder()
                .addHeader(URLConstant.SESSION_HEADER,sessionId)
                .post(requestBody)
                .url(url)
                .build();
        HttpUtil.sendOkHttpRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"连接失败，错误原因:"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respBody = response.body().string();
                Message msg = handler.obtainMessage();
                if(respBody.equals("Success")){
                    if(flg == 0){
                        msg.what = HANDLER_CONF_SUC;
                    }else{
                        msg.what = HANDLER_CANE_SUC;
                    }
                }else{
                    if(flg == 0){
                        msg.what = HANDLER_CONF_EOR;
                    }else{
                        msg.what = HANDLER_CANE_EOR;
                    }
                }
                handler.sendMessage(msg);
            }
        });
    }
}
