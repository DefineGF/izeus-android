package com.cjm.izeus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjm.izeus.R;
import com.cjm.izeus.bean.Transfer;
import com.cjm.izeus.listener.IRecyclerItemListener;

import java.util.List;

public class AccountTransferAdapter extends RecyclerView.Adapter<AccountTransferAdapter.ViewHolder> {
    private static final String TAG = "AccountTransferAdapter";

    private List<Transfer> transferList;
    private IRecyclerItemListener listener;
    private Context context = null;


    public AccountTransferAdapter(List<Transfer> transfers){
        this.transferList = transfers;
    }

    public void setRecyclerItemListener(IRecyclerItemListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        if(context == null){
            context = viewGroup.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.acc_transfer_item,viewGroup,false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        final Transfer transfer = transferList.get(i);
        viewHolder.tvItemID.setText(transfer.getId());
        viewHolder.tvItemTime.setText(transfer.getTn_time());
        viewHolder.tvItemMoney.setText(transfer.getAmount());
        viewHolder.tvItemName.setText(transfer.getFrom_name());
        viewHolder.tvItemState.setText(transfer.getState());
        String state = transfer.getState();
        if(state.equals("未完成")){//未完成
            viewHolder.tvItemState.setTextColor(context.getResources().getColor(R.color.colorTransferStateUnFinish));
        }else if(state.equals("已完成")){
            viewHolder.tvItemState.setTextColor(context.getResources().getColor(R.color.colorTransferStateFinish));
        }else{
            viewHolder.tvItemState.setTextColor(context.getResources().getColor(R.color.colorTransferStateFailure));
        }
        viewHolder.item.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e(TAG,"position is :"+i+" state = "+transfer.getState());
                listener.recyclerItemClick(transfer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transferList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView tvItemID,tvItemName,tvItemMoney,tvItemState,tvItemTime;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView;
            tvItemID = itemView.findViewById(R.id.transfer_item_id);
            tvItemName = itemView.findViewById(R.id.transfer_item_from_name);
            tvItemMoney = itemView.findViewById(R.id.transfer_item_money);
            tvItemState = itemView.findViewById(R.id.transfer_item_state);
            tvItemTime = itemView.findViewById(R.id.transfer_item_time);
        }
    }

}
