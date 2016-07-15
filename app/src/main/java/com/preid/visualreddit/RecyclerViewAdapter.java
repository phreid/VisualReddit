package com.preid.visualreddit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ClickableViewHolder> {
    public static final int VIEW_GRID = 0;
    public static final int VIEW_LIST = 1;

    private Context mContext;
    private List<RedditResponse.RedditPostData> mPostList;
    private int mViewType;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public class ClickableViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView mImageView;
        OnItemClickListener mOnItemClickListener;

        public ClickableViewHolder(View itemView) {
            super(itemView);
        }

        public void setOnClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public class GridViewHolder extends ClickableViewHolder {

        public GridViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.tile_image_view);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setOnClickListener(this);
        }
    }

    public class ListViewHolder extends ClickableViewHolder {
        TextView mTextView;

        public ListViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.list_image_view);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setOnClickListener(this);

            mTextView = (TextView) itemView.findViewById(R.id.list_text_view);
        }
    }

    public RecyclerViewAdapter(Context context, int viewType) {
        mContext = context;
        mPostList = new ArrayList<>();
        mViewType = viewType;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        ClickableViewHolder holder;

        if (mViewType == VIEW_GRID) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tile, parent, false);

            holder = new GridViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);

            holder = new ListViewHolder(itemView);
        }

        holder.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(mContext, ImageZoomActivity.class);

                RedditResponse.RedditPostData data = mPostList.get(position);

                String url = data.getUrl();
                String title = data.getTitle();

                Log.d("test", data.getUrl());

                intent.putExtra("url", url);
                intent.putExtra("title", title);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        RedditResponse.RedditPostData post = mPostList.get(position);
        String url;

        if (holder instanceof ListViewHolder) {
            String title = post.getTitle();
            ((ListViewHolder) holder).mTextView.setText(title);
            url = post.getLowRes();
        } else {
            url = post.getThumbnail();
        }

        Picasso.with(mContext)
                .load(url)
                .error(R.drawable.loading)
                .into(holder.mImageView);

//        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Bitmap overlayedBitmap = applyOverlay(resource, R.drawable.album);
//
//                holder.mImageView.setImageBitmap(overlayedBitmap);
//            }
//        };
//
//        Glide.with(mContext)
//                .load(thumbnailURL)
//                .asBitmap()
//                .error(R.drawable.loading)
//                .into(target);
    }

//    public Bitmap applyOverlay(Bitmap original, int overlayResId) {
//        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
//
//        Canvas canvas = new Canvas(result);
//        Bitmap overlay = BitmapFactory.decodeResource(
//                mContext.getResources(), overlayResId);
//
//        canvas.drawBitmap(original, 0, 0, null);
//        canvas.drawBitmap(overlay, original.getWidth() / 2, original.getHeight() / 2, null);
//
//        return result;
//    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public void add(RedditResponse.RedditPostData post) {
        mPostList.add(post);
    }

    public void add(List<RedditResponse.RedditPostData> posts) {
        for (int i = 0; i < posts.size(); i++) {
            mPostList.add(posts.get(i));
        }

        notifyDataSetChanged();
    }

    public void clear() {
        mPostList.clear();
        notifyDataSetChanged();
    }
}
