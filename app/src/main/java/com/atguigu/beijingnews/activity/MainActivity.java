package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.fragment.ContentFragment;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_TAG = "left_tag";
    public static final String MAIN_TAG = "main_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置左侧菜单
        initSlidingMenu();

        //初始化Fragment
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //添加或替换fragment
        ft.replace(R.id.fl_left, new  LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.fl_main,new ContentFragment(), MAIN_TAG);
        //提交事务
        ft.commit();
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

    /**
     * 得到左侧菜单Fragment
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        LeftMenuFragment leftFragment = (LeftMenuFragment) fm.findFragmentByTag(LEFT_TAG);
//
//        return leftFragment;

        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_TAG);
    }
}
