package com.juma.truckdoctor.js.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hedong on 16/8/8.
 * 验证码获取功能类
 */

public class FunctionVerifyCode {
    private WeakReference<Context> context;
    private View mTextView;

    private static final int INIT_DATA = 0x10;
    private static final int UPDATE_DATA = 0x11;
    private static final int DONE_DATA = 0x12;
    private static final int MAX_TIME = 30;
    private int currentTime = MAX_TIME;

    private TimerHandler handler;
    private Timer timer;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if(currentTime == 0) {
                cancel();
                handler.obtainMessage(DONE_DATA)
                        .sendToTarget();
                return;
            }

            //倒计时
            currentTime--;
            handler.obtainMessage(UPDATE_DATA)
                    .sendToTarget();

        }
    };

    public FunctionVerifyCode(View textView) {
        this.context = new WeakReference<Context>(textView.getContext());
        this.mTextView = textView;
        handler = new TimerHandler();
    }

    /**
     * 开启定时器任务
     * 执行获取验证码倒计时
     */
    public void start() {
        if(timer != null) {
            cancel();
        }

        handler.obtainMessage(INIT_DATA)
                .sendToTarget();

        timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }

    /**
     * 停止验证码倒计时
     */
    public void cancel() {
        timer.cancel();
        task.cancel();
        timer = null;
        currentTime = MAX_TIME;
    }

    private class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_DATA:
                    setText(currentTime + "s");
                    mTextView.setEnabled(false);
                    mTextView.setAlpha(0.5f);
                    break;
                case UPDATE_DATA:
                    setText(currentTime + "s");
                    break;
                case DONE_DATA:
                    //倒计时已归零,可以重新触发倒计时
                    setText("重新获取");
                    mTextView.setEnabled(true);
                    mTextView.setAlpha(1.0f);
                    break;
            }
        }
    }

    //刷新验证码按钮显示
    private void setText(String text) {
        if(mTextView instanceof TextView) {
            ((TextView)mTextView).setText(text);
        } else if(mTextView instanceof Button) {
            ((Button)mTextView).setText(text);
        }
    }
}
