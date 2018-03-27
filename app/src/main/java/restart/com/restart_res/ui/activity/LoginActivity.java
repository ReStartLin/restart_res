package restart.com.restart_res.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import restart.com.restart_res.R;
import restart.com.restart_res.UserInfoHolder;
import restart.com.restart_res.bean.User;
import restart.com.restart_res.biz.UserBiz;
import restart.com.restart_res.net.CommonCallback;
import restart.com.restart_res.utils.T;

public class LoginActivity extends BaseActivity {
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;


    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra(KEY_USERNAME);
            if (username != null) {
                mEtUsername.setText(username);
            }
            String password = intent.getStringExtra(KEY_PASSWORD);
            if (password != null) {
                mEtPassword.setText(password);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initIntent();
        initEvent();
    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast("账号或者密码不能为空");
                    return;
                }
                startLoadingProgress();
                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User user) {
                        stopLoadingProgress();
                        T.showToast("登录成功");
                        UserInfoHolder.getInstance().setUser(user);
                        toOrderActivity();
                        finish();
                    }
                });
            }
        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }

    private void initView() {
        mEtUsername = findViewById(R.id.id_et_username);
        mEtPassword = findViewById(R.id.id_et_password);
        mBtnLogin =  findViewById(R.id.id_btn_login);
        mTvRegister = findViewById(R.id.id_btn_register);
    }

    private void toOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }
}
