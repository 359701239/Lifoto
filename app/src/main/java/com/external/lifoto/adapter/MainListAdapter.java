package com.external.lifoto.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.external.lifoto.DetailActivity;
import com.external.lifoto.R;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.design.widget.ElevationCardView;
import com.external.lifoto.design.widget.ScaleImageView;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-12-1.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<PhotoItem> items = new ArrayList<>();

    public MainListAdapter(Activity context, ArrayList<PhotoItem> items) {
        this.context = context;
        this.items = items;
    }

    public void setData(ArrayList<PhotoItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void insertData(ArrayList<PhotoItem> items) {
        int count = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(count, items.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mainlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PhotoItem item = items.get(position);
        holder.bottom.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.likes.setText(String.valueOf(item.getLikes()));
        holder.thumb.setInitSize(item.getWidth(), item.getHeight());
        Glide.with(context).load(item.getUrl_regular())
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.thumb.setImageDrawable(resource);
                    }
                });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.photo_more, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        return true;
                    }
                });
                popup.show();
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("item", items.get(holder.getAdapterPosition()));
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context,
                        Pair.create((View) holder.more, "e"),
                        Pair.create((View) holder.bottom, "c"),
                        Pair.create((View) holder.card, "b"),
                        Pair.create((View) holder.thumb, "a")).toBundle());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ElevationCardView card;
        private ScaleImageView thumb;
        private LinearLayout bottom;
        private ImageView more;
        private TextView likes;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            thumb = itemView.findViewById(R.id.item_thumb);
            bottom = itemView.findViewById(R.id.item_bottom);
            more = itemView.findViewById(R.id.more);
            likes = itemView.findViewById(R.id.likes);
        }
    }
}
