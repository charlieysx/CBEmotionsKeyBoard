package com.codebear.keyboard.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.codebear.keyboard.R;
import com.codebear.keyboard.adapter.CBEmoticonAdapter;
import com.codebear.keyboard.data.BigEmoticonAdapterBean;
import com.codebear.keyboard.data.EmoticonAdapterBean;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.widget.CBEmoticonsView;
import com.codebear.keyboard.widget.CBViewPagerIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/6/29.
 */

public class CBEmoticonFragment extends Fragment implements ICBFragment {

    private Context mContext;
    private View mRootView;

    ViewPager vpEmoticonContent;
    CBViewPagerIndicatorView cbvpiGuideIndicator;

    private EmoticonsBean emoticonsBean = new EmoticonsBean();

    private List<View> views = new ArrayList<>();

    private boolean hadLoadData = false;
    private boolean UserVisible = false;
    private boolean viewCreate = false;

    private int pageSize;
    private int size;
    private int count;

    private CBEmoticonsView.OnEmoticonClickListener listener;

    public static ICBFragment newInstance() {
        return new CBEmoticonFragment();
    }

    @Override
    public void setOnEmoticonClickListener(CBEmoticonsView.OnEmoticonClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
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
        viewCreate = true;
        if (!hadLoadData && UserVisible) {
            hadLoadData = true;
            initView();
        }
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

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UserVisible = true;
            if (!hadLoadData && viewCreate) {
                hadLoadData = true;
                initView();
            }
        }
    }

    private void initData() {

        size = emoticonsBean.getEmoticonsBeanList().size();
        count = emoticonsBean.getRol() * emoticonsBean.getRow();
        Log.i("data", emoticonsBean.getRol() + " -- " + emoticonsBean.getRow());
        if (emoticonsBean.isShowDel()) {
            int p = count - 1;
            while (p <= size) {
                emoticonsBean.getEmoticonsBeanList().add(p, new EmoticonsBean(true));
                p += count;
                size += 1;
            }
            if (size % count > 0) {
                for (int i = size; i < p; ++i) {
                    emoticonsBean.getEmoticonsBeanList().add(i, new EmoticonsBean());
                }
                emoticonsBean.getEmoticonsBeanList().add(p, new EmoticonsBean(true));
                p++;
                size = p;
            }
        }
        pageSize = size / count;
        if (size % count > 0) {
            pageSize++;
        }
    }

    private void initView() {
        for (int i = 0; i < pageSize; ++i) {
            View view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R
                    .layout.view_emoticon_gridview, null);
            GridView egvEmoticon = view.findViewById(R.id.egv_emoticon);
            egvEmoticon.setNumColumns(emoticonsBean.getRol());

            egvEmoticon.setMotionEventSplittingEnabled(false);
            egvEmoticon.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            egvEmoticon.setCacheColorHint(0);
            egvEmoticon.setSelector(new ColorDrawable(Color.TRANSPARENT));
            egvEmoticon.setVerticalScrollBarEnabled(false);

            final int left = i * count;
            int right = (i + 1) * count;
            if (right > size) {
                right = size;
            }

            EmoticonAdapterBean emoticonAdapterBean;
            if (emoticonsBean.isBigEmoticon()) {
                emoticonAdapterBean = new BigEmoticonAdapterBean(emoticonsBean.getEmoticonsBeanList().subList(left,
                        right));
            } else {
                emoticonAdapterBean = new EmoticonAdapterBean(emoticonsBean.getEmoticonsBeanList().subList(left,
                        right));
            }
            emoticonAdapterBean.setPage(i);
            emoticonAdapterBean.setRol(emoticonsBean.getRol());
            emoticonAdapterBean.setRow(emoticonsBean.getRow());
            emoticonAdapterBean.setShowName(emoticonsBean.isShowName());

            CBEmoticonAdapter adapter = new CBEmoticonAdapter(mContext, emoticonAdapterBean);
            egvEmoticon.setAdapter(adapter);
            adapter.setItemClickListener(new CBEmoticonAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(EmoticonsBean data, int position, int page) {
                    data.setParentId(emoticonsBean.getId());
                    data.setBigEmoticon(emoticonsBean.isBigEmoticon());
                    if (null != listener) {
                        listener.onEmoticonClick(data, data.isShowDel() && EmoticonsBean.DEL.equals(data.getId()));
                    }
                }
            });

            views.add(view);
        }
        vpEmoticonContent.getAdapter().notifyDataSetChanged();
        cbvpiGuideIndicator.setPageCount(pageSize);
    }

    @Override
    public void setEmoticonsBean(EmoticonsBean emoticonsBean) {
        this.emoticonsBean = emoticonsBean;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
