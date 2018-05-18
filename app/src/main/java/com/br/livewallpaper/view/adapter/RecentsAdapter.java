package com.br.livewallpaper.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.livewallpaper.R;
import com.br.livewallpaper.database.Recents;
import com.br.livewallpaper.model.WallpaperItem;
import com.br.livewallpaper.view.Common.Common;
import com.br.livewallpaper.view.activity.ListWallpaperActivity;
import com.br.livewallpaper.view.activity.ViewWallpaperActivity;
import com.br.livewallpaper.view.interfaces.ItemClickListener;
import com.br.livewallpaper.view.viewholder.ListWallpaperViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

    private Context context;
    private List<Recents> recents;

    public RecentsAdapter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @Override
    public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallpaper_item, parent, false);
        int height = parent.getMeasuredHeight()/2;
        itemView.setMinimumHeight(height);
        return new ListWallpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( final ListWallpaperViewHolder holder, final int position) {
        Picasso.with(context)
                .load(recents.get(position).getImageLink())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(recents.get(position).getImageLink())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.wallpaper, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Log.e("ERROR", "IMAGEM ERROR!");
                                    }
                                });

                    }

                });


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, ViewWallpaperActivity.class);
                WallpaperItem item = new WallpaperItem();
                item.setCategoryId(recents.get(position).getCategoryId());
                item.setImageLink(recents.get(position).getImageLink());
                Common.select_background = item;
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }
}
