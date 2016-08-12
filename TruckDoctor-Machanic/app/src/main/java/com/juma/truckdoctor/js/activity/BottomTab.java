package com.juma.truckdoctor.js.activity;

import com.juma.truckdoctor.js.R;
import com.juma.truckdoctor.js.fragment.BaseWebFragment;
import com.juma.truckdoctor.js.fragment.CustomAuthWebFragment;
import com.juma.truckdoctor.js.fragment.MessageListWebFragment;
import com.juma.truckdoctor.js.fragment.OrderListWebFragment;
import com.juma.truckdoctor.js.fragment.PriceListWebFragment;
import com.juma.truckdoctor.js.fragment.UserCenterWebFragment;

/**
 * Created by hedong on 16/8/9.
 * 底部导航栏选项卡定义
 */

public enum BottomTab {
    ORDERS(0,
            R.drawable.tab_icon_orders,
            R.string.tab_my_order,
            OrderListWebFragment.class),
    MESSAGE(1,
            R.drawable.tab_icon_message,
            R.string.tab_my_message,
            MessageListWebFragment.class),
    PRICES(2,
            R.drawable.tab_icon_pricelist,
            R.string.tab_my_pricelist,
            PriceListWebFragment.class),
    CUSTOM(3,
            R.drawable.tab_icon_customer,
            R.string.tab_custom_verifycode,
            CustomAuthWebFragment.class),
    USER_CENTER(4,
            R.drawable.tab_icon_user,
            R.string.tab_user_center,
            UserCenterWebFragment.class);

    private int index;
    private int resIcon;
    private int tabName;
    private Class<?> clz;

    BottomTab(int index, int resIconId, int resTabName, Class<?> clz) {
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

    public int getTabName() {
        return tabName;
    }

    public void setTabName(int taabName) {
        this.tabName = taabName;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
