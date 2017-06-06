package com.atguigu.beijingnews.basepager;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    public ImageButton ib_switch_list_grid;
    /**
     * 代表整个页面
     */
    public View rootView;

    public BasePager(Context context) {
        this.context = context;

        //初始化布局
        rootView = View.inflate(context, R.layout.base_pager, null);

        ib_switch_list_grid = (ImageButton) rootView.findViewById(R.id.ib_switch_list_grid);
        ButterKnife.inject(this, rootView);
    }

    /**
     * 子类要绑定数据的时候重写该方法
     */
    public void initData() {

    }

    @OnClick(R.id.ib_menu)
    public void onViewClicked() {
        //关-开
        ((MainActivity) context).getSlidingMenu().toggle();

    }
}
