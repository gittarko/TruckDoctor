package com.juma.truckdoctor.js.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.activity.MainWebActivity;
import com.juma.truckdoctor.js.api.Api;
import com.juma.truckdoctor.js.api.ApiUser;
import com.juma.truckdoctor.js.api.ApiResponse;
import com.juma.truckdoctor.js.base.BaseActivity;
import com.juma.truckdoctor.js.helper.FunctionVerifyCode;
import com.juma.truckdoctor.js.model.User;
import com.juma.truckdoctor.js.utils.AppUtils;
import com.juma.truckdoctor.js.utils.RegularUtils;
import com.juma.truckdoctor.js.widget.PopUpWindowAlertDialog;

import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * @author dong.he
 *
 * 登录界面
 *
 */
public class LoginActivity extends BaseActivity {
    private static final String TITLE_CENTER = "登录";
    // UI references.
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;
    private EditText mVerifyCode;
    private TextView mForgetPassword;
    private TextView mLoginSwitch;
    private LinearLayout mLayoutPwd;
    private LinearLayout mLayoutSms;
    private Button mVerifyCodeBtn;

    private View mProgressView;
    private View mLoginFormView;

    //登录方式定义
    private enum LoginType {
        ACCOUNT_LOGIN,  //使用帐号登录
        SMS_CODE_LOGIN  //还用短信验证码登录
    }
    private LoginType type = LoginType.ACCOUNT_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initLayoutView() {
        super.initLayoutView();

        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mVerifyCode = (EditText)findViewById(R.id.verify_code);

        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mForgetPassword = (TextView)findViewById(R.id.forget_password);
        mForgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPopUpWindow();
            }
        });

        mVerifyCodeBtn = (Button)findViewById(R.id.verify_code_button);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //设置登录方式
        mLayoutPwd = (LinearLayout)findViewById(R.id.ll_password_login);
        mLayoutSms = (LinearLayout)findViewById(R.id.ll_sms_login);
        mLoginSwitch = (TextView)findViewById(R.id.login_switch);
        updateLoginType();
    }

    /**
     * 初始化登录方式
     * 登录包含用密码登录和使用短信验证码登录
     */
    private void updateLoginType() {
        if(type == LoginType.ACCOUNT_LOGIN) {
            mLayoutSms.setVisibility(View.GONE);
            mLayoutPwd.setVisibility(View.VISIBLE);
            mVerifyCode.setText("");
            mLoginSwitch.setText(R.string.label_sms_login);
        }else {
            mLayoutSms.setVisibility(View.VISIBLE);
            mLayoutPwd.setVisibility(View.GONE);
            mPasswordView.setText("");
            mLoginSwitch.setText(R.string.label_pwd_login);
        }
    }

    @Override
    public String getTitleCenter() {
        return TITLE_CENTER;
    }


    @Override
    public OnClickListener getNavigationClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
    }


    /**
     * 登录遇到困难,触发弹窗允许用户联系客服协助解决登录问题
     */
    PopUpWindowAlertDialog.Builder builder;
    private void attemptPopUpWindow() {
        builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.label_contact), 16)
        .setMessage(Api.Customer_Service_Phone + "\n" +
                getResources().getString(R.string.label_contact_descripton), 14)
        .setPositiveButton(getResources().getString(R.string.action_call_phone),
                new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //调起电话拨打界面
                AppUtils.getPhoneCall(LoginActivity.this, Api.Customer_Service_Phone);
                builder.dimiss();
                builder = null;
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.dimiss();
                builder = null;
            }
        })
        .build();
    }


    /**
     * 开始登录操作
     */
    private void attemptLogin() {
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String password = "";

        if(isPhoneValid(phone)) {
            focusView = mPhoneView;
            cancel = true;
        }

        if(type == LoginType.ACCOUNT_LOGIN) {
            password = mPasswordView.getText().toString();
            // 检查密码输入合法性
            if (!isPasswordValid(password)) {
                focusView = mPasswordView;
                cancel = true;
            }
        }else {
            password = mVerifyCode.getText().toString();
            //检测验证码是否为空
            if(!isVerifyCodeValid(password)) {
                focusView = mVerifyCode;
                cancel = true;
            }
        }

        if (cancel) {
            //手机号或密码输入不合法,焦点定位到错误栏
            focusView.requestFocus();
        } else {
            //登录前显示加载等待效果,然后执行登录
            showProgress(true);
            doLogin(phone, password);
        }
    }

    /**
     * 验证码是否有效
     * @param password
     * @return
     */
    private boolean isVerifyCodeValid(String password) {
        if(TextUtils.isEmpty(password)) {
            mVerifyCode.setError(getResources().getString(R.string.error_empty_code));
            return false;
        }

        return true;
    }

    /**
     * 检查手机号是否输入有效
     * @param phone
     * @return
     */
    private boolean isPhoneValid(String phone) {
        //检查手机号输入合法性
        if(!RegularUtils.isMobileExact(phone)){
//            showToast(R.string.error_invalid_phone, Toast.LENGTH_SHORT);
            mPhoneView.setError(getResources().getString(R.string.error_invalid_phone));
            return false;
        }

        return true;
    }


    /**
     * <p>
     * 检查密码输入是否符合规则,可输入数字，英文字母大小写
     * 密码长度6个字符以上, 20个以内
     * </p>
     * @param password  密码
     */
    private boolean isPasswordValid(String password) {
        return Pattern.matches("^[\\w]{6,20}", password);
    }

    /**
     * 执行异步登录
     * @param phone 手机号
     * @param password 登录密码
     */
    private void doLogin(String phone, String password) {
        ApiUser.asyncLogin(phone, password, new ApiResponse<User>() {
            @Override
            public void onSuccess(User response) {
                showProgress(false);
                showToast("登录成功", Toast.LENGTH_SHORT);
                forwardHome();
            }

            @Override
            public void onError(Exception e) {
                showProgress(false);
                String msg = e.getMessage();
                showToast(msg, Toast.LENGTH_SHORT);
            }
        });
    }

    //跳转至应用首页
    private void forwardHome() {
        mLoginFormView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, MainWebActivity.class);
                intent.setData(getIntent().getData());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        }, 1000);//延时1s执行
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * 异步获取短信验证码
     * @param view
     */
    public void getVerifyCode(View view) {
//        String phone = mPhoneView.getText().toString();
//        if(!isPhoneValid(phone)) {
//            return;
//        }

        new FunctionVerifyCode(mVerifyCodeBtn).start();

//        ApiUser.getVerifyCode(phone, new ApiResponse<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject response) {
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 切换登录方式
     * @param view
     */
    public void switchLoginType(View view) {
        if(type == LoginType.ACCOUNT_LOGIN) {
            type = LoginType.SMS_CODE_LOGIN;
        }else {
            type = LoginType.ACCOUNT_LOGIN;
        }

        updateLoginType();
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mPhoneView.setAdapter(adapter);
    }

}
