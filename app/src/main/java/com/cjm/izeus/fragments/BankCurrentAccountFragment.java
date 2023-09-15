package com.cjm.izeus.fragments;

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

import com.cjm.izeus.R;
import com.cjm.izeus.adapter.CurrentAccountAdapter;
import com.cjm.izeus.bean.AccountOverview;

import java.util.ArrayList;
import java.util.List;

public class BankCurrentAccountFragment extends Fragment {
    private static final String TAG = "BankCurAccFragment";
    private static List<AccountOverview> accountMsgList;
    private RecyclerView recyclerView;

    public static BankCurrentAccountFragment newInstance(List<AccountOverview> accountOverviewList){
        BankCurrentAccountFragment bankCurrentAccountFragment = new BankCurrentAccountFragment();
        bankCurrentAccountFragment.initData(accountOverviewList);
        return bankCurrentAccountFragment;
    }

    private void initData(List<AccountOverview> overviewList){
        accountMsgList = new ArrayList<>();
        for(AccountOverview overview:overviewList){
            if(overview.getIscurrent() == 1){
                accountMsgList.add(overview);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bank_current,container,false);
        recyclerView = view.findViewById(R.id.cur_acc_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG,"run the onActivityCreated");
        Log.d(TAG,"the length is :"+accountMsgList.size());
        CurrentAccountAdapter accountAdapter = new CurrentAccountAdapter(accountMsgList);
        recyclerView.setAdapter(accountAdapter);
    }
}
