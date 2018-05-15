package com.br.livewallpaper.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.br.livewallpaper.R;
import com.br.livewallpaper.databinding.ActivityViewWallpaperBinding;
import com.br.livewallpaper.helper.SaveImageHelper;
import com.br.livewallpaper.view.Common.Common;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class ViewWallpaperActivity extends AppCompatActivity {

    ActivityViewWallpaperBinding binding;

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

        binding.collapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        binding.collapsing.setExpandedTitleTextAppearance(R.style.ExpanableAppBar);
        binding.collapsing.setTitle(Common.CATEGORY_SELECTED);

        Picasso.with(this)
                .load(Common.select_background.getImageLink())
                .into(binding.imgThumb);

        binding.fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.select_background.getImageLink())
                        .into(target);
            }
        });

        binding.fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request runtime permission
                if (ActivityCompat.checkSelfPermission(ViewWallpaperActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
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
                            "WFG Live Wallpaper Image"));

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish(); //Fecha activity ao clicar no botão back
        return super.onOptionsItemSelected(item);
    }
}
