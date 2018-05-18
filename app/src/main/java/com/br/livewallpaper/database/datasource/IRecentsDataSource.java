package com.br.livewallpaper.database.datasource;

import com.br.livewallpaper.database.Recents;

import java.util.List;

import io.reactivex.Flowable;

public interface IRecentsDataSource {
    Flowable<List<Recents>> getAllRecents();
    void insertRecents(Recents... recents);
    void updateRecents(Recents... recents);
    void deleteRecents(Recents... recents);
    void deleteAllRecents();
}
