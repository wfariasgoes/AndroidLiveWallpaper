package com.br.livewallpaper.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.livewallpaper.R;
import com.br.livewallpaper.view.interfaces.ItemClickListener;

public class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mCategoryName;
    public ImageView mBackgroudImage;


    ItemClickListener listener;

    public CategoriaViewHolder(View itemView) {
        super(itemView);
        mBackgroudImage = (ImageView) itemView.findViewById(R.id.imgWallpaper);
        mCategoryName = (TextView) itemView.findViewById(R.id.tvName);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition());
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
}
