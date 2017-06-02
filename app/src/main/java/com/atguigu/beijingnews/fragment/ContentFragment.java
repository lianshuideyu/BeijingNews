package com.atguigu.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.basefragment.BaseFragment;
import com.atguigu.beijingnews.basepager.BasePager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsPager;
import com.atguigu.beijingnews.pager.SettingPager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {


    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> pagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //设置viewPager的数据---适配器
        //准备数据
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));
        pagers.add(new NewsPager(context));
        pagers.add(new SettingPager(context));


        vp.setAdapter(new MyAdaper());

        //默认选择主页
        rgMain.check(R.id.rb_home);
    }




    class MyAdaper extends PagerAdapter {
        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;
            container.addView(rootView);
            basePager.initData();

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
