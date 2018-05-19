package com.br.livewallpaper.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.br.livewallpaper.R;
import com.br.livewallpaper.databinding.FragmentTrendingBinding;
import com.br.livewallpaper.model.CategoriaItem;
import com.br.livewallpaper.model.WallpaperItem;
import com.br.livewallpaper.view.Common.Common;
import com.br.livewallpaper.view.activity.ListWallpaperActivity;
import com.br.livewallpaper.view.activity.ViewWallpaperActivity;
import com.br.livewallpaper.view.interfaces.ItemClickListener;
import com.br.livewallpaper.view.viewholder.ListWallpaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class TrendingFragment extends Fragment {

    FragmentTrendingBinding binding;

    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem,ListWallpaperViewHolder> adapter;

    private static TrendingFragment INSTANCE = null;

    public TrendingFragment() {
        // Required empty public constructor

        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.STR_WALLPAPER);

        Query query = categoryBackground.orderByChild("viewCount")
                .limitToLast(10); //get 10 intem have biggest count

        options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
                .setQuery(query, WallpaperItem.class)
                .build();

    }

    public static TrendingFragment getInstance() {
      if (INSTANCE == null)
          INSTANCE = new TrendingFragment();
      return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false);
        loadFirebaseAdapter();
        binding.recyclerTreinding.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        //Como o Firebase retorna a lista de classificação crescente, por isso precisamos inverter a exibição Recyclerview para exibir o maior item primeiro
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        binding.recyclerTreinding.setLayoutManager(linearLayoutManager);

        loadTreindingList();
        return binding.getRoot();
    }


    private void loadFirebaseAdapter() {
        adapter = new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final WallpaperItem model) {
                Picasso.with(getActivity())
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.wallpaper, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
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
                        Intent intent = new Intent(  getActivity(), ViewWallpaperActivity.class);
                        Common.select_background = model;
                        Common.select_background_key = adapter.getRef(position).getKey(); //get key of item
                        startActivity( intent);
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
    }

    private void loadTreindingList() {
        adapter.startListening();
        binding.recyclerTreinding.setAdapter(adapter);
    }



    @Override
    public void onStart() {
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



}
