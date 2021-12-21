package com.hznu.lin.project.fragment.weather;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hznu.lin.project.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newTodayWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newTodayWeatherFragment extends Fragment {
    private View contextView;// 总视图
    private TabLayout tabLayout;
    private ViewPager viewpager;
    List<Fragment> fragmentList;
    String[] titles = {"今日","推荐"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_new_today_weather, container, false);
        tabLayout = contextView.findViewById(R.id.tab_layout_new);
        viewpager = contextView.findViewById(R.id.viewPage_new);
        return contextView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentList = new ArrayList<>();
        //第一个tab当前天气tab
        fragmentList.add(new TodayFragmentNew());
        //第二个tab推荐tab
        fragmentList.add(new RecommendFragment());
        //将tab与fragment 建立连接
        FragmentAdapter adapter = new FragmentAdapter(fragmentList, titles,getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }
}