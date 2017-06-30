package com.codebear.keyboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codebear.keyboard.R;
import com.codebear.keyboard.data.EmoticonAdapterBean;
import com.codebear.keyboard.data.EmoticonsBean;

/**
 * description:
 * <p>
 * Created by CodeBear on 2017/6/30.
 */

public class CBEmoticonAdapter extends BaseAdapter {

    public interface OnItemClickListener {
        void onItemClick(EmoticonsBean data, int position, int page);
    }

    private Context mContext;
    private EmoticonAdapterBean mData;

    private double mItemHeightMaxRatio;
    private int mItemHeightMax;
    private int mItemHeightMin;
    private int mItemHeight;

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CBEmoticonAdapter(Context mContext, EmoticonAdapterBean mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mItemHeightMaxRatio = mData.getmItemHeightMaxRatio();
        this.mItemHeight = (int) mContext.getResources().getDimension(mData.getmItemHeight());
    }

    @Override
    public int getCount() {
        return mData.getmData().size();
    }

    @Override
    public Object getItem(int i) {
        return mData.getmData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_emoticon, null);
            viewHolder.rootView = convertView;
            viewHolder.llRoot = convertView.findViewById(R.id.ll_root);
            viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        showData(viewHolder, i);
        updateUI(viewHolder, viewGroup);

        return convertView;
    }

    private void showData(final ViewHolder viewHolder, final int position) {
        if (mData.getmData().get(position).getIconUri() != null) {
            Glide.with(mContext).load(mData.getmData().get(position).getIconUri()).asBitmap().dontAnimate().into
                    (viewHolder.ivIcon);

            viewHolder.rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }
                    return false;
                }
            });
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != itemClickListener) {
                        itemClickListener.onItemClick(mData.getmData().get(position), position, mData.getPage());
                    }
                }
            });
        }
        if (mData.isShowName()) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(mData.getmData().get(position).getName());
        }
    }

    private void updateUI(ViewHolder viewHolder, ViewGroup parent) {
        mItemHeightMax = this.mItemHeightMax != 0 ? this.mItemHeightMax : (int) (mItemHeight * mItemHeightMaxRatio);
        mItemHeightMin = this.mItemHeightMin != 0 ? this.mItemHeightMin : mItemHeight;
        int realItemHeight = ((View) parent.getParent()).getMeasuredHeight() / mData.getRow();
        realItemHeight = Math.min(realItemHeight, mItemHeightMax);
        realItemHeight = Math.max(realItemHeight, mItemHeightMin);
        viewHolder.ivIcon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                mItemHeight));
        viewHolder.llRoot.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                realItemHeight));
    }

    public static class ViewHolder {
        public View rootView;
        public LinearLayout llRoot;
        public ImageView ivIcon;
        public TextView tvTitle;
    }
}
