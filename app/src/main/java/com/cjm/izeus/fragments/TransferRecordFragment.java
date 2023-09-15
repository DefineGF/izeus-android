package com.cjm.izeus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjm.izeus.R;
import com.cjm.izeus.adapter.AccountTransferAdapter;
import com.cjm.izeus.bean.Transfer;
import com.cjm.izeus.listener.IRecyclerItemListener;
import com.cjm.izeus.widget.BottomDialog;

import java.util.List;

public class TransferRecordFragment extends Fragment implements IRecyclerItemListener {
    private static final String TAG = "TransferRecordFragment";
    private List<Transfer> transferList;


    public static TransferRecordFragment newInstance(List<Transfer> transferList){
        Log.e(TAG,"run the newInstance");
        TransferRecordFragment recordFragment = new TransferRecordFragment();
        recordFragment.setData(transferList);
        return recordFragment;
    }


    private void setData(List<Transfer> transfers){
        this.transferList = transfers;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"run the onCreateView");
        View view = inflater.inflate(R.layout.fragment_transfer_record,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.frag_tf_record_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AccountTransferAdapter transferAdapter = new AccountTransferAdapter(transferList);
        transferAdapter.setRecyclerItemListener(this);
        recyclerView.setAdapter(transferAdapter);
        return view;
    }

    public void dataUpdate(List<Transfer> transfers){
        this.transferList = transfers;
        //transferAdapter.notifyDataSetChanged();
    }

    @SuppressLint({"InflateParams", "ResourceAsColor"})
    @Override
    public void recyclerItemClick(Transfer record) {
        View dialogTranDetailView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_content_no_btn,null);
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

        BottomDialog bottomDialog = BottomDialog.newInstance(getContext(), dialogTranDetailView);
        bottomDialog.show();
    }
}
