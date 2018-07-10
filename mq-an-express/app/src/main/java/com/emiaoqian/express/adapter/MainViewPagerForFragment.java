package com.emiaoqian.express.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/21.
 */

public   class MainViewPagerForFragment extends FragmentPagerAdapter {

    ArrayList<Fragment> fg=new ArrayList<>();

    //这里不用把具体的标签页的标题都写出来，只需要在构造方法中传入即可。然后具体的实现在具体的fragment页中实现即可
    //String[]  tabtitle=Myapplication.mcontext.getResources().getStringArray(R.array.tab_title);

    String[]  tabtitle;



    public MainViewPagerForFragment(FragmentManager fm, String[] s) {
        super(fm);
        this.tabtitle=s;
    }



    //在viewpager中添加fragment的方法。
    public   void  add(Fragment fragment){
       fg.add(fragment);
    }



    //待会试试这个不写会怎么样 。4.11
    @Override
    public Fragment getItem(int position) {
        return fg.get(position);
    }

    @Override
    public int getCount() {
        return fg.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle[position];
    }


    /***
     *
     * 重写这个空方法能够取消viewpager切换的时候销毁视图 4.18
     * @param container
     * @param position
     * @param object
     */
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//
//    }
}
