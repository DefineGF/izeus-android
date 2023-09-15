package com.cjm.izeus.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cjm.izeus.R;
import com.cjm.izeus.util.HttpUtil;
import com.cjm.izeus.util.PrefSaveAndRead;
import com.cjm.izeus.util.URLConstant;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private static final int HANDLER_SUC = 1;
    private static final int HANDLER_EOR = 0;
    private static final int HANDLER_INTERNET = -1;
    private EditText etName,etPsw;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_EOR:
                    Toast.makeText(MainActivity.this,"账户信息错误",Toast.LENGTH_SHORT).show();
                    break;
                case HANDLER_SUC:
                    Toast.makeText(MainActivity.this,"login success",Toast.LENGTH_SHORT).show();
                    Intent intentAccount = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intentAccount);
                    finish();
                    break;
                case HANDLER_INTERNET:
                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        etName = findViewById(R.id.login_et_name);
        etPsw  = findViewById(R.id.login_et_password);

        Button btnLogin = findViewById(R.id.login_btn_login);
        Button btnRegister = findViewById(R.id.login_btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        TextView tvTitle = findViewById(R.id.main_title);
        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog_inout,null);
                final EditText etIp = view.findViewById(R.id.alert_et_ip);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setView(view);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip = etIp.getText().toString();
                        Log.e(TAG,"get the ip is :"+ip);
                        URLConstant.setIpUrl(ip);
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn_login:
                String name = etName.getText().toString();
                String psw  = etPsw.getText().toString();

                final RequestBody requestBody = new FormBody.Builder()
                        .add("personal_account",name)
                        .add("password",psw)
                        .build();
                Request request = new Request.Builder()
                        .url(URLConstant.LOGIN_URL)
                        .post(requestBody)
                        .build();

                HttpUtil.sendOkHttpRequest(request, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message msg = handler.obtainMessage();
                        msg.what = HANDLER_INTERNET;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg = handler.obtainMessage();
                        if(response.body() != null){
                            String resBodyString = response.body().string();
                            if(!resBodyString.equals("Failure")){
                                msg.what = HANDLER_SUC;
                                Headers headers = response.headers();
                                List<String> cookies = headers.values("Set-Cookie");
                                String session = cookies.get(0);
                                String sessionId = session.substring(0,session.indexOf(";"));
                                PrefSaveAndRead.saveSession(MainActivity.this,sessionId);
                                PrefSaveAndRead.saveMsg(MainActivity.this,URLConstant.USER_MSG,
                                        URLConstant.USER_NAME_kEY,resBodyString);
                                Log.e(TAG,"save the session is :"+sessionId);
                            }else{
                                msg.what = HANDLER_EOR;
                            }
                        }else{
                            msg.what = HANDLER_EOR;
                        }
                        handler.sendMessage(msg);
                    }
                });
                break;
            case R.id.login_btn_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
