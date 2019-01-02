package com.yq.imageviewer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yq.imageviewer.Const;
import com.yq.imageviewer.MyApplication;
import com.yq.imageviewer.R;
import com.yq.imageviewer.bean.CoverItem;
import com.yq.imageviewer.utils.DeviceUtils;
import com.yq.imageviewer.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangqiang on 08/02/2018.
 */

public class CoverListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CoverItem> mCoverItems = new ArrayList<>();
    private ElementListener mElementListener;
    private int mColumnWidth;

    public CoverListAdapter(Context context) {
        mContext = context;
        mColumnWidth = DeviceUtils.getScreenWidth(MyApplication.getAppContext()) / Const.COLUMN;
    }

    public void setCoverItems(List<CoverItem> coverItems) {
        mCoverItems.clear();
        if (coverItems != null) {
            mCoverItems.addAll(coverItems);
        }
    }

    public void remove(int pos) {
        mCoverItems.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(mContext).inflate(R.layout.item_cover, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final MHolder holder = (MHolder) h;

        final CoverItem coverItem = mCoverItems.get(position);
        holder.iv.setController(Fresco.newDraweeControllerBuilder()
            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(coverItem.getCoverFile())).
                setResizeOptions(
                    new ResizeOptions(
                        mColumnWidth, mColumnWidth * coverItem.getImgOriginalHeight() / coverItem.getImgOriginalWidth())).build())
            .setOldController(holder.iv.getController())
            .setAutoPlayAnimations(true)
            .build());
        holder.iv.setAspectRatio(coverItem.getRatio());
        holder.tvTitle.setText(coverItem.getTitle());
        holder.tvDate.setText(coverItem.getPublishDate());
        holder.tvCount.setText(coverItem.getDirectory().list().length + "张");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mElementListener != null) {
                    mElementListener.onClick(coverItem);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mElementListener != null) {
                    mElementListener.onLongClick(coverItem, holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCoverItems.size();
    }

    public void setElementListener(ElementListener elementListener) {
        mElementListener = elementListener;
    }

    public static class MHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_cover_image) SimpleDraweeView iv;
        @BindView(R.id.item_cover_title) TextView tvTitle;
        @BindView(R.id.item_cover_date) TextView tvDate;
        @BindView(R.id.item_cover_count) TextView tvCount;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ElementListener {
        void onClick(CoverItem coverItem);
        void onLongClick(CoverItem coverItem, int pos);
    }
}
