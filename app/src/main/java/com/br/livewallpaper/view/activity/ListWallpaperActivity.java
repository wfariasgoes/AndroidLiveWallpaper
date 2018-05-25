package com.br.livewallpaper.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.br.livewallpaper.R;
import com.br.livewallpaper.databinding.ActivityListWallpaperBinding;
import com.br.livewallpaper.model.WallpaperItem;
import com.br.livewallpaper.view.Common.Common;
import com.br.livewallpaper.view.WLPActivity;
import com.br.livewallpaper.view.interfaces.ItemClickListener;
import com.br.livewallpaper.view.viewholder.ListWallpaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ListWallpaperActivity extends WLPActivity {

    Query query;
    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder> adapter;

    ActivityListWallpaperBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_wallpaper);
        initBindings();
    }

    private void initBindings() {

        binding.toolbar.setTitle(Common.CATEGORY_SELECTED);
        setSupportActionBar( binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.recyclerListWallpaper.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerListWallpaper.setLayoutManager(gridLayoutManager);

        loadBackgroundList();
    }

    private void loadBackgroundList() {
        query = FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .orderByChild("categoryId").equalTo(Common.CATEGORY_ID_SELECTED);

        options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
                .setQuery(query,WallpaperItem.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position,final @NonNull WallpaperItem model) {
                    Picasso.with(getBaseContext())
                            .load(model.getImageLink())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(holder.wallpaper, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getBaseContext())
                                            .load(model.getImageLink())
                                            .error(R.drawable.ic_terrain_black_24dp)
                                            .into(holder.wallpaper, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                    Log.e("ERROR_LIST", "Não foi possível renderizar a imagem!");

                                                }
                                            });


                                }


                            });

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        sendActity(view,model,adapter, position);

                    }
                });
            }

            @NonNull
            @Override
            public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_wallpaper_item, parent, false);
                int height = parent.getMeasuredHeight()/5;
                itemView.setMinimumHeight(height);
                return new ListWallpaperViewHolder(itemView);
            }
        };

        adapter.startListening();
        binding.recyclerListWallpaper.setAdapter(adapter);

    }


    private void sendActity(View view, WallpaperItem model, FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder> adapter, int position){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, ViewWallpaperActivity.class);

            String transitionName = "wallpaper";
            View imgThumb = view.findViewById(R.id.imgWallpaperItem);
//            ViewCompat.setTransitionName(imgThumb, transitionName);

            // TRANSITIONS
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create(imgThumb, transitionName));

//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imgThumb, transitionName);

            Common.select_background = model;
            Common.select_background_key = this.adapter.getRef(position).getKey(); //get key of item
            startActivity(intent, options.toBundle());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }


    @Override
    public void onStop() {
        if (adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
