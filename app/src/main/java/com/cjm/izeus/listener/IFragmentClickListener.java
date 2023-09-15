package com.cjm.izeus.listener;

public interface IFragmentClickListener {
    int FRAGMENT_COMPANY = 0;
    int FRAGMENT_ACCOUNT = 1;
    int FRAGMENT_MEMBER  = 2;
    public void fragmentOneClick(int fragId);
    public void fragmentTwoClick(int fragId, int count);
    public void fragmentThreeClick();
}
