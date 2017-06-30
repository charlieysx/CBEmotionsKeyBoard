package com.codebear.keyboard.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.codebear.keyboard.DefEmoticons;
import com.codebear.keyboard.R;
import com.codebear.keyboard.adapter.CBEmoticonsToolbarAdapter;
import com.codebear.keyboard.data.EmojiBean;
import com.codebear.keyboard.data.EmoticonsBean;
import com.codebear.keyboard.fragment.CBEmoticonFragment;
import com.codebear.keyboard.fragment.ICBFragment;
import com.codebear.keyboard.utils.ParseDataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * description:默认的表情集的view
 * <p>
 * Created by CodeBearon 2017/6/28.
 */

public class CBEmoticonsView extends FrameLayout {

    public interface OnEmoticonClickListener {
        void onEmoticonClick(EmoticonsBean emoticon, boolean isDel);
    }

    private View rootView;
    private ViewPager vpEmoticonsContent;
    private RecyclerView rcvEmoticonsToolbar;

    private FragmentManager fragmentManager;

    private CBEmoticonsToolbarAdapter emoticonsToolbarAdapter;

    private List<EmoticonsBean> emoticonsBeanList = new ArrayList<>();
    private List<ICBFragment> emoticonFragments = new ArrayList<>();

    private OnEmoticonClickListener listener;

    private int lastPosition = 0;
    private boolean click = false;

    public void setOnEmoticonClickListener(OnEmoticonClickListener listener) {
        this.listener = listener;
        for (ICBFragment fragment : emoticonFragments) {
            fragment.setOnEmoticonClickListener(listener);
        }
    }

    public CBEmoticonsView(Context context) {
        super(context);


        rootView = LayoutInflater.from(context).inflate(R.layout.cb_view_emoticons_default, this, false);
        addView(rootView);

    }

    public void init(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        initViewPager();
        initRecycleView();
    }

    private void initViewPager() {
        vpEmoticonsContent = rootView.findViewById(R.id.vp_emoticons_content);

        vpEmoticonsContent.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return emoticonFragments.get(position).getFragment();
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
                if(!click) {
                    if (lastPosition < position) {
                        emoticonFragments.get(position).setSeeItem(0);
                    } else if (lastPosition > position) {
                        emoticonFragments.get(position).setSeeItem(1);
                    }
                }
                click = false;
                lastPosition = position;
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
                click = true;
                vpEmoticonsContent.setCurrentItem(position, true);
            }
        });
    }

    public void addEmoticons(EmoticonsBean bean) {
        emoticonsToolbarAdapter.add(bean);
        emoticonsToolbarAdapter.notifyItemInserted(emoticonsBeanList.size());

        ICBFragment fragment = CBEmoticonFragment.newInstance();
        fragment.setEmoticonsBean(bean);
        fragment.setOnEmoticonClickListener(listener);
        emoticonFragments.add(fragment);
        vpEmoticonsContent.setOffscreenPageLimit(emoticonFragments.size());
        vpEmoticonsContent.getAdapter().notifyDataSetChanged();
    }

    public void addEmoticons(List<EmoticonsBean> beanList) {
        emoticonsToolbarAdapter.addAll(beanList);
        emoticonsToolbarAdapter.notifyItemRangeInserted(emoticonsBeanList.size() - beanList.size(), beanList.size());

        for (EmoticonsBean bean : beanList) {
            ICBFragment fragment = CBEmoticonFragment.newInstance();
            fragment.setOnEmoticonClickListener(listener);
            fragment.setEmoticonsBean(bean);
            emoticonFragments.add(fragment);
        }
        vpEmoticonsContent.getAdapter().notifyDataSetChanged();
    }

    public void addEmoticonsWithName(String name) {
        if ("default".equals(name)) {
            addEmoticons(getDefaultEmoticon());
        } else {
            EmoticonsBean bean = ParseDataUtils.parseDataFromFile(getContext(), name);
            if (null != bean) {
                for (EmoticonsBean b : bean.getEmoticonsBeanList()) {
                    b.setParentTag(name);
                }
                addEmoticons(bean);
            }
        }
    }

    public void addEmoticonsWithName(String[] nameList) {
        for (String name : nameList) {
            addEmoticonsWithName(name);
        }
    }

    private EmoticonsBean getDefaultEmoticon() {

        final ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);

        EmoticonsBean emoticonsBean = new EmoticonsBean();
        emoticonsBean.setId("default");
        emoticonsBean.setName(emojiArray.get(0).emoji);
        emoticonsBean.setIconUri(emojiArray.get(0).icon);
        emoticonsBean.setShowDel(true);
        emoticonsBean.setBigEmoticon(false);
        for (EmojiBean emojiBean : emojiArray) {
            EmoticonsBean temp = new EmoticonsBean();
            temp.setParentTag("default");
            temp.setParentId(emoticonsBean.getId());
            temp.setName(emojiBean.emoji);
            temp.setIconUri(emojiBean.icon);
            emoticonsBean.getEmoticonsBeanList().add(temp);
        }
        return emoticonsBean;
    }
}
