package com.juma.truckdoctor.js.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.juma.truckdoctor.js.R;

/**
 * Created by Administrator on 2016/8/4 0004.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        if(getLayoutResId() != 0) {
            setContentView(getLayoutResId());
        }

        initToolBar();
        initLayoutView();
    }

    //初始化toolBar
    protected void initToolBar() {
        Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar);
        if(toolBar != null) {
            TextView title = (TextView) findViewById(R.id.tv_title);
            title.setText(getTitleCenter());
            if (getNavigationIcon() != 0) {
                toolBar.setNavigationIcon(getNavigationIcon());
                toolBar.setNavigationOnClickListener(getNavigationClickListener());
            }
        }
    }

    //获取标题
    protected String getTitleCenter() {
        return "";
    }

    //获取导航栏图标
    protected int getNavigationIcon() {
        return 0;
    }

    //设置导航栏点击事件
    protected View.OnClickListener getNavigationClickListener() {
        return null;
    }

    //初始化布局控件
    protected void initLayoutView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private ProgressDialog progressDialog;

    public void showToast(final CharSequence text, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, text, duration).show();
            }
        });
    }

    public void showToast(final int resId, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, resId, duration).show();
            }
        });
    }

    //显示加载进度条
    public void showProgressDialog() {
        if (!isFinishing()) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(BaseActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.dialog_common_progress, null);
                progressDialog.setContentView(view);
            }
        }
    }

    /**
     * 自定义加载进度条提示
     * @param msg  消息提示
     * @param cancelable    是否允许外部可触摸
     */
    public void showProgressDialog(String msg, boolean cancelable) {
        if (!isFinishing()) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(BaseActivity.this);
            }
            progressDialog.setCancelable(cancelable);
            progressDialog.show();
            View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.dialog_common_progress, null);
            TextView tvContent = null;
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            if (!TextUtils.isEmpty(msg)) {
                tvContent.setText(msg);
            }
            progressDialog.setContentView(view);
        }
    }

    //隐藏进度条
    public void dismissProgressDialog() {
        if (!isFinishing() && progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
