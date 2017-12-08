package com.external.lifoto.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
        int color = Color.parseColor(item.getColor());
        if (Color.red(color) > 200 && Color.green(color) > 200 && Color.blue(color) > 200) {
            holder.bottom.setBackgroundColor(Color.parseColor("#C8C8C8"));
        } else {
            holder.bottom.setBackgroundColor(color);
        }
        holder.likes.setText(String.valueOf(item.getLikes()));
        holder.thumb.setInitSize(item.getWidth(), item.getHeight());
        Glide.with(context).load(Uri.parse(item.getUrl_small()))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.thumb);
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
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context, holder.thumb, "a").toBundle());

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
