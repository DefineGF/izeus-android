package com.cjm.izeus.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjm.izeus.R;
import com.cjm.izeus.adapter.RegularAccountAdapter;
import com.cjm.izeus.bean.AccountOverview;

import java.util.ArrayList;
import java.util.List;

public class BankRegularAccountFragment extends Fragment {

    private static List<AccountOverview> accOvList;
    private RecyclerView recyclerView;

    public static BankRegularAccountFragment newInstance(List<AccountOverview> accountOverviewList){
        BankRegularAccountFragment regularAccountFragment = new BankRegularAccountFragment();
        regularAccountFragment.initData(accountOverviewList);
        return new BankRegularAccountFragment();
    }

    private void initData(List<AccountOverview> accountOverviewList){
        accOvList = new ArrayList<>();
        for(AccountOverview accOv : accountOverviewList){
            if(accOv.getIscurrent() != 1){
                accOvList.add(accOv);
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_regular,container,false);
        recyclerView = view.findViewById(R.id.reg_acc_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RegularAccountAdapter regularAccountAdapter = new RegularAccountAdapter(accOvList);
        recyclerView.setAdapter(regularAccountAdapter);
    }
}
