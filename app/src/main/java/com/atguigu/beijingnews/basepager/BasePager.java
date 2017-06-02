package com.atguigu.beijingnews.basepager;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class BasePager {

    public Context context;

    @InjectView(R.id.tv_title)
    public TextView tvTitle;
    @InjectView(R.id.ib_menu)
    public ImageButton ibMenu;
    @InjectView(R.id.fl_content)
    public FrameLayout flContent;

    /**
     * 代表整个页面
     */
    public View rootView;

    public BasePager(Context context) {
        this.context = context;

        //初始化布局
        rootView = View.inflate(context, R.layout.base_pager, null);
        ButterKnife.inject(this,rootView);
    }

    /**
     * 子类要绑定数据的时候重写该方法
     */
    public void initData(){

    }
}
