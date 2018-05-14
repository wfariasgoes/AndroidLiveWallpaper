package com.br.livewallpaper.view.viewholder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;

import com.br.livewallpaper.R;
import com.br.livewallpaper.view.interfaces.ItemClickListener;

public class ListWallpaperViewHolder extends ViewHolder implements View.OnClickListener{
    ItemClickListener itemClickListener;

    public ImageView wallpaper;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public ListWallpaperViewHolder(View itemView) {
        super(itemView);
        wallpaper = (ImageView) itemView.findViewById(R.id.imgWallpaperItem);

        wallpaper.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}
