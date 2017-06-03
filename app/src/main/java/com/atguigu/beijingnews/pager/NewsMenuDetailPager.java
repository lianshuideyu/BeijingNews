package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.basepager.MenuDetailBasePager;

/**
 * Created by Administrator on 2017/6/3.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        //创建子类的视图
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("新闻详情页面的内容");
    }
}