package com.cjm.izeus.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cjm.izeus.R;
import com.cjm.izeus.bean.Member;

import java.util.List;

public class MemberInputAdapter extends RecyclerView.Adapter<MemberInputAdapter.ViewHolder>{

    private static final String TAG = "MemberInputAdapter";
    private List<Member> memberList;


    public MemberInputAdapter(List<Member> memberList){
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        Member member = memberList.get(i);
        viewHolder.tvMemberLabel.setText("成员" + member.getMemberLabel());
        viewHolder.etMbName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberList.get(i).setMemberName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }

        });
        viewHolder.etMbAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberList.get(i).setMemberAccount(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.etMbId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberList.get(i).setMemberId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.etMbContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberList.get(i).setMemberContact(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMemberLabel;
        EditText etMbName,etMbAccount,etMbId,etMbContact;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberLabel = itemView.findViewById(R.id.tv_member_label);
            etMbName      = itemView.findViewById(R.id.et_member_name);
            etMbAccount   = itemView.findViewById(R.id.et_member_account);
            etMbId        = itemView.findViewById(R.id.et_member_id);
            etMbContact   = itemView.findViewById(R.id.et_member_contact);
        }

    }


    public String getMsg(){
        JSONArray jsonArray = (JSONArray)JSON.toJSON(memberList);
        Log.e(TAG,"get the json is :" + jsonArray.toJSONString());
        return jsonArray.toJSONString();

    }


}
