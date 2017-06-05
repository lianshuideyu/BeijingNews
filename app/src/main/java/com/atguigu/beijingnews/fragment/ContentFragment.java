package com.atguigu.beijingnews.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.basefragment.BaseFragment;
import com.atguigu.beijingnews.basepager.BasePager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsPager;
import com.atguigu.beijingnews.pager.SettingPager;
import com.atguigu.beijingnews.view.NoViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {


    @InjectView(R.id.rg_main)
    RadioGroup rgMain;
    @InjectView(R.id.vp)
    NoViewPager vp;

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

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int idCheck) {
                switch (idCheck) {
                    case R.id.rb_home:
                        vp.setCurrentItem(0);

                        break;
                    case R.id.rb_news:
                        vp.setCurrentItem(1);
                        break;
                    case R.id.rb_setting:
                        vp.setCurrentItem(2);
                        break;
                }
            }
        });

        /**
         * 设置viewPager页面改变的监听，页面改变radiabutton也随之改变
         */
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pagers.get(position).initData();

                if(position == 1) {
                    isEnableSlidingMenu(context,SlidingMenu.TOUCHMODE_MARGIN);
                }else {
                    isEnableSlidingMenu(context,SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //解决初进页面首页没数据的问题
        pagers.get(0).initData();

        isEnableSlidingMenu(context,SlidingMenu.TOUCHMODE_NONE);
        //默认选择主页
        rgMain.check(R.id.rb_home);
    }

    /**
     * 是否让SlidingMenu可以滑动
     * @param context
     * @param touchmodeFullscreen
     */
    private static void isEnableSlidingMenu(Context context, int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    public NewsPager getNewsPager() {

        return (NewsPager) pagers.get(1);
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
            //basePager.initData();

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
