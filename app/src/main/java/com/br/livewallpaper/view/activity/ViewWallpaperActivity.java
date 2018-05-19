package com.br.livewallpaper.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.br.livewallpaper.R;
import com.br.livewallpaper.database.Recents;
import com.br.livewallpaper.database.datasource.RecentRepository;
import com.br.livewallpaper.database.localDatabase.LocalDatabase;
import com.br.livewallpaper.database.localDatabase.RecentsDataSource;
import com.br.livewallpaper.databinding.ActivityViewWallpaperBinding;
import com.br.livewallpaper.helper.SaveImageHelper;
import com.br.livewallpaper.model.WallpaperItem;
import com.br.livewallpaper.view.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewWallpaperActivity extends AppCompatActivity {

    ActivityViewWallpaperBinding binding;

    //Room Database
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Common.PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlertDialog dialog = new SpotsDialog(ViewWallpaperActivity.this);
                    dialog.show();
                    dialog.setMessage("Por favor espere...");

                    String fileName = UUID.randomUUID().toString() + ".png";
                    Picasso.with(getBaseContext())
                            .load(Common.select_background.getImageLink())
                            .into(new SaveImageHelper(getBaseContext(),
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName,
                                    "WFG Live Wallpaper Image"));
                } else
                    Toast.makeText(this, "Você precisa aceitar a permissão para baixar a imagem", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    //Definir Wallpaper como tela de fundo do device
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(binding.layoutRoot,"Wallpaper definido como tela de fundo.", Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_wallpaper);
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(this);
        recentRepository = RecentRepository.getInstance( RecentsDataSource.getInstance(database.recentsDAO()) );

        init();

    }

    private void init() {
        binding.collapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        binding.collapsing.setExpandedTitleTextAppearance(R.style.ExpanableAppBar);
        binding.collapsing.setTitle(Common.CATEGORY_SELECTED);

        Picasso.with(this)
                .load(Common.select_background.getImageLink())
                .into(binding.imgThumb);

        //add to recents
        addToRecents();


        binding.fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.select_background.getImageLink())
                        .into(target);
            }
        });

        downloadImage();
    }

    private void downloadImage() {
        binding.fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request runtime permission
                if (ActivityCompat.checkSelfPermission(ViewWallpaperActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
                    }
                }else{
                    AlertDialog dialog = new SpotsDialog(ViewWallpaperActivity.this);
                    dialog.show();
                    dialog.setMessage("Por favor espere...");

                    String fileName = UUID.randomUUID().toString() + ".png";
                    Picasso.with(getBaseContext())
                            .load(Common.select_background.getImageLink())
                            .into(new SaveImageHelper(getBaseContext(),
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName,
                                    "WFGDev Live Wallpaper Image"));

                }
            }
        });

        //view count
        increaseViewCount();

    }

    //aumentar contagem de visualização
    private void increaseViewCount() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(Common.select_background_key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("viewCount")){
                            WallpaperItem wallpaperItem = dataSnapshot.getValue(WallpaperItem.class);
                            long count = wallpaperItem.getViewCount() + 1;
                            //Update
                            Map<String,Object> updateView = new HashMap<>();
                            updateView.put("viewCount", count);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(updateView)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpaperActivity.this, "Não foi possível atualizar contagem de visualizações", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            //Update
                            Map<String,Object> updateView = new HashMap<>();
                            updateView.put("viewCount", Long.valueOf(1));

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(updateView)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpaperActivity.this, "Não foi possível atualizar conjunto padrão de visualizações", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void addToRecents() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Recents recents = new Recents(
                        Common.select_background.getImageLink(),
                        Common.select_background.getCategoryId(),
                        String.valueOf(System.currentTimeMillis()),
                        Common.select_background_key);
                recentRepository.insertRecents(recents);
                e.onComplete();

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR_ADD",throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(target);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish(); //Fecha activity ao clicar no botão back
        return super.onOptionsItemSelected(item);
    }
}
