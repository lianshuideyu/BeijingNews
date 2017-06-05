package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    /**
     * TabDetailPager的对应的数据
     */
    private List<NewsCenterBean.DataBean.ChildrenBean> datas;
    private ViewPager viewpager;

    private TabPageIndicator indicator;
    /**
     * TabDetailPager页面集合
     */
    private List<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.datas = dataBean.getChildren();
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_news_menu_detail,null);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);


        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //根据数据创建集合
        tabDetailPagers = new ArrayList<>();
        for(int i = 0; i < datas.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(context,datas.get(i)));
            Log.e("TAG","datas.get(i).getTitle()==" + datas.get(i).getTitle());
        }

        //设置适配器
        viewpager.setAdapter(new NewsMenuDetailPagerAdapter());
        indicator.setViewPager(viewpager);
    }

    private class NewsMenuDetailPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return datas.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers == null ? 0 : tabDetailPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view== object;
        }
    }
}
