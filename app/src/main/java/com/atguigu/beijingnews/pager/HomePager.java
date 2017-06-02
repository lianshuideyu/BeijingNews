package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.basepager.BasePager;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HomePager extends BasePager {


    public HomePager(Context context) {
        super(context);

    }

    @Override
    public void initData() {
        super.initData();
        //把视图绑定到视图上

        //设置标题
        tvTitle.setText("主页");

        //创建子类视图
        TextView textView = new TextView(context);
        textView.setText("主页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        flContent.addView(textView);
    }
}
