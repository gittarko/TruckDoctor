package com.juma.truckdoctor.js.activity;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.fragment.BaseWebFragment;

/**
 * Created by hedong on 16/8/9.
 * 底部导航栏选项卡定义
 */

public enum BottomTab {
    ORDERS(0,
            R.drawable.tab_icon_orders,
            "我的订单",
            BaseWebFragment.class),
    MESSAGE(1,
            R.drawable.tab_icon_message,
            "消息中心",
            BaseWebFragment.class),
    PRICES(2,
            R.drawable.tab_icon_pricelist,
            "价格表",
            BaseWebFragment.class),
    CUSTOM(3,
            R.drawable.tab_icon_customer,
            "客户验证码",
            BaseWebFragment.class),
    USER_CENTER(4,
            R.drawable.tab_icon_user,
            "我的",
            BaseWebFragment.class);

    private int index;
    private int resIcon;
    private String tabName;
    private Class<?> clz;

    BottomTab(int index, int resIconId, String resTabName, Class<?> clz) {
        this.index = index;
        this.resIcon = resIconId;
        this.tabName = resTabName;
        this.clz = clz;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String taabName) {
        this.tabName = taabName;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
