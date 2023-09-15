package com.cjm.izeus.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.cjm.izeus.R;

import java.util.Objects;

public class BottomDialog extends Dialog {

    private BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static BottomDialog newInstance(Context context,View view){
        BottomDialog bottomDialog = new BottomDialog(context, R.style.BottomDialog);
        bottomDialog.setContentView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);

        Objects.requireNonNull(bottomDialog.getWindow()).setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCancelable(true);
        return bottomDialog;
    }

}
