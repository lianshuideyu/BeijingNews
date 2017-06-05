package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.basepager.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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

    private ImageButton ib_next;

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
        ib_next = (ImageButton) view.findViewById(R.id.ib_next);
        //点击箭头进入下一页
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            }
        });

        //监听页面的变化
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity main = (MainActivity) context;
                if(position == 0) {//当滑动到第一页时侧滑栏可以划开
                    main.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    main.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
