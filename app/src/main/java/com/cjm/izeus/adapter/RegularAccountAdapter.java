package com.cjm.izeus.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjm.izeus.R;
import com.cjm.izeus.bean.AccountOverview;

import java.util.List;

public class RegularAccountAdapter extends RecyclerView.Adapter<RegularAccountAdapter.ViewHolder> {
    private List<AccountOverview> accOvList;

    public RegularAccountAdapter(List<AccountOverview> accountOverviewList){
        this.accOvList = accountOverviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.regular_acc_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AccountOverview accOv =accOvList.get(i);
        viewHolder.tvAcc.setText(accOv.getCreditcard());
        viewHolder.tvType.setText(accOv.getType());
        viewHolder.tvName.setText(accOv.getByname());
        viewHolder.tvMoney.setText(accOv.getCurrency());
        viewHolder.tvRate.setText(accOv.getRate());
    }

    @Override
    public int getItemCount() {
        return accOvList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvAcc,tvType,tvName,tvMoney,tvRate;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAcc = itemView.findViewById(R.id.reg_item_tv_acc);
            tvType = itemView.findViewById(R.id.reg_item_tv_type);
            tvName = itemView.findViewById(R.id.reg_item_tv_name);
            tvMoney = itemView.findViewById(R.id.reg_item_tv_money_type);
            tvRate = itemView.findViewById(R.id.reg_item_tv_rate);
        }
    }
}
