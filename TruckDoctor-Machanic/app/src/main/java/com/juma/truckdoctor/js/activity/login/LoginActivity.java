package com.juma.truckdoctor.js.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.api.Api;
import com.juma.truckdoctor.js.api.ApiUser;
import com.juma.truckdoctor.js.api.HttpResponse;
import com.juma.truckdoctor.js.base.BaseActivity;
import com.juma.truckdoctor.js.model.User;
import com.juma.truckdoctor.js.utils.AppUtils;
import com.juma.truckdoctor.js.widget.PopUpWindowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static android.Manifest.permission.READ_CONTACTS;

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
    private TextView mForgetPassword;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initLayoutView() {
        super.initLayoutView();
        // Set up the login form.
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
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public String getTitleCenter() {
        return TITLE_CENTER;
    }


    @Override
    protected OnClickListener getNavigationClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
    }

    PopUpWindowAlertDialog.Builder builder;
    /**
     * 登录遇到困难,触发弹窗允许用户联系客服协助解决登录问题
     */
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
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone.
        if (TextUtils.isEmpty(phone) || AppUtils.isPhoneValid(phone)) {
            showToast(R.string.error_invalid_phone, Toast.LENGTH_SHORT);
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            showToast(R.string.error_invalid_password, Toast.LENGTH_SHORT);
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            doLogin(phone, password);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    //执行异步登录
    private void doLogin(String phone, String password) {
        ApiUser.asyncLogin(phone, password, new HttpResponse<User>() {
            @Override
            public void onSuccess(User response) {
                showProgress(false);
                //进入主页
                Intent intent = new Intent(LoginActivity.this, null);
                LoginActivity.this.startActivity(intent);
                finish();
            }

            @Override
            public void onError(Call request, Exception e) {
                showProgress(false);

            }
        });
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

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mPhoneView.setAdapter(adapter);
    }

}

