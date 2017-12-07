package com.external.lifoto.adapter;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.external.lifoto.R;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.design.widget.ScaleImageView;

import java.util.ArrayList;

/**
 * Created by zuojie on 17-12-1.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private Fragment context;
    private ArrayList<PhotoItem> items = new ArrayList<>();

    public MainListAdapter(Fragment context, ArrayList<PhotoItem> items) {
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
//        notifyDataSetChanged();
        notifyItemRangeInserted(count, items.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mainlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoItem item = items.get(position);
//        holder.textView.setText(items.get(position).getDescription());
        holder.thumb.setInitSize(item.getWidth(), item.getHeight());
        Glide.with(context).load(Uri.parse(item.getUrls().small))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ScaleImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            thumb = itemView.findViewById(R.id.thumb);
        }
    }
}
