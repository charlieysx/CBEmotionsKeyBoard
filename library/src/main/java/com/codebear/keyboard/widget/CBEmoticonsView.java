package com.codebear.keyboard.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.codebear.keyboard.R;
import com.codebear.keyboard.adapter.CBEmoticonsToolbarAdapter;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.fragment.CBEmoticonFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * description:默认的表情集的view
 * <p>
 * Created by CodeBearon 2017/6/28.
 */

public class CBEmoticonsView extends FrameLayout {

    private View rootView;
    private ViewPager vpEmoticonsContent;
    private RecyclerView rcvEmoticonsToolbar;

    private FragmentManager fragmentManager;

    private CBEmoticonsToolbarAdapter emoticonsToolbarAdapter;

    private List<EmoticonsBean> emoticonsBeanList = new ArrayList<>();
    private SparseArray<Fragment> emoticonsMap = new SparseArray<>();
    private List<CBEmoticonFragment> emoticonFragments = new ArrayList<>();

    public CBEmoticonsView(Context context, FragmentManager fragmentManager) {
        super(context);

        this.fragmentManager = fragmentManager;

        rootView = LayoutInflater.from(context).inflate(R.layout.cb_view_emoticons_default, this, false);
        addView(rootView);

        initViewPager();
        initRecycleView();
    }

    private void initViewPager() {
        vpEmoticonsContent = rootView.findViewById(R.id.vp_emoticons_content);

        vpEmoticonsContent.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return emoticonFragments.get(position);
            }

            @Override
            public int getCount() {
                return emoticonFragments.size();
            }
        });
        vpEmoticonsContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                emoticonsToolbarAdapter.setSelectPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initRecycleView() {
        rcvEmoticonsToolbar = rootView.findViewById(R.id.rcv_emoticons_toolbar);
        rcvEmoticonsToolbar.setHasFixedSize(true);
        rcvEmoticonsToolbar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));

        emoticonsToolbarAdapter = new CBEmoticonsToolbarAdapter(getContext());
        rcvEmoticonsToolbar.setAdapter(emoticonsToolbarAdapter);

        emoticonsToolbarAdapter.setOnItemClickListener(new CBEmoticonsToolbarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //                Toast.makeText(getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
                vpEmoticonsContent.setCurrentItem(position, true);
            }
        });
    }

    public void addEmoticons(EmoticonsBean bean) {
        emoticonsToolbarAdapter.add(bean);
        emoticonsToolbarAdapter.notifyItemInserted(emoticonsBeanList.size());

        CBEmoticonFragment fragment = CBEmoticonFragment.newInstance();
        fragment.setEmoticonsBean(bean);
        emoticonFragments.add(fragment);
        vpEmoticonsContent.getAdapter().notifyDataSetChanged();
    }

    public void addEmoticons(List<EmoticonsBean> beanList) {
        emoticonsToolbarAdapter.addAll(beanList);
        emoticonsToolbarAdapter.notifyItemRangeInserted(emoticonsBeanList.size() - beanList.size(), beanList.size());

        for(EmoticonsBean bean : beanList) {
            CBEmoticonFragment fragment = CBEmoticonFragment.newInstance();
            fragment.setEmoticonsBean(bean);
            emoticonFragments.add(fragment);
        }
        vpEmoticonsContent.getAdapter().notifyDataSetChanged();
    }
}
