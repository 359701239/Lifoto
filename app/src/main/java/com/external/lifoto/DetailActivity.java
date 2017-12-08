package com.external.lifoto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.external.lifoto.bean.PhotoItem;
import com.external.lifoto.design.widget.FitToolbar;
import com.external.lifoto.design.widget.ScaleImageView;

/**
 * Created by zuojie on 17-12-8.
 */

public class DetailActivity extends AppCompatActivity {

    private FitToolbar toolbar;
    private ScaleImageView thumb;
    private PhotoItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        toolbar = findViewById(R.id.toolbar);
        thumb = findViewById(R.id.thumb);

        init();
        initView();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView(){
        thumb.setInitSize(item.getWidth(), item.getHeight());
        Glide.with(this).load(Uri.parse(item.getUrl_small()))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumb);
    }
}
