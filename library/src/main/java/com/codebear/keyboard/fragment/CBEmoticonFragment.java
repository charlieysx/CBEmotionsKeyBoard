package com.codebear.keyboard.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebear.keyboard.R;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.widget.CBViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/6/29.
 */

public class CBEmoticonFragment extends Fragment {

    private Context mContext;
    private View mRootView;

    ViewPager vpEmoticonContent;
    CBViewPagerIndicatorView cbvpiGuideIndicator;

    private EmoticonsBean emoticonsBean = new EmoticonsBean();

    private List<View> views = new ArrayList<>();

    public static CBEmoticonFragment newInstance() {
        return new CBEmoticonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_emoticons, container, false);
        mContext = getContext();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpEmoticonContent = mRootView.findViewById(R.id.vp_emoticon_content);
        cbvpiGuideIndicator = mRootView.findViewById(R.id.cbvpi_guide_indicator);

        initViewPager();
        initData();
    }

    private void initViewPager() {
        vpEmoticonContent.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));

                return views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }
        });
        cbvpiGuideIndicator.setViewPager(vpEmoticonContent);
    }

    private void initData() {
        if(views.size() > 0) {
            return;
        }
        int size = emoticonsBean.getEmoticonsBeanList().size();
        int count = emoticonsBean.getRol() * emoticonsBean.getRow();
        int pageSize = size / count;
        if(size % count > 0) {
            pageSize++;
        }
        int[] colors = {Color.RED, Color.BLUE, Color.DKGRAY, Color.CYAN};
        for(int i = 0;i < pageSize;++i) {
            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cb_view_func_emoticons, null);
            Random random = new Random();
            view.setBackgroundColor(colors[random.nextInt(colors.length)]);
            views.add(view);
        }
        vpEmoticonContent.getAdapter().notifyDataSetChanged();
        cbvpiGuideIndicator.setPageCount(pageSize);
    }

    public void setEmoticonsBean(EmoticonsBean emoticonsBean) {
        this.emoticonsBean = emoticonsBean;
    }
}
