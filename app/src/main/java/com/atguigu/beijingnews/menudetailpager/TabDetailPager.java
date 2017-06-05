package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;

/**
 * Created by Administrator on 2017/6/5.
 */

public class TabDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private TextView textView;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        //Log.e("TAG","childrenBean.title" + childrenBean.getTitle());
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
        //设置数据
        textView.setText(childrenBean.getTitle());
    }
}
