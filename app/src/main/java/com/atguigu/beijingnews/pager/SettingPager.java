package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.basepager.BasePager;

/**
 * Created by Administrator on 2017/6/2.
 */

public class SettingPager extends BasePager {


    public SettingPager(Context context) {
        super(context);

    }

    @Override
    public void initData() {
        super.initData();
        //把视图绑定到视图上
        Log.e("TAG","SettingPager---initData");
        //设置标题
        tvTitle.setText("设置");

        //创建子类视图
        TextView textView = new TextView(context);
        textView.setText("设置的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        flContent.addView(textView);
    }
}
