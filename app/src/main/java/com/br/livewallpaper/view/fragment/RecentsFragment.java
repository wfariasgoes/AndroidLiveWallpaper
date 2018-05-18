package com.br.livewallpaper.view.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.livewallpaper.R;
import com.br.livewallpaper.database.Recents;
import com.br.livewallpaper.database.datasource.RecentRepository;
import com.br.livewallpaper.database.localDatabase.LocalDatabase;
import com.br.livewallpaper.database.localDatabase.RecentsDataSource;
import com.br.livewallpaper.view.adapter.RecentsAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class RecentsFragment extends Fragment {

    List<Recents> recents;
    RecentsAdapter adapter;
    Context context;
//    FragmentRecentsBinding binding;
    RecyclerView recyclerRecente;

    //Room database
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    private static RecentsFragment INSTANCE = null;

    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        this.context = context;

        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(context);
        recentRepository = RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));
    }

    public static RecentsFragment getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new RecentsFragment(context);
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recents, container, false);
        View view = inflater.inflate(R.layout.fragment_recents,container, false);

        recyclerRecente = (RecyclerView) view.findViewById(R.id.recyclerRecente);
        recyclerRecente.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(),2);
        recyclerRecente.setLayoutManager(gridLayoutManager);

        recents = new ArrayList<>();

        adapter = new RecentsAdapter(context, recents);
        recyclerRecente.setAdapter(adapter);

        if (recents != null){
            for (Recents r: recents ) {
                Log.v("VERBOSE", r.getCategoryId());
            }
        }

        loadRecents();
        return view;
    }

    private void initView() {


    }

    private void loadRecents() {
        Disposable disposable =  recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetAllRecentSuccess(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR_LOAD_RECENTS", throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void onGetAllRecentSuccess(List<Recents> recents) {
            recents.clear();
            recents.addAll(recents);
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
