package com.onimaskesi.hmsaccountkitdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class MainActivity extends AppCompatActivity {

    TextView desc;
    TextView email;
    Button signBtn;

    private int LOGIN_RESULT = 333;

    AuthAccount huaweiAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        desc = findViewById(R.id.descTV);
        email = findViewById(R.id.emailTV);
        signBtn = findViewById(R.id.signBtn);

        huaweiAccount = null;

        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setEmail().setIdToken().createParams();
        AccountAuthService service = AccountAuthManager.getService(MainActivity.this, authParams);

        signBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(huaweiAccount == null){

                    startActivityForResult(service.getSignInIntent(), LOGIN_RESULT);

                } else {

                    huaweiAccount = null;
                    signInView();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_RESULT) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                huaweiAccount = authAccountTask.getResult();
                signOutView();
                Log.i("Login", "idToken:" + huaweiAccount.getIdToken());
            } else {
                Log.e("Login", "sign in failed : " +((ApiException)authAccountTask.getException()).getStatusCode());
            }
        }
    }

    void signInView(){
        desc.setText(getString(R.string.desc));
        email.setText("");
        email.setVisibility(View.INVISIBLE);
        signBtn.setText(getString(R.string.login_button));
    }

    void signOutView(){
        desc.setText(huaweiAccount.getDisplayName());
        email.setText(huaweiAccount.getEmail());
        email.setVisibility(View.VISIBLE);
        signBtn.setText(getString(R.string.logout_button));
    }
}
