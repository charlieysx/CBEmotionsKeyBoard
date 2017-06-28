package com.codebear.keyboard.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.codebear.keyboard.R;
import com.codebear.keyboard.adapter.CBEmoticonsToolbarAdapter;
import com.codebear.keyboard.data.EmoticonsBean;

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

    private CBEmoticonsToolbarAdapter emoticonsToolbarAdapter;

    private List<EmoticonsBean> emoticonsBeanList = new ArrayList<>();
    private SparseArray<Fragment> emoticonsMap = new SparseArray<>();

    public CBEmoticonsView(Context context) {
        super(context);

        rootView = LayoutInflater.from(context).inflate(R.layout.cb_view_emoticons_default, this, false);
        addView(rootView);

        initViewPager();
        initRecycleView();

        addData();
    }

    private void initViewPager() {
        vpEmoticonsContent = rootView.findViewById(R.id.vp_emoticons_content);
    }

    private void initRecycleView() {
        rcvEmoticonsToolbar = rootView.findViewById(R.id.rcv_emoticons_toolbar);
        rcvEmoticonsToolbar.setHasFixedSize(true);
        rcvEmoticonsToolbar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        emoticonsToolbarAdapter = new CBEmoticonsToolbarAdapter(getContext());
        rcvEmoticonsToolbar.setAdapter(emoticonsToolbarAdapter);

        emoticonsToolbarAdapter.setOnItemClickListener(new CBEmoticonsToolbarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
//                vpEmoticonsContent.setCurrentItem(position, true);
            }
        });
    }

    private void addData() {
        for(int i = 0;i < 15;++i) {
            emoticonsBeanList.add(new EmoticonsBean());
        }
        emoticonsToolbarAdapter.addAll(emoticonsBeanList);
        emoticonsToolbarAdapter.notifyDataSetChanged();
    }
}
