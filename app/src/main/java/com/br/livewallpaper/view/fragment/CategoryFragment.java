package com.br.livewallpaper.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.livewallpaper.R;
import com.br.livewallpaper.databinding.FragmentCategoryBinding;
import com.br.livewallpaper.model.CategoriaItem;
import com.br.livewallpaper.view.Common.Common;
import com.br.livewallpaper.view.activity.ListWallpaperActivity;
import com.br.livewallpaper.view.interfaces.ItemClickListener;
import com.br.livewallpaper.view.viewholder.CategoriaViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    //Firebase
    FirebaseDatabase database;
    DatabaseReference categoriaBackground;

    //FirebaseUi Adapter
    FirebaseRecyclerOptions<CategoriaItem> options;
    FirebaseRecyclerAdapter<CategoriaItem, CategoriaViewHolder> adapter;

    //databind
    FragmentCategoryBinding binding;
    private static CategoryFragment INSTANCE = null;

    public CategoryFragment() {
        // Required empty public constructor

        database = FirebaseDatabase.getInstance();
        categoriaBackground = database.getReference(Common.STR_CATEGORY_BACKGROUND);

        options = new FirebaseRecyclerOptions.Builder<CategoriaItem>()
                .setQuery(categoriaBackground, CategoriaItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoriaItem, CategoriaViewHolder>(options) {
            @Override
            protected void onBindViewHolder( final CategoriaViewHolder holder, int position, final CategoriaItem model) {
                Picasso.with(getActivity())
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.mBackgroudImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
                                        .load(model.getImageLink())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.mBackgroudImage, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.e("ERROR_WFG", "Coldn't fetch image");
                                            }
                                        });
                            }
                        });

                holder.mCategoryName.setText(model.getNome());

                holder.setListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Common.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey(); //get key of item
                        Common.CATEGORY_SELECTED = model.getNome();
                        startActivity( new Intent( getActivity(), ListWallpaperActivity.class));

                    }
                });
            }

            @Override
            public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from( parent.getContext() )
                        .inflate(R.layout.layout_category_item,parent, false);
                return new CategoriaViewHolder(itemView);
            }
        };
    }


    public static CategoryFragment getInstance() {
       if(INSTANCE == null)
           INSTANCE =  new CategoryFragment();
       return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);

        binding.recyclerCategoria.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(),2);
        binding.recyclerCategoria.setLayoutManager(gridLayoutManager);

        setCategoria();
        return binding.getRoot();
    }

    private void setCategoria() {
        adapter.startListening();
        binding.recyclerCategoria.setAdapter(adapter);
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
