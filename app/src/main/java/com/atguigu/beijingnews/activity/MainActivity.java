package com.atguigu.beijingnews.activity;

import android.os.Bundle;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置左侧菜单
        initSlidingMenu();


    }

    private void initSlidingMenu() {
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu);

        SlidingMenu slidingMenu = getSlidingMenu();

        //设置模式：左侧+主页；左侧+主页+右侧；主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);

        //设置主页面占的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));
    }
}
