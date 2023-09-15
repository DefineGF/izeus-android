package com.cjm.izeus.util;

public class URLConstant {
    private static String IP = "http://172.20.10.4:8080";
    private static String IP_URL = IP + "/pay_system";

    public static final String COMPANY_URL          = IP_URL + "/applyAccountServletForAPP";
    public static final String ACCOUNT_URL          = IP_URL + "/applyAccountServlet1ForAPP";
    public static final String MEMBER_URL           = IP_URL + "/RegisterResultServletForAPP";
    public static final String LOGIN_URL            = IP_URL + "/loginServletForAPP";
    public static final String GET_BANK_CARD_URL    = IP_URL + "/showAccountOverviewServletForAPP";
    public static final String GET_ACCOUNT_MSG_URL  = IP_URL + "/showAccountInfoServletForAPP";
    public static final String LUNCH_TRANSFER_URL   = IP_URL + "/lunchTransferServletForAPP";
    public static final String TRANSFER_RECORD_URL  = IP_URL + "/showTransferHistoryServletForAPP";
    public static final String CONF_LUNCH_TF_ID     = IP_URL + "/joinTransferResultServletForAPP";
    public static final String CNCL_LUNCH_TF_ID     = IP_URL + "/endTransferServletForAPP";

    public static final String SESSION_NAME = "Session";
    public static final String SESSION_KEY  = "SessionID";
    public static final String SESSION_HEADER = "cookie";

    public static final String USER_MSG      = "user_msg";
    public static final String USER_NAME_kEY = "user_name";

    public static void setIpUrl(String ip){
        IP = ip;
    }

}
